package com.yang.wanandroid.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.bean.Navigation
import kotlinx.android.synthetic.main.item_navigation.view.*

/**
 * @author ym on 1/20/21
 * 导航适配器
 */
class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    var onItemTagClickListener: ((article: Article) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Navigation) {
        holder.itemView.run {
            title.text = item.name
            tagFlawLayout.adapter = NavigationTagAdapter(item.articles)
            tagFlawLayout.setOnTagClickListener { _, position, _ ->
                onItemTagClickListener?.invoke(item.articles[position])
                true
            }
        }

    }
}