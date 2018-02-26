package net.dragora.recipeapp.base.ui

import android.support.annotation.PluralsRes
import android.support.annotation.StringRes

/**
 * Created by luigipapino on 26/02/2018.
 */
interface StringRetriever {

    fun getString(@StringRes resId: Int, vararg values: Any): String

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String

}