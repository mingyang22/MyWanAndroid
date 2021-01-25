package com.yang.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.htmlToSpanned
import com.yang.wanandroid.model.bean.Category
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_system_category_tag.view.*

/**
 * @author ym on 1/19/21
 * 体系/过滤 Tag适配器
 */
class SystemTagAdapter(private val categoryList: List<Category>) :
    TagAdapter<Category>(categoryList) {
    override fun getView(parent: FlowLayout?, position: Int, t: Category?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_system_category_tag, parent, false).apply {
                tvTag.text = categoryList[position].name.htmlToSpanned()
            }
    }
}