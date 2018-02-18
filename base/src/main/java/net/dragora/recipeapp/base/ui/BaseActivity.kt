package net.dragora.recipeapp.base.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by luigipapino on 18/02/2018.
 * Copyright Drop Kitchen
 */
abstract class BaseActivity(private val layoutId: Int? = null) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId?.let {
            setContentView(it)
        }
    }
}