package com.yang.wanandroid.ui.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.toDateTime
import com.yang.wanandroid.model.bean.PointRecord
import kotlinx.android.synthetic.main.item_mine_points.view.*

/**
 * @author ym on 1/21/21
 * 我的积分
 */
class MinePointsAdapter : BaseQuickAdapter<PointRecord, BaseViewHolder>(R.layout.item_mine_points),
    LoadMoreModule {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: PointRecord) {
        holder.itemView.run {
            tvReason.text = item.reason
            tvTime.text = item.date.toDateTime("YYYY-MM-dd HH:mm:ss")
            tvPoint.text = "+${item.coinCount}"
        }
    }
}