package net.dragora.recipeapp.base.data.repository

import io.reactivex.Single
import io.reactivex.SingleEmitter
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.network.RecipePayload
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty.Hard
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty.Medium
import net.dragora.recipeapp.base.tools.Loggy
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers

/**
 * Created by luigipapino on 18/02/2018.
 */
class RecipeRepository internal constructor(private val recipeApiService: RecipeApiService) {

    private var data: List<RecipePayload>? = null

    /**
     * Non thread safe
     */
    fun retrieveRecipes(query: String? = null): Single<List<RecipeModel>> {
        val cache = getCachedData()
        if (cache != null) {
            return Single.just(getModels(query))
        }
        return Single.create {
            fetchRecipes(it, query)
        }
    }

    private fun getCachedData(): List<RecipePayload>? {
        return data
    }

    private fun getModels(query: String?): List<RecipeModel> {
        return data.toModels().filter { it.matchQuery(query) }
    }

    private fun fetchRecipes(
            emitter: SingleEmitter<List<RecipeModel>>,
            query: String?) {
        recipeApiService.getRecipes()
                .subscribeOn(LokiSchedulers.NETWORK)
                .observeOn(LokiSchedulers.COMPUTATION)
                .subscribe(
                        {
                            data = it
                            emitter.onSuccess(getModels(query))
                        },
                        {
                            Loggy.e(it, TAG)
                            emitter.onError(RepositoryException(it))
                        }
                )

    }

    sealed class RetrieveEvent {
        class Idle : RetrieveEvent()
        class Fetching : RetrieveEvent()
        data class Fetched(val data: List<RecipeModel>) : RetrieveEvent()
        data class FetchError(val message: String) : RetrieveEvent()

    }

    class RepositoryException(cause: Throwable) : Exception(cause)

    companion object {

        private const val TAG = "RecipeRepository"
        private fun List<RecipePayload>?.toModels(): List<RecipeModel> {
            return this?.map { it.toModel() } ?: emptyList()
        }

        /**
         * This is useless at the moment.
         * It's just to protect the presentation layer from changes in the domain layer
         */
        private fun RecipePayload.toModel(): RecipeModel {
            val ingredients =
                    this.ingredients
                            .map { IngredientModel(it.quantity, it.name, it.type) }
            val steps = this.steps
            val timers = this.timers
            val difficulty = when (steps.size) {
                in 0..3 -> Difficulty.Easy
                in 4..8 -> Medium
                else -> Hard
            }
            val totalTime = this.timers.reduce { acc, i -> acc + i }

            val model = RecipeModel(name, ingredients, steps, timers, imageURL, originalURL,
                    difficulty, totalTime)
            return model
        }

        private fun RecipeModel.matchQuery(query: String?): Boolean {
            query ?: return true

            return when {
                name.contains(query, true) -> true
                ingredients.any { it.matchQuery(query) } -> true
                steps.any { it.contains(query, true) } -> true
                else -> false
            }
        }

        private fun IngredientModel.matchQuery(query: String?): Boolean {
            query ?: return true
            return this.name.contains(query, true) || this.type.contains(query, true)

        }

    }
}