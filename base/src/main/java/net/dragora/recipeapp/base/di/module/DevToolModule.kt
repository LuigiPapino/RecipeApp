package net.dragora.recipeapp.base.di.module

import android.app.Application
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER

@Target(FUNCTION, PROPERTY_GETTER)
annotation class DevTool

@Module
internal object DevToolModule {

    @Provides
    @IntoSet
    @DevTool
    @JvmStatic
    internal fun devInitStetho(app: Application): () -> Unit = {
        Stetho.initializeWithDefaults(app)
    }

    @Provides
    @IntoSet
    @DevTool
    @JvmStatic
    internal fun devInitLeakCanary(app: Application): () -> Unit = {
        if (LeakCanary.isInAnalyzerProcess(app)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.

        } else {
            LeakCanary.refWatcher(app).buildAndInstall()
        }
    }

}