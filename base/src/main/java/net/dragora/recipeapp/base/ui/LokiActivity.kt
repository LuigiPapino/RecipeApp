package net.dragora.recipeapp.base.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.dragora.recipeapp.base.LokiApplication
import net.dragora.recipeapp.base.di.ApplicationProvider

/**
 * Created by luigipapino on 18/02/2018.
 */
abstract class LokiActivity(private val layoutId: Int? = null) : AppCompatActivity(),
        ApplicationProvider, StringRetriever {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId?.let {
            setContentView(it)
        }
    }

    override fun provideApp(): LokiApplication = application as LokiApplication

    override fun getQuantityString(resId: Int, quantity: Int, vararg formatArgs: Any): String {
        return resources.getQuantityString(resId, quantity, *formatArgs)
    }
}