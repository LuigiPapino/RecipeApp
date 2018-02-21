package net.dragora.recipeapp.base.di

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import net.dragora.recipeapp.base.LokiApplication
import net.dragora.recipeapp.base.di.module.DevTool
import net.dragora.recipeapp.base.di.module.InitAction
import net.dragora.recipeapp.base.di.module.InitModule
import net.dragora.recipeapp.base.di.module.NetworkModule
import net.dragora.recipeapp.base.di.module.RepositoryModule
import javax.inject.Singleton

/**
 * Created by luigipapino on 18/02/2018.
 */
@Singleton
@Component(
        modules = [AppModule::class, InitModule::class, NetworkModule::class, RepositoryModule::class])
interface ApplicationComponent : AndroidInjector<LokiApplication> {

    @get:InitAction
    val initActions: Set<() -> Unit>

    @get:InitAction
    @get:DevTool
    val devInitActions: Set<() -> Unit>

    fun baseSubcomponentBuilder(): BaseSubComponent.Builder

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<LokiApplication>()
}

@Module
internal object AppModule {
    @Provides
    @Singleton
    @JvmStatic
    internal fun provideApp(app: LokiApplication): Application = app
}

