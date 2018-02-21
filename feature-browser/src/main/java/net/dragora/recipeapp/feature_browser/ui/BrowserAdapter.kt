package net.dragora.recipeapp.feature_browser.ui

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.dragora.recipeapp.feature_browser.presenter.BrowserPresenter.View.CellViewModel

/**
 * Created by luigipapino on 21/02/2018.
 */
class BrowserAdapter(clickCell: (Int) -> Unit) :
        BaseQuickAdapter<CellViewModel, BaseViewHolder>(
                emptyList()) {

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseViewHolder {
        return if (layoutResId == 0) {
            BaseViewHolder(BrowserCellView(mContext))
        } else {
            super.createBaseViewHolder(parent, layoutResId)
        }
    }

    override fun convert(helper: BaseViewHolder, item: CellViewModel) {
        val cell = helper.itemView as BrowserCellView
        cell.bind(item)
    }
}