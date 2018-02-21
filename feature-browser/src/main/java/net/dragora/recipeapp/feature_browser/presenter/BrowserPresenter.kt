package net.dragora.recipeapp.feature_browser.presenter

import io.reactivex.Observable
import net.dragora.recipeapp.base.data.repository.RecipeModel
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers.Companion.COMPUTATION
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers.Companion.MAIN
import net.dragora.recipeapp.feature_browser.di.BrowserScope
import net.dragora.recipeapp.feature_browser.domain.BrowserUseCase
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel
import javax.inject.Inject

/**
 * Created by luigipapino on 18/02/2018.
 */
@BrowserScope
class BrowserPresenter @Inject constructor(private val usecase: BrowserUseCase) :
        BrowserUseCase.Callback {

    private var view: View? = null

    internal fun init(view: View) {
        this.view = view
        usecase.init(this)

        view.onLoading(true)
        usecase.loadRecipes()
    }

    fun destroy() {
        usecase.dispose()
        view = null
    }

    override fun onError(message: String) {
        view?.onLoading(false)
        view?.onShowMessage(message)
    }

    override fun onRecipesRetrieved(recipe: List<RecipeModel>) {
        Observable.fromCallable {
            recipe.map {
                val ingredientsCount = it.ingredients.size.toString()
                val minutes = it.totalTimeSeconds.div(60).toString()//Todo prettify timer
                CellViewModel(it.imageURL, it.name, ingredientsCount, minutes)
            }
        }
                .subscribeOn(COMPUTATION)
                .observeOn(MAIN)
                .subscribe {
                    view?.onLoading(false)
                    view?.onLoadCells(it)
                }

    }

    interface View {

        fun onLoadCells(cells: List<CellViewModel>)

        fun onLoading(isLoading: Boolean)
        fun onShowMessage(message: String)

        enum class Difficulty { Easy, Medium, Hard }

        data class CellViewModel(
                val imageUrl: String,
                val title: String,
                val ingredientsCount: String,
                val minutes: String
        )
    }

    fun queryChange(query: String) {
        usecase.loadRecipes(query)
    }

    companion object {

    }
}