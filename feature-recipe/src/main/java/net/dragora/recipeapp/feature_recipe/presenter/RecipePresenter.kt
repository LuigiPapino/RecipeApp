package net.dragora.recipeapp.feature_recipe.presenter

import com.chad.library.adapter.base.entity.MultiItemEntity
import net.dragora.recipeapp.base.data.repository.RecipeModel
import net.dragora.recipeapp.base.ui.StringRetriever
import net.dragora.recipeapp.feature_recipe.R
import net.dragora.recipeapp.feature_recipe.domain.RecipeUseCase
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Ingredient
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Section
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Step
import javax.inject.Inject

/**
 * Created by luigipapino on 26/02/2018.
 */
class RecipePresenter @Inject constructor(private val useCase: RecipeUseCase,
        private val res: StringRetriever) :
        RecipeUseCase.Callback {

    interface View {
        fun show(viewModel: ViewModel)

    }

    data class ViewModel(
            val name: String,
            val imageUri: String,
            val rows: List<RowViewModel>
    )

    sealed class RowViewModel : MultiItemEntity {
        data class Section(val title: String) : RowViewModel() {
            override fun getItemType(): Int = TYPE

            companion object {
                const val TYPE = 1
            }
        }

        data class Ingredient(val name: String, val quantity: String) : RowViewModel() {
            override fun getItemType(): Int = TYPE

            companion object {
                const val TYPE = 2
            }
        }

        data class Step(val description: String, val duration: Int) : RowViewModel() {
            override fun getItemType(): Int = TYPE

            companion object {
                const val TYPE = 3
            }
        }

    }

    fun init(view: View) {
        this.view = view
        useCase.init(this)

        useCase.loadRecipe()

    }

    override fun recipeNotFound() {
    }

    override fun onRecipeRetrieved(recipe: RecipeModel) {
        val rows = mutableListOf<RowViewModel>()

        //Ingredients
        rows.add(Section(res.getString(R.string.recipe_section_ingredients,
                recipe.ingredients.size)))
        recipe.ingredients.forEach {
            rows.add(Ingredient(it.name, it.quantity))
        }

        //Steps
        rows.add(Section(res.getString(R.string.recipe_section_steps)))
        recipe.steps.forEachIndexed { index, it ->
            rows.add(Step(it, recipe.timers[index]))
        }

        val name = recipe.name
        val imageUri = recipe.imageURL

        view?.show(ViewModel(name, imageUri, rows))
    }

    override fun onError(message: String) {
    }

    fun destroy() {
        useCase.dispose()
        view = null
    }

    private var view: View? = null
}