package com.yang.wanandroid.ui.adapter

import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.htmlToSpanned
import com.yang.wanandroid.model.bean.Article
import kotlinx.android.synthetic.main.item_article.view.*

/**
 * @author ym on 1/8/21
 * 首页文章基础适配器
 */
class SimpleArticleAdapter(layoutResId: Int = R.layout.item_article_simple) :
    BaseQuickAdapter<Article, BaseViewHolder>(layoutResId), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.run {
            itemView.run {
                tv_author.text = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_fresh.isVisible = item.fresh
                tv_title.text = item.title.htmlToSpanned()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
        }
    }
}