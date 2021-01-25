package com.yang.wanandroid.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.htmlToSpanned
import com.yang.wanandroid.model.bean.Category
import kotlinx.android.synthetic.main.item_system_category.view.*

/**
 * @author ym on 1/19/21
 * 体系 过滤选择
 */
class SystemCategoryAdapter(
    layoutResId: Int = R.layout.item_system_category,
    categoryList: MutableList<Category>, var checked: Pair<Int, Int>,
) : BaseQuickAdapter<Category, BaseViewHolder>(layoutResId, categoryList), LoadMoreModule {

    var onCheckedListener: ((checked: Pair<Int, Int>) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Category) {
        holder.itemView.run {
            title.text = item.name.htmlToSpanned()
            tagFlowLayout.adapter = SystemTagAdapter(item.children)
            if (checked.first == holder.adapterPosition) {
                tagFlowLayout.adapter.setSelectedList(checked.second)
            }
            tagFlowLayout.setOnTagClickListener { _, position, _ ->
                checked = holder.adapterPosition to position
                notifyDataSetChanged()
                tagFlowLayout.postDelayed({ onCheckedListener?.invoke(checked) }, 300)
                true
            }

        }

    }
}