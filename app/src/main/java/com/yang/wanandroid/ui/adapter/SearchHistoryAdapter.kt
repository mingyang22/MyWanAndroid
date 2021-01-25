package com.yang.wanandroid.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yang.wanandroid.R
import kotlinx.android.synthetic.main.item_search_history.view.*

/**
 * @author ym on 1/14/21
 * 继承系统提供的ListAdapter,简化使用 DiffUtil
 * 必须实现 DiffUtil.ItemCallback
 * 搜索历史适配器
 */
class SearchHistoryAdapter(
    private var context: Context,
    private var layoutResId: Int = R.layout.item_search_history,
) : ListAdapter<String, SearchHistoryHolder>(HistoryDiffCallback()) {

    var onItemClickListener: ((position: Int) -> Unit)? = null
    var onDeleteClickListener: ((position: Int) -> Unit)? = null
    var data = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryHolder {
        return SearchHistoryHolder(LayoutInflater.from(context).inflate(layoutResId, parent, false))
    }

    override fun onBindViewHolder(holder: SearchHistoryHolder, position: Int) {
        holder.itemView.run {
            tvLabel.text = getItem(position)
            setOnClickListener { onItemClickListener?.invoke(holder.adapterPosition) }
            ivDelete.setOnClickListener { onDeleteClickListener?.invoke(holder.adapterPosition) }
        }
    }

    override fun onBindViewHolder(
        holder: SearchHistoryHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        onBindViewHolder(holder, position)
    }

    override fun submitList(list: MutableList<String>?) {
        data = if (list.isNullOrEmpty()) {
            mutableListOf()
        } else {
            ArrayList(list)
        }
        super.submitList(data)
    }

}

class SearchHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class HistoryDiffCallback : DiffUtil.ItemCallback<String>() {
    // 返回两个item是否相等
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        // 当areItemsTheSame返回true时，判断内容是否相等
        return oldItem == newItem
    }

}