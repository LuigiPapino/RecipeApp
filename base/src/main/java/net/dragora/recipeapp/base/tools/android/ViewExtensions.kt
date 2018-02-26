package net.dragora.recipeapp.base.tools.android

import android.view.View
import net.dragora.recipeapp.R
import net.dragora.recipeapp.base.ui.StringRetriever

/**
 * Created by luigipapino on 26/02/2018.
 */
fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun Long.formatDuration(res: StringRetriever): CharSequence {

    val HOUR_IN_MILLIS = 60 * 60 * 1000
    val MINUTE_IN_MILLIS = 60 * 1000
    val SECOND_IN_MILLIS = 1000

    when {
        this >= HOUR_IN_MILLIS -> {
            val hours = ((this + 1800000) / HOUR_IN_MILLIS).toInt()
            return res.getQuantityString(R.plurals.common_hours, hours, hours)
        }
        this >= MINUTE_IN_MILLIS -> {
            val minutes = ((this + 30000) / MINUTE_IN_MILLIS).toInt()
            return res.getQuantityString(R.plurals.common_minutes, minutes, minutes)
        }
        else -> {
            val seconds = ((this + 500) / SECOND_IN_MILLIS).toInt()
            return res.getQuantityString(R.plurals.common_seconds, seconds, seconds)
        }
    }
}

fun Int.formatMinutesDuration(res: StringRetriever): String {
    return (this * 60L * 1000L).formatDuration(res).toString()
}