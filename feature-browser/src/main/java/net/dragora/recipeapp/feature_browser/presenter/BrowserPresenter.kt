package net.dragora.recipeapp.feature_browser.presenter

import io.reactivex.Observable
import net.dragora.recipeapp.base.data.repository.RecipeModel
import net.dragora.recipeapp.base.tools.android.formatMinutesDuration
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers.Companion.COMPUTATION
import net.dragora.recipeapp.base.tools.rxjava.LokiSchedulers.Companion.MAIN
import net.dragora.recipeapp.base.ui.StringRetriever
import net.dragora.recipeapp.feature_browser.R
import net.dragora.recipeapp.feature_browser.di.BrowserScope
import net.dragora.recipeapp.feature_browser.domain.BrowserUseCase
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.FilterType.Difficulty
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.FilterType.Duration
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel
import javax.inject.Inject

/**
 * Created by luigipapino on 18/02/2018.
 */
@BrowserScope
class BrowserPresenter @Inject constructor(
        private val usecase: BrowserUseCase,
        private val res: StringRetriever) :
        BrowserUseCase.Callback {

    enum class FilterType { Difficulty, Duration }

    enum class FilterDifficulty(val resId: Int, val value: RecipeModel.Difficulty) {
        Easy(R.string.browser_filter_easy, RecipeModel.Difficulty.Easy),
        Medium(R.string.browser_filter_medium, RecipeModel.Difficulty.Medium),
        Hard(R.string.browser_filter_hard, RecipeModel.Difficulty.Hard)
    }

    enum class FilterDuration(val resId: Int, val value: IntRange) {
        To10(R.string.browser_filter_duration_to_10min, 0..10),
        To20(R.string.browser_filter_duration_to_20min, 11..20),
        From20(R.string.browser_filter_duration_more_20min, 21..Int.MAX_VALUE)
    }

    interface View {

        fun showCells(cells: List<CellViewModel>)

        fun onLoading(isLoading: Boolean)

        fun showMessage(message: String)

        fun showFilterChoices(type: FilterType, items: Array<String>)

        fun setFilterChip(type: FilterType, visible: Boolean, label: String)

        data class CellViewModel(
                val recipeId: Int,
                val imageUrl: String,
                val title: String,
                val ingredientsCount: String,
                val minutes: String
        )
    }

    fun init(view: View) {
        this.view = view
        FilterType.values().forEach { view.setFilterChip(it, false, "") }

        usecase.init(this)

        view.onLoading(true)
        usecase.loadRecipes()
    }

    fun destroy() {
        usecase.dispose()
        view = null
    }

    override fun onRecipesRetrieved(recipe: List<RecipeModel>) {
        Observable.fromCallable {
            recipe.map {
                val ingredientsCount = it.ingredients.size.toString()
                val minutes = it.duration.formatMinutesDuration(res)
                CellViewModel(it.id,
                        it.imageURL,
                        it.name,
                        res.getString(R.string.browser_cell_ingredients, ingredientsCount),
                        minutes)
            }
        }
                .subscribeOn(COMPUTATION)
                .observeOn(MAIN)
                .subscribe {
                    view?.onLoading(false)
                    view?.showCells(it)
                }

    }

    private fun updateRecipes() {
        val difficulty = lastFilterType[Difficulty] as FilterDifficulty?
        val duration = lastFilterType[Duration] as FilterDuration?
        usecase.loadRecipes(lastQuery, difficulty?.value, duration?.value)
    }

    fun queryChange(query: String) {
        lastQuery = query
        updateRecipes()
    }

    fun onFilterDelete(type: FilterType) {
        view?.setFilterChip(type, false, "")
        lastFilterType.remove(type)
        updateRecipes()

    }

    fun onFilterClick(type: FilterType) {
        val items = when (type) {
            Difficulty -> FilterDifficulty.values().map { res.getString(it.resId) }
            Duration -> FilterDuration.values().map { res.getString(it.resId) }
        }
        view?.showFilterChoices(type, items.toTypedArray())
    }

    fun onFilterChoice(type: FilterType, index: Int) {
        when (type) {
            Difficulty -> onFilterDifficultyChoice(index)
            Duration -> onFilterDifficultyDuration(index)
        }

    }

    private fun onFilterDifficultyChoice(index: Int) {
        val item = FilterDifficulty.values()[index]
        lastFilterType[Difficulty] = item
        view?.setFilterChip(Difficulty, true, res.getString(item.resId))
        updateRecipes()

    }

    private fun onFilterDifficultyDuration(index: Int) {
        val item = FilterDuration.values()[index]
        lastFilterType[Duration] = item
        view?.setFilterChip(Duration, true, res.getString(item.resId))
        updateRecipes()
    }

    override fun onError(message: String) {
        view?.onLoading(false)
        view?.showMessage(message)
    }

    fun clickCell(recipeId: Int) {
        usecase.startRecipe(recipeId)

    }

    private var lastQuery: String? = null
    private val lastFilterType = mutableMapOf<FilterType, Any>()
    private var view: View? = null

}