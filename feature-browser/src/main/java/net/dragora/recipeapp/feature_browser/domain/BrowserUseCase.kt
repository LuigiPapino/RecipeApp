package net.dragora.recipeapp.feature_browser.domain

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import net.dragora.recipeapp.base.data.repository.RecipeModel
import net.dragora.recipeapp.base.data.repository.RecipeRepository
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.FetchError
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.Fetched
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.Fetching
import net.dragora.recipeapp.base.data.repository.RecipeRepository.RetrieveEvent.Idle
import net.dragora.recipeapp.base.tools.Loggy
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers
import net.dragora.recipeapp.feature_browser.di.BrowserScope
import javax.inject.Inject

/**
 * Created by luigipapino on 18/02/2018.
 */
@BrowserScope
class BrowserUseCase @Inject constructor(private val recipeRepository: RecipeRepository) {

    private var disposables = CompositeDisposable()

    private var callback: Callback? = null

    fun init(callback: Callback) {
        disposables = CompositeDisposable()
        this.callback = callback
    }

    fun loadRecipes() {
        disposables += recipeRepository
                .retrieveRecipes()
                .observeOn(LokiSchedulers.MAIN)
                .doOnSubscribe { callback?.onLoading() }
                .subscribe {
                    Loggy.d("retrieveRecipes event ${it}")
                    when (it) {
                        is Idle -> {
                            /**ignore**/
                        }
                        is Fetching -> {
                            callback?.onLoading()
                        }
                        is Fetched -> {
                            callback?.onRecipesRetrieved(it.data)
                        }
                        is FetchError -> {
                            callback?.onError(it.message)
                        }
                    }
                }
    }

    fun dispose() {
        callback = null
        disposables.dispose()
    }

    interface Callback {
        fun onError(message: String)
        fun onLoading()
        fun onRecipesRetrieved(recipe: List<RecipeModel>)

    }

}