package com.yang.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import com.yang.wanandroid.R
import com.yang.wanandroid.model.bean.Frequently
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

/**
 * @author ym on 1/20/21
 * 发现/常用网址 Tag适配器
 */
class DiscoveryTagAdapter(private val frequentlyList: List<Frequently>) :
    TagAdapter<Frequently>(frequentlyList) {

    override fun getView(parent: FlowLayout?, position: Int, t: Frequently?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false).apply {
                tvTag.text = frequentlyList[position].name
            }
    }
}