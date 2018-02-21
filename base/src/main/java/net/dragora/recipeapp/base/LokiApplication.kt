package net.dragora.recipeapp.base

import android.app.Application
import android.os.Build
import android.os.StrictMode
import net.dragora.recipeapp.base.BuildType.DEBUG
import net.dragora.recipeapp.base.di.ApplicationComponent
import net.dragora.recipeapp.base.di.DaggerApplicationComponent

/**
 * Created by luigipapino on 18/02/2018.
 */
class LokiApplication : Application() {

    private lateinit var appComponent: ApplicationComponent

    internal fun provideBaseSubComponent() = appComponent.baseSubcomponentBuilder().build()

    override fun onCreate() {
        appComponent = DaggerApplicationComponent.builder().create(this) as ApplicationComponent
        super.onCreate()
        appComponent.initActions.forEach { it.invoke() }
        appComponent.devInitActions.forEach { it.invoke() }
        enableStrictMode()
    }

    private fun enableStrictMode() {
        if (DEBUG.isCurrentBuild) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .penaltyDeathOnNetwork()
                    .build())

            val vmPolicyBuilder = StrictMode.VmPolicy.Builder().detectActivityLeaks()
                    .detectFileUriExposure()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog()
                    .penaltyDeath()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vmPolicyBuilder.detectContentUriWithoutPermission()
            }
            StrictMode.setVmPolicy(vmPolicyBuilder.build())
        }
    }
}