package com.yang.wanandroid.ui.search.history

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isGone
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.model.bean.HotWord
import com.yang.wanandroid.ui.adapter.SearchHistoryAdapter
import com.yang.wanandroid.ui.search.SearchActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_search_history.*
import kotlinx.android.synthetic.main.item_hot_search.view.*

/**
 * @author ym on 1/13/21
 * 搜索历史
 */
class SearchHistoryFragment : BaseVmFragment<SearchHistoryViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = SearchHistoryFragment()
    }

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun getLayoutId() = R.layout.fragment_search_history

    override fun viewModelClass() = SearchHistoryViewModel::class.java

    override fun initView() {
        searchHistoryAdapter = SearchHistoryAdapter(activity!!).apply {
            onItemClickListener = {
                (activity as? SearchActivity)?.fillSearchInput(data[it])
            }
            onDeleteClickListener = {
                viewModel.deleteSearchHistory(searchHistoryAdapter.data[it])
            }
        }
        rvSearchHistory.adapter = searchHistoryAdapter
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            hotWords.observe(viewLifecycleOwner, {
                tvHotSearch.visibility = View.VISIBLE
                setHotWords(it)
            })
            searchHistory.observe(viewLifecycleOwner, {
                tvSearchHistory.isGone = it.isEmpty()
                searchHistoryAdapter.submitList(it)
            })
        }
    }

    /**
     * 设置热门搜索词
     * @param list List<HotWord>
     */
    private fun setHotWords(list: List<HotWord>) {
        tflHotSearch.run {
            adapter = object : TagAdapter<HotWord>(list) {
                override fun getView(parent: FlowLayout?, position: Int, t: HotWord?): View {
                    return LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_hot_search, parent, false).apply {
                            this.tvTag.text = t?.name
                        }
                }
            }
            setOnTagClickListener { _, position, _ ->
                (activity as? SearchActivity)?.fillSearchInput(list[position].name)
                false
            }
        }
    }

    override fun lazyLoadData() {
        viewModel.getHotSearch()
        viewModel.getSearchHistory()
    }

    fun addSearchHistory(keywords: String) {
        viewModel.addSearchHistory(keywords)
    }

}