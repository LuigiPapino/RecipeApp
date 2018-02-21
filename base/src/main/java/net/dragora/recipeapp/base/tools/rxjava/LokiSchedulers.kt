package net.dragora.recipeapp.base.tools.rxjava

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by luigipapino on 21/02/2018.
 */
class LokiSchedulers {

    companion object {
        val COMPUTATION = Schedulers.computation()
        val NETWORK = Schedulers.io()
        val MAIN = AndroidSchedulers.mainThread()

        fun ui(action: () -> Unit) {
            MAIN.scheduleDirect(action)
        }

        fun worker(action: () -> Unit) {
            COMPUTATION.scheduleDirect(action)
        }
    }
}