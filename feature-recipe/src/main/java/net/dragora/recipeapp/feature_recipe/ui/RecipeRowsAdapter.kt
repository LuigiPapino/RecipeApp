package net.dragora.recipeapp.feature_recipe.ui

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Ingredient
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Section
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.RowViewModel.Step

/**
 * Created by luigipapino on 26/02/2018.
 */
internal class RecipeRowsAdapter :
        BaseMultiItemQuickAdapter<RowViewModel, BaseViewHolder>(emptyList()) {

    init {
        addItemType(RowViewModel.Section.TYPE, 0)
        addItemType(RowViewModel.Ingredient.TYPE, 0)
        addItemType(RowViewModel.Step.TYPE, 0)

    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            BaseViewHolder(
                    when (viewType) {
                        Section.TYPE -> RecipeSectionView(mContext)
                        Ingredient.TYPE -> RecipeIngredientView(mContext)
                        Step.TYPE -> RecipeStepView(mContext)
                        else -> throw IllegalArgumentException("viewType $viewType not supported")
                    }
            )

    override fun convert(helper: BaseViewHolder, item: RowViewModel) {
        when (item) {
            is RowViewModel.Section -> (helper.itemView as RecipeSectionView).bind(item)
            is RowViewModel.Ingredient -> (helper.itemView as RecipeIngredientView).bind(item)
            is RowViewModel.Step -> (helper.itemView as RecipeStepView).bind(item)
        }
    }

}