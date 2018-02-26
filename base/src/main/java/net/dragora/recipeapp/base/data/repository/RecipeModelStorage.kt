package net.dragora.recipeapp.base.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by luigipapino on 26/02/2018.
 */

@Singleton
class RecipeModelStorage @Inject constructor(private val prefs: SharedPreferences,
        private val moshi: Moshi) {

    @SuppressLint("ApplySharedPref")
    fun store(value: List<RecipeModel>) {
        val type = Types.newParameterizedType(List::class.java,
                RecipeModel::class.java)
        val adapter = moshi.adapter<List<RecipeModel>>(type)
        val json = adapter.toJson(value)

        prefs.edit()
                .putString(KEY_RECIPES, json)
                .putLong(KEY_RECIPES_TIME, System.currentTimeMillis())
                .commit()
    }

    @Throws(StorageExpired::class)
    fun retrieve(): List<RecipeModel> {
        val type = Types.newParameterizedType(List::class.java,
                RecipeModel::class.java)
        val adapter = moshi.adapter<List<RecipeModel>>(type)
        val json = prefs.getString(KEY_RECIPES, null)
        val time = prefs.getLong(KEY_RECIPES_TIME, 0L)
        if (time + TTL >= System.currentTimeMillis()) {
            return adapter.fromJson(json) ?: emptyList()
        } else {
            throw StorageExpired()
        }

    }

    class StorageExpired : Exception()

    companion object {

        private const val KEY_RECIPES = "recipes"
        private const val KEY_RECIPES_TIME = "recipes-time"

        private const val TTL = 60 * 60 * 1000

    }
}