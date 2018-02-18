package net.dragora.recipeapp.base.di

import dagger.Subcomponent
import net.dragora.recipeapp.base.data.repository.RecipeRepository

/**
 * Created by luigipapino on 18/02/2018.
 */

@Subcomponent
interface BaseSubComponent {

    val recipeRepository: RecipeRepository

    @Subcomponent.Builder
    interface Builder {
        fun build(): BaseSubComponent
    }
}