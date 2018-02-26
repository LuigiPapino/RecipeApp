package net.dragora.recipeapp.base.ui

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.widget.FrameLayout

/**
 * Created by luigipapino on 18/02/2018.
 */
open class LokiFrameLayout(context: Context, @LayoutRes layoutResId: Int = 0) :
        FrameLayout(context) {

    init {
        if (layoutResId != 0) {
            val inflater = context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(layoutResId, this, true)
            layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT)
        }
    }
}