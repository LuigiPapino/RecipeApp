package net.dragora.recipeapp.feature_browser.domain

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import net.dragora.recipeapp.base.data.repository.RecipeModel
import net.dragora.recipeapp.base.data.repository.RecipeModelFilter
import net.dragora.recipeapp.base.data.repository.RecipeModelFilter.Difficulty
import net.dragora.recipeapp.base.data.repository.RecipeModelFilter.Duration
import net.dragora.recipeapp.base.data.repository.RecipeModelFilter.Query
import net.dragora.recipeapp.base.data.repository.RecipeRepository
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

    fun loadRecipes(query: String? = null, difficulty: RecipeModel.Difficulty? = null,
            duration: IntRange? = null) {
        val filters = mutableListOf<RecipeModelFilter>()
        if (query != null) {
            filters.add(Query(query))
        }
        if (difficulty != null) {
            filters.add(Difficulty(difficulty))
        }
        if (duration != null) {
            filters.add(Duration(duration))
        }

        disposables += recipeRepository
                .retrieveRecipes(filters)
                .observeOn(LokiSchedulers.MAIN)
                .subscribe(
                        {
                            callback?.onRecipesRetrieved(it)
                        },
                        {
                            callback?.onError(it.message ?: "")
                        })
    }

    fun dispose() {
        callback = null
        disposables.dispose()
    }

    interface Callback {
        fun onError(message: String)
        fun onRecipesRetrieved(recipe: List<RecipeModel>)

    }

    companion object {

    }

}

