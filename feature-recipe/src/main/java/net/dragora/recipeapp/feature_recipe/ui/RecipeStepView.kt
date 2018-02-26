package net.dragora.recipeapp.feature_recipe.ui

import android.content.Context
import kotlinx.android.synthetic.main.recipe_step_view.view.step_title
import net.dragora.recipeapp.base.ui.LokiFrameLayout
import net.dragora.recipeapp.feature_recipe.R
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Step

/**
 * Created by luigipapino on 26/02/2018.
 */
class RecipeStepView(context: Context) :
        LokiFrameLayout(context, R.layout.recipe_step_view) {

    fun bind(vm: Step) {
        step_title.text = vm.description
    }
}