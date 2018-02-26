package net.dragora.recipeapp.feature_browser.di

import dagger.Component
import dagger.Module
import dagger.Provides
import net.dragora.recipeapp.base.di.BaseInjector
import net.dragora.recipeapp.base.di.BaseSubComponent
import net.dragora.recipeapp.base.ui.StringRetriever
import net.dragora.recipeapp.feature_browser.ui.BrowserActivity
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Created by luigipapino on 18/02/2018.
 */

@Scope
@Retention(RUNTIME)
annotation class BrowserScope

@BrowserScope
@Component(
        modules = [BrowserModule::class],
        dependencies = [BaseSubComponent::class])
interface BrowserComponent : BaseInjector<BrowserActivity> {

    @Component.Builder
    abstract class Builder : BaseInjector.Builder<BrowserActivity>()
}

@Module
object BrowserModule {

    @Provides
    @BrowserScope
    @JvmStatic
    fun provideStringRetriever(context: BrowserActivity): StringRetriever = context
}