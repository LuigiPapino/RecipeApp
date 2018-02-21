package net.dragora.recipeapp.base.data.repository

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.network.RecipePayload
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.FetchError
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.Fetched
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.Fetching
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.Idle
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers

/**
 * Created by luigipapino on 18/02/2018.
 */
class RecipeRepository internal constructor(private val recipeApiService: RecipeApiService) {

    private val recipeSubject = BehaviorSubject.createDefault<RetrieveEvent>(Idle())

    fun retrieveRecipes(): Observable<RetrieveEvent> {
        refreshRecipes()
        return recipeSubject
    }

    private fun refreshRecipes() {
        synchronized(recipeSubject) {
            when (recipeSubject.value) {
                is Idle,
                is FetchError -> {
                    fetchRecipes()
                }
                is Fetching -> {
                }
                is Fetched -> {
                }

            }
        }

    }

    private fun fetchRecipes() {
        recipeApiService.getRecipes()
                .subscribeOn(LokiSchedulers.NETWORK)
                .observeOn(LokiSchedulers.COMPUTATION)
                .subscribe(
                        {
                            recipeSubject.onNext(Fetched(it.toModels()))
                        },
                        {
                            recipeSubject.onNext(FetchError(it.message ?: ""))

                        }
                )

    }

    sealed class RetrieveEvent {
        class Idle : RetrieveEvent()
        class Fetching : RetrieveEvent()
        data class Fetched(val data: List<RecipeModel>) : RetrieveEvent()
        data class FetchError(val message: String) : RetrieveEvent()

    }

    companion object {

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

            val model = RecipeModel(name, ingredients, steps, timers, imageURL, originalURL)
            return model
        }
    }
}
