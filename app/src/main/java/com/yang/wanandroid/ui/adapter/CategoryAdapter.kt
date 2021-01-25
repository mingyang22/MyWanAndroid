package com.yang.wanandroid.ui.adapter

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.dpToPx
import com.yang.wanandroid.ext.htmlToSpanned
import com.yang.wanandroid.model.bean.Category
import kotlinx.android.synthetic.main.item_category_sub.view.*

/**
 * @author ym on 1/8/21
 * RecycleView 类别适配器
 */
class CategoryAdapter(layoutResId: Int = R.layout.item_category_sub) :
    BaseQuickAdapter<Category, BaseViewHolder>(layoutResId), LoadMoreModule {

    private var checkedPosition = 0
    var onCheckedListener: ((position: Int) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Category) {
        holder.itemView.run {
            ctvCategory.text = item.name.htmlToSpanned()
            ctvCategory.isChecked = checkedPosition == holder.adapterPosition
            setOnClickListener {
                val position = holder.adapterPosition
                check(position)
                onCheckedListener?.invoke(position)
            }
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = if (holder.adapterPosition == 0) 8.dpToPx().toInt() else 0
            }
        }

    }

    fun check(position: Int) {
        checkedPosition = position
        notifyDataSetChanged()
    }
}