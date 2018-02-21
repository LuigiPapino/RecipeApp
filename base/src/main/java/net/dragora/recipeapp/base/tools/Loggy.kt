package net.dragora.recipeapp.base.tools

import android.util.Log

/**
 * Created by luigipapino on 21/02/2018.
 */
object Loggy {
    private const val TAG = "Loki"

    fun d(msg: String, tag: String = TAG) {
        Log.d(tag, msg)
    }
}