package com.yang.wanandroid.ui.search.result

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.adapter.ArticleAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/13/21
 * 搜索结果
 */
class SearchResultFragment : BaseVmFragment<SearchResultViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = SearchResultFragment()
    }

    private lateinit var adapter: ArticleAdapter

    override fun getLayoutId() = R.layout.fragment_search_result

    override fun viewModelClass() = SearchResultViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.search() }
        }
        adapter = ArticleAdapter().apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener { viewModel.loadMore() }
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            addChildClickViewIds(R.id.iv_collect)
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        viewModel.uncollect(article.id)
                    } else {
                        viewModel.collect(article.id)
                    }
                }
            }
        }
        recyclerView.adapter = adapter
        btnReload.setOnClickListener { viewModel.search() }
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            articleList.observe(viewLifecycleOwner, {
                adapter.setNewInstance(it)
            })
            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            emptyStatus.observe(viewLifecycleOwner, {
                emptyView.isVisible = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapter.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })
            Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
                viewModel.updateListCollectState()
            })
            Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, {
                viewModel.updateItemCollectState(it)
            })
        }
    }

    fun doSearch(searchWords: String) {
        viewModel.search(searchWords)
    }


}