package net.dragora.recipeapp.feature_browser.ui

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.Menu
import kotlinx.android.synthetic.main.browser_activity.browser_recycler
import kotlinx.android.synthetic.main.browser_activity.browser_toolbar
import net.dragora.recipeapp.base.ui.LokiActivity
import net.dragora.recipeapp.feature_browser.R
import net.dragora.recipeapp.feature_browser.di.DaggerBrowserComponent
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel
import javax.inject.Inject

class BrowserActivity : LokiActivity(R.layout.browser_activity), View {

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.browser_menu, menu)
        val searchItem = menu.findItem(R.id.browser_action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.queryChange(newText)
                return true
            }
        })


        return super.onCreateOptionsMenu(menu)
    }

    private fun setupView() {
        setSupportActionBar(browser_toolbar)
        browser_recycler.layoutManager = GridLayoutManager(this, 2)

        cellsAdapter = BrowserAdapter { }
        cellsAdapter?.bindToRecyclerView(browser_recycler)
        cellsAdapter?.setEmptyView(R.layout.browser_empty)

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
