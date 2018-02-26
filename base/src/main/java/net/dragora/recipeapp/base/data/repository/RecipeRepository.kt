package net.dragora.recipeapp.base.data.repository

import io.reactivex.Single
import io.reactivex.SingleEmitter
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.network.RecipePayload
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty.Hard
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty.Medium
import net.dragora.recipeapp.base.data.repository.RecipeModelStorage.StorageExpired
import net.dragora.recipeapp.base.tools.Loggy
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers

/**
 * Created by luigipapino on 18/02/2018.
 */
class RecipeRepository internal constructor(
        private val recipeApiService: RecipeApiService,
        private val storage: RecipeModelStorage) {

    private var data: List<RecipeModel>? = null

    /**
     * Non thread safe
     */
    fun retrieveRecipes(filters: List<RecipeModelFilter>): Single<List<RecipeModel>> {
        val cache = getCachedData()
        if (cache != null) {
            data = cache
            return Single.just(getModels(filters))
        }
        return Single.create {
            fetchRecipes(it, filters)
        }
    }

    fun retrieveRecipe(recipeId: Int): Single<RecipeModel> {
        return retrieveRecipes(emptyList())
                .map {
                    it.first { it.id == recipeId }
                }
    }

    private fun fetchRecipes(
            emitter: SingleEmitter<List<RecipeModel>>,
            filters: List<RecipeModelFilter>) {
        Loggy.d("fetchRecipes()")
        recipeApiService.getRecipes()
                .subscribeOn(LokiSchedulers.NETWORK)
                .observeOn(LokiSchedulers.NETWORK)
                .map { store(it) }
                .observeOn(LokiSchedulers.COMPUTATION)
                .subscribe(
                        {
                            emitter.onSuccess(getModels(filters))
                        },
                        {
                            Loggy.e(it, TAG)
                            emitter.onError(RepositoryException(it))
                        }
                )

    }

    private fun getCachedData(): List<RecipeModel>? {
        return try {
            storage.retrieve()
        } catch (ex: StorageExpired) {
            null
        }
    }

    private fun getModels(filters: List<RecipeModelFilter>): List<RecipeModel> {
        return data?.filter { it.matchFilters(filters) } ?: emptyList()
    }

    private fun store(payload: List<RecipePayload>): List<RecipeModel> {
        val data = payload.toModels()
        storage.store(data)
        this.data = data
        return data
    }

    class RepositoryException(cause: Throwable) : Exception(cause)

    companion object {

        private const val TAG = "RecipeRepository"

        private fun List<RecipePayload>?.toModels(): List<RecipeModel> {
            return this?.mapIndexed { index, it -> it.toModel(index) } ?: emptyList()
        }

        /**
         * This is useless at the moment.
         * It's just to protect the presentation layer from changes in the domain layer
         */
        private fun RecipePayload.toModel(recipeId: Int): RecipeModel {
            val ingredients =
                    this.ingredients
                            .map { IngredientModel(it.quantity, it.name, it.type) }
            val steps = this.steps
            val timers = this.timers
            val difficulty = when (steps.size) {
                in 0..4 -> Difficulty.Easy
                in 5..6 -> Medium
                else -> Hard
            }
            val totalTime = this.timers.reduce { acc, i -> acc + i }

            Loggy.d("toModel() steps=${steps.size} duration=$totalTime")

            val model = RecipeModel(recipeId, name, ingredients, steps, timers, imageURL,
                    originalURL,
                    difficulty, totalTime)
            return model
        }

    }

}