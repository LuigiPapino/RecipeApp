package net.dragora.recipeapp.feature_browser.ui

import android.content.Context
import android.net.Uri
import kotlinx.android.synthetic.main.browser_cell.view.browser_cell_image
import kotlinx.android.synthetic.main.browser_cell.view.browser_cell_quantity
import kotlinx.android.synthetic.main.browser_cell.view.browser_cell_time
import kotlinx.android.synthetic.main.browser_cell.view.browser_cell_title
import net.dragora.recipeapp.base.ui.LokiFrameLayout
import net.dragora.recipeapp.feature_browser.R
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel

/**
 * Created by luigipapino on 18/02/2018.
 */

class BrowserCellView(context: Context) : LokiFrameLayout(context, null, R.layout.browser_cell) {

    internal fun bind(viewModel: CellViewModel) {
        with(viewModel) {
            browser_cell_title.text = title
            browser_cell_quantity.text = ingredientsCount
            browser_cell_time.text = minutes
            browser_cell_image.setImageURI(Uri.parse(viewModel.imageUrl))

        }

    }
}