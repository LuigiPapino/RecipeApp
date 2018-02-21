package net.dragora.recipeapp.base.di

import dagger.android.AndroidInjector
import net.dragora.recipeapp.base.LokiApplication

/**
 * Created by luigipapino on 18/02/2018.
 */
interface BaseInjector<T : ApplicationProvider> : AndroidInjector<T> {

    abstract class Builder<T : ApplicationProvider> :
            dagger.android.AndroidInjector.Builder<T>() {

        abstract operator fun plus(component: BaseSubComponent): Builder<T>

        fun inject(activity: T) {
            plus(activity.provideApp().provideBaseSubComponent())
            create(activity)
                    .inject(activity)
        }

    }

}

interface ApplicationProvider {

    fun provideApp(): LokiApplication
}