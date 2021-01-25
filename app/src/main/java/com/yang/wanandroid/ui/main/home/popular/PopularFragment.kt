package com.yang.wanandroid.ui.main.home.popular

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.adapter.ArticleAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * @author ym on 1/6/21
 * 首页 热门 Tab Fragment
 */
class PopularFragment : BaseVmFragment<PopularViewModel>(), ScrollToTop {

    companion object {
        @JvmStatic
        fun newInstance() = PopularFragment()
    }

    private lateinit var adapter: ArticleAdapter

    override fun getLayoutId() = R.layout.fragment_popular

    override fun viewModelClass() = PopularViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshArticleList() }

            adapter = ArticleAdapter(R.layout.item_article).apply {
                loadMoreModule.loadMoreView = CommonLoadMoreView()
                // 设置加载更多监听事件
                loadMoreModule.setOnLoadMoreListener { viewModel.loadMoreArticleList() }
                setOnItemClickListener { _, _, position ->
                    val article = adapter.data[position]
                    ActivityHelper.start(DetailActivity::class.java,
                        mapOf(DetailActivity.PARAM_ARTICLE to article))
                }
                addChildClickViewIds(R.id.iv_collect)
                setOnItemChildClickListener { _, view, position ->
                    val article = adapter.data[position]
                    if (view.id == R.id.iv_collect && checkLogin()) {
                        view.isSelected = !view.isSelected
                        if (article.collect) {
                            viewModel.uncollect(article.id)
                        } else {
                            viewModel.collect(article.id)
                        }
                    }
                }
                animationEnable = true
                setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
            }
            recyclerView.adapter = adapter
            btnReload.setOnClickListener { viewModel.refreshArticleList() }

        }
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            // 监听数据
            articleList.observe(viewLifecycleOwner, {
                adapter.setList(it)
            })
            // 监听刷新状态
            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            // 监听加载更多状态
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapter.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    else -> return@Observer
                }
            })
            // 监听重新加载状态
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })
        }

        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            viewModel.updateListCollectState()
        })
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, {
            viewModel.updateItemCollectState(it)
        })
    }

    override fun lazyLoadData() {
        viewModel.refreshArticleList()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }

}