package net.dragora.recipeapp.feature_recipe.di

import dagger.Component
import dagger.Module
import dagger.Provides
import net.dragora.recipeapp.base.di.BaseInjector
import net.dragora.recipeapp.base.di.BaseSubComponent
import net.dragora.recipeapp.base.route.RecipeRoute
import net.dragora.recipeapp.base.ui.StringRetriever
import net.dragora.recipeapp.feature_recipe.ui.RecipeActivity
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Created by luigipapino on 18/02/2018.
 */

@Scope
@Retention(RUNTIME)
annotation class RecipeScope

@RecipeScope
@Component(
        modules = [RecipeModule::class],
        dependencies = [BaseSubComponent::class])
interface RecipeComponent : BaseInjector<RecipeActivity> {

    @Component.Builder
    abstract class Builder : BaseInjector.Builder<RecipeActivity>()
}

@Module
object RecipeModule {

    @Provides
    @RecipeScope
    @JvmStatic
    fun provideStringRetriever(context: RecipeActivity): StringRetriever = context

    @Provides
    @RecipeScope
    @JvmStatic
    fun provideRouteData(context: RecipeActivity): RecipeRoute.Data = RecipeRoute.Data(
            context.intent.data)

}