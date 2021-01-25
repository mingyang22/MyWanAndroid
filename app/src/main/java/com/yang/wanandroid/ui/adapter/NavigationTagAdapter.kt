package com.yang.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import com.yang.wanandroid.R
import com.yang.wanandroid.model.bean.Article
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

/**
 * @author ym on 1/20/21
 * 导航 Tag适配器
 */
class NavigationTagAdapter(private val articles: List<Article>) :
    TagAdapter<Article>(articles) {

    override fun getView(parent: FlowLayout?, position: Int, t: Article?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false).apply {
                tvTag.text = articles[position].title
            }
    }
}