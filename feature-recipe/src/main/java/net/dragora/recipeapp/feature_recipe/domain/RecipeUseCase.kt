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

    interface Callback {
        fun recipeNotFound()

        fun onRecipeRetrieved(recipeModel: RecipeModel)

        fun onError(message: String)

    }

    val isEmbedded = routeData.isEmbedded?.toBoolean() ?: false

    fun init(callback: Callback) {
        disposables = CompositeDisposable()
        this.callback = callback
    }

    fun dispose() {
        disposables.dispose()
        this.callback = null
    }

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

    private var callback: RecipeUseCase.Callback? = null
    private val recipeId = routeData.recipeId?.toInt()
    private var disposables = CompositeDisposable()

}