package com.yang.wanandroid.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.model.bean.HotWord
import kotlinx.android.synthetic.main.item_hot_word.view.*

/**
 * @author ym on 1/20/21
 * 发现/热门搜索
 */
class HotWordsAdapter(layoutResId: Int = R.layout.item_hot_word) :
    BaseQuickAdapter<HotWord, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: HotWord) {
        holder.itemView.tvName.text = item.name
    }
}