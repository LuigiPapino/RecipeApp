package net.dragora.recipeapp.feature_recipe.domain

import io.reactivex.disposables.CompositeDisposable
import net.dragora.recipeapp.base.data.repository.RecipeModel
import net.dragora.recipeapp.base.data.repository.RecipeRepository
import net.dragora.recipeapp.base.route.HomeRoute
import net.dragora.recipeapp.base.route.HomeRoute.Data
import net.dragora.recipeapp.base.route.RecipeRoute
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers
import net.dragora.recipeapp.feature_recipe.di.RecipeScope
import javax.inject.Inject

/**
 * Created by luigipapino on 26/02/2018.
 */
@RecipeScope
class RecipeUseCase @Inject constructor(
        private val recipeRepository: RecipeRepository,
        private val routeData: RecipeRoute.Data,
        private val homeRoute: HomeRoute
) {

    private val recipeId = routeData.recipeId?.toInt()
    val isEmbedded = routeData.isEmbedded?.toBoolean() ?: false

    private var disposables = CompositeDisposable()

    interface Callback {
        fun recipeNotFound()

        fun onRecipeRetrieved(recipeModel: RecipeModel)

        fun onError(message: String)

    }

    internal fun init(callback: Callback) {
        disposables = CompositeDisposable()
        this.callback = callback
    }

    internal fun dispose() {
        disposables.dispose()
        this.callback = null
    }

    private var callback: RecipeUseCase.Callback? = null
    fun loadRecipe() {
        if (recipeId == null) {
            callback?.recipeNotFound()

        } else {
            recipeRepository.retrieveRecipe(recipeId)
                    .subscribeOn(LokiSchedulers.NETWORK)
                    .observeOn(LokiSchedulers.MAIN)
                    .subscribe(
                            {
                                callback?.onRecipeRetrieved(it)
                            },
                            {
                                callback?.onError(it.message ?: "")

                            })
        }

    }

    fun navigateUp() {
        homeRoute.start(Data())
    }
}