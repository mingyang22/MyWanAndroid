package com.yang.wanandroid.ui.main.home.project

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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
import com.yang.wanandroid.ui.adapter.CategoryAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * @author ym on 1/6/21
 * 首页 项目 Tab Fragment
 */
class ProjectFragment : BaseVmFragment<ProjectViewModel>(), ScrollToTop {

    companion object {
        @JvmStatic
        fun newInstance() = ProjectFragment()
    }

    private lateinit var adapter: ArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun getLayoutId() = R.layout.fragment_project

    override fun viewModelClass() = ProjectViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshProjectList() }
        }
        categoryAdapter = CategoryAdapter(R.layout.item_category_sub).apply {
            onCheckedListener = {
                viewModel.refreshProjectList(it)
            }
        }
        rvCategory.adapter = categoryAdapter

        adapter = ArticleAdapter(R.layout.item_article).apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadMoreProjectList()
            }
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
        }
        recyclerView.adapter = adapter

        reloadListView.btnReload.setOnClickListener {
            viewModel.refreshProjectList()
        }
        reloadView.btnReload.setOnClickListener {
            viewModel.getProjectCategory()
        }

    }

    override fun observe() {
        super.observe()
        viewModel.run {
            categories.observe(viewLifecycleOwner, {
                rvCategory.isGone = it.isEmpty()
                categoryAdapter.setNewInstance(it)
            })
            checkedCategory.observe(viewLifecycleOwner, {
                categoryAdapter.check(it)
            })
            articleList.observe(viewLifecycleOwner, {
                adapter.setNewInstance(it)
            })
            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
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
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            viewModel.updateListCollectState()
        })
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, {
            viewModel.updateItemCollectState(it)
        })

    }

    override fun lazyLoadData() {
        viewModel.getProjectCategory()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }

}