package net.dragora.recipeapp.feature_browser

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.dragora.recipeapp.base.data.repository.RecipeRepository
import net.dragora.recipeapp.base.ui.BaseActivity
import net.dragora.recipeapp.feature_browser.di.DaggerBrowserComponent
import javax.inject.Inject

class BrowserActivity : BaseActivity(R.layout.browser_activity) {

    @Inject
    internal lateinit var recipeRepository: RecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerBrowserComponent.builder()
                .inject(this)

        recipeRepository
                .retrieveRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}
