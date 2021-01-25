package com.yang.wanandroid.ui.adapter

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.htmlToSpanned
import com.yang.wanandroid.model.bean.Article
import kotlinx.android.synthetic.main.item_article.view.*

/**
 * @author ym on 1/7/21
 * 首页文章适配器
 */
class ArticleAdapter(layoutResId: Int = R.layout.item_article) :
    BaseQuickAdapter<Article, BaseViewHolder>(layoutResId), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.itemView.run {
            tv_author.text = when {
                !item.author.isNullOrEmpty() -> {
                    item.author
                }
                !item.shareUser.isNullOrEmpty() -> {
                    item.shareUser
                }
                else -> context.getString(R.string.anonymous)
            }
            tv_top.isVisible = item.top
            tv_fresh.isVisible = item.fresh && !item.top
            tv_tag.visibility = if (item.tags.isNotEmpty()) {
                tv_tag.text = item.tags[0].name
                View.VISIBLE
            } else {
                View.GONE
            }
            tv_chapter.text = when {
                !item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                    "${item.superChapterName.htmlToSpanned()}/${item.chapterName.htmlToSpanned()}"
                item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                    item.chapterName.htmlToSpanned()
                !item.superChapterName.isNullOrEmpty() && item.chapterName.isNullOrEmpty() ->
                    item.superChapterName.htmlToSpanned()
                else -> ""
            }
            tv_title.text = item.title.htmlToSpanned()
            tv_desc.text = item.desc.htmlToSpanned()
            tv_desc.isGone = item.desc.isNullOrEmpty()
            tv_time.text = item.niceDate
            iv_collect.isSelected = item.collect
        }
    }
}