package net.dragora.recipeapp.feature_recipe.ui

import android.content.Context
import kotlinx.android.synthetic.main.recipe_section_view.view.section_title
import net.dragora.recipeapp.base.ui.LokiFrameLayout
import net.dragora.recipeapp.feature_recipe.R
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Section

/**
 * Created by luigipapino on 26/02/2018.
 */
class RecipeSectionView(context: Context) :
        LokiFrameLayout(context, R.layout.recipe_section_view) {

    fun bind(vm: Section) {
        section_title.text = vm.title
    }
}