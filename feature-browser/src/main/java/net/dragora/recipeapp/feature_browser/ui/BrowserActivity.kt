package net.dragora.recipeapp.feature_browser.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.browser_activity.browser_filter_difficulty
import kotlinx.android.synthetic.main.browser_activity.browser_filter_time
import kotlinx.android.synthetic.main.browser_activity.browser_recycler
import kotlinx.android.synthetic.main.browser_activity.browser_toolbar
import net.dragora.recipeapp.base.tools.android.visible
import net.dragora.recipeapp.base.ui.LokiActivity
import net.dragora.recipeapp.feature_browser.R
import net.dragora.recipeapp.feature_browser.di.DaggerBrowserComponent
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.FilterType
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.FilterType.Difficulty
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.FilterType.Duration
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel
import javax.inject.Inject

class BrowserActivity : LokiActivity(R.layout.browser_activity), View {

    override fun setFilterChip(type: FilterType, visible: Boolean, text: String) {
        val view = when (type) {
            Difficulty -> browser_filter_difficulty
            Duration -> browser_filter_time
        }
        with(view) {
            visible(visible)
            label = text
            setOnDeleteClicked { presenter.onFilterDelete(type) }
        }

    }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.browser_action_filter_difficulty -> presenter.onFilterClick(Difficulty)
            R.id.browser_action_filter_duration -> presenter.onFilterClick(Duration)
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setupView() {
        setSupportActionBar(browser_toolbar)
        browser_recycler.layoutManager = GridLayoutManager(this, 2)

        cellsAdapter = BrowserAdapter { presenter.clickCell(it) }
        cellsAdapter?.bindToRecyclerView(browser_recycler)
        cellsAdapter?.setEmptyView(R.layout.browser_empty)

    }

    override fun showCells(cells: List<CellViewModel>) {
        cellsAdapter?.setNewData(cells)
    }

    override fun onLoading(isLoading: Boolean) {

    }

    override fun showMessage(message: String) {
    }

    override fun showFilterChoices(type: FilterType, items: Array<String>) {

        AlertDialog.Builder(this)
                .setItems(items) { _, index ->
                    presenter.onFilterChoice(type, index)
                }
                .show()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
