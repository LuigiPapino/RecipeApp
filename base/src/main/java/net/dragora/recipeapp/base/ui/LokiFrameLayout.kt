package net.dragora.recipeapp.base.ui

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

/**
 * Created by luigipapino on 18/02/2018.
 */
open class LokiFrameLayout(context: Context,
        attrs: AttributeSet? = null, @LayoutRes layoutResId: Int = 0) :
        FrameLayout(context, attrs) {

    init {
        if (layoutResId != 0) {
            val inflater = context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(layoutResId, this, false)
            super.addView(view)
        }
    }
}