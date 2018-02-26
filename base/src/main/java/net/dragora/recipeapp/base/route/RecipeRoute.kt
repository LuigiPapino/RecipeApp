package net.dragora.recipeapp.base.route

import android.content.Context
import android.net.Uri
import android.os.Bundle
import net.dragora.recipeapp.R

/**
 * Created by luigipapino on 26/02/2018.
 */
class RecipeRoute(private val context: Context) : LokiRoute {

    class Data(uri: Uri? = null) : UriData(uri) {
        var recipeId by map
    }

    private fun uri(data: Data): Uri {
        return LokiRoute.builder(context, R.string.route_app_scheme,
                R.string.route_app_host_screens, R.string.route_app_path_recipe)
                .let { data.appendAsQuery(it) }
                .build()
    }

    fun start(data: Data, bundle: Bundle = Bundle()) {
        LokiRoute.start(context, uri(data), bundle)
    }

}