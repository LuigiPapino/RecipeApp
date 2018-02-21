package net.dragora.recipeapp.feature_browser.ui

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.browser_activity.browser_recycler
import net.dragora.recipeapp.base.ui.LokiActivity
import net.dragora.recipeapp.feature_browser.R.layout
import net.dragora.recipeapp.feature_browser.di.DaggerBrowserComponent
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel
import javax.inject.Inject

class BrowserActivity : LokiActivity(
        layout.browser_activity), View {

    private var cellsAdapter: BrowserAdapter? = null

    @Inject
    internal lateinit var presenter: BrowserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        DaggerBrowserComponent.builder()
                .inject(this)
        presenter.init(this)
    }

    private fun setupView() {
        cellsAdapter = BrowserAdapter { }
        browser_recycler.layoutManager = GridLayoutManager(this, 2)
        browser_recycler.adapter = cellsAdapter
    }

    override fun onLoadCells(cells: List<CellViewModel>) {
        cellsAdapter?.setNewData(cells)
    }

    override fun onLoading(isLoading: Boolean) {

    }

    override fun onShowMessage(message: String) {
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
