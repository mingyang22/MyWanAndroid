package com.yang.wanandroid.ui.main.system

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseFragment
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ext.dpToPx
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.ui.adapter.CategoryAdapter
import com.yang.wanandroid.ui.adapter.SimpleArticleAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_system_pager.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/18/21
 * 体系
 */
class SystemPageFragment : BaseVmFragment<SystemPageViewModel>(), ScrollToTop {

    companion object {

        private const val CATEGORY_LIST = "category_list"

        @JvmStatic
        fun newInstance(categoryList: ArrayList<Category>): SystemPageFragment {
            return SystemPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    private lateinit var categoryList: List<Category>
    var checkedPosition = 0
    private lateinit var adapterArticle: SimpleArticleAdapter
    private lateinit var adapterCategory: CategoryAdapter

    override fun getLayoutId() = R.layout.fragment_system_pager

    override fun viewModelClass() = SystemPageViewModel::class.java

    override fun initData() {
        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0
    }

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                viewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }

        adapterCategory = CategoryAdapter(R.layout.item_category_sub).apply {
            setNewInstance(categoryList.toMutableList())
            rvCategory.adapter = this
            onCheckedListener = {
                checkedPosition = it
                viewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }

        adapterArticle = SimpleArticleAdapter(R.layout.item_article_simple).apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadMoreArticleList(categoryList[checkedPosition].id)
            }
            setOnItemClickListener { _, _, position ->
                val article = adapterArticle.data[position]
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            addChildClickViewIds(R.id.iv_collect)
            setOnItemChildClickListener { _, view, position ->
                val article = adapterArticle.data[position]
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
        recyclerView.adapter = adapterArticle

        btnReload.setOnClickListener {
            viewModel.refreshArticleList(categoryList[checkedPosition].id)
        }

    }

    override fun observe() {
        super.observe()
        viewModel.run {
            articleList.observe(viewLifecycleOwner, {
                adapterArticle.setNewInstance(it)
            })
            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapterArticle.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapterArticle.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapterArticle.loadMoreModule.loadMoreEnd()
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
        viewModel.refreshArticleList(categoryList[checkedPosition].id)
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }

    /**
     * 选中子分类
     * @param position Int
     */
    fun check(position: Int) {
        if (position != checkedPosition) {
            checkedPosition = position
            adapterCategory.check(position)
            (rvCategory.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position,
                8.dpToPx().toInt())
            viewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }

}