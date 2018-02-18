package net.dragora.recipeapp.base.data.repository

import io.reactivex.Observable
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.network.RecipePayload
import net.dragora.recipeapp.base.data.repository.RetrieveEvent.Failed
import net.dragora.recipeapp.base.data.repository.RetrieveEvent.Loading
import net.dragora.recipeapp.base.data.repository.RetrieveEvent.Success

/**
 * Created by luigipapino on 18/02/2018.
 */
class RecipeRepository(private val recipeApiService: RecipeApiService) {

    fun retrieveRecipes(): Observable<RetrieveEvent<List<RecipePayload>>> {

        return Observable.create { emitter ->
            emitter.onNext(Loading())

            recipeApiService.getRecipes()
                    .subscribe(
                            {
                                emitter.onNext(Success(it))
                            },
                            {
                                emitter.onNext(Failed(it.message))
                            })
        }

    }
}

sealed class RetrieveEvent<T> {

    class Failed<T>(val reason: String?) : RetrieveEvent<T>()
    class Loading<T>() : RetrieveEvent<T>()
    class Success<T>(val data: T) : RetrieveEvent<T>()

}