package com.yang.wanandroid.ui.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.model.bean.PointRank
import kotlinx.android.synthetic.main.item_points_rank.view.*

/**
 * @author ym on 1/21/21
 * 积分排行
 */
class PointsRankAdapter : BaseQuickAdapter<PointRank, BaseViewHolder>(R.layout.item_points_rank),
    LoadMoreModule {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: PointRank) {
        holder.itemView.run {
            tvNo.text = "${holder.adapterPosition + 1}"
            tvName.text = item.username
            tvPoints.text = item.coinCount.toString()
        }
    }
}