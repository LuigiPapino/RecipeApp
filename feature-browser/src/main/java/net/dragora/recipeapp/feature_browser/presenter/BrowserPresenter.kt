package net.dragora.recipeapp.feature_browser.presenter

import net.dragora.recipeapp.base.data.repository.RecipeModel
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

    override fun onError(message: String) {
        view?.onShowMessage(message)
    }

    override fun onLoading() {
        view?.onLoading(true)
    }

    override fun onRecipesRetrieved(recipe: List<RecipeModel>) {
        val viewModels = recipe
                .map {
                    val ingredientsCount = it.ingredients.size.toString()
                    val minutes = it.timers
                            .reduce { acc, i -> acc + i }
                            .div(60)
                            .toString() //Todo prettify timer
                    CellViewModel(it.imageURL, it.name, ingredientsCount, minutes)
                }
        view?.onLoadCells(viewModels)
    }

    private var view: View? = null

    internal fun init(view: View) {
        this.view = view
        usecase.init(this)

        usecase.loadRecipes()
    }

    fun destroy() {
        usecase.dispose()
        view = null
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
}