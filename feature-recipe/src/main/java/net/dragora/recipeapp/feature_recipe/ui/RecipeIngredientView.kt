package net.dragora.recipeapp.feature_recipe.ui

import android.content.Context
import kotlinx.android.synthetic.main.recipe_ingredient_view.view.ingredient_quantity
import kotlinx.android.synthetic.main.recipe_ingredient_view.view.ingredient_title
import net.dragora.recipeapp.base.ui.LokiFrameLayout
import net.dragora.recipeapp.feature_recipe.R
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Ingredient

/**
 * Created by luigipapino on 26/02/2018.
 */
class RecipeIngredientView(context: Context) :
        LokiFrameLayout(context, R.layout.recipe_ingredient_view) {

    fun bind(vm: Ingredient) {
        ingredient_title.text = vm.name
        ingredient_quantity.text = vm.quantity
    }
}