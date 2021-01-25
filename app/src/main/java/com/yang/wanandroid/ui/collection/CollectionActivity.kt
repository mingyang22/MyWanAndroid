package com.yang.wanandroid.ui.collection

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.adapter.ArticleAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/22/21
 * 我的收藏
 */
class CollectionActivity : BaseVmActivity<CollectionViewModel>() {

    private lateinit var adapter: ArticleAdapter

    override fun viewModelClass() = CollectionViewModel::class.java

    override fun getLayoutId() = R.layout.activity_collection

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
        adapter = ArticleAdapter().apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadMore()
            }
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            addChildClickViewIds(R.id.iv_collect)
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect) {
                    viewModel.uncollect(article.originId)
                    removeItem(position)
                }
            }
        }
        recyclerView.adapter = adapter

        btnReload.setOnClickListener {
            viewModel.refresh()
        }
        ivBack.setOnClickListener {
            ActivityHelper.finish(CollectionActivity::class.java)
        }

        viewModel.refresh()
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            articleList.observe(this@CollectionActivity, {
                adapter.setNewInstance(it)
            })

            refreshStatus.observe(this@CollectionActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            emptyStatus.observe(this@CollectionActivity, {
                emptyView.isVisible = it
            })
            loadMoreStatus.observe(this@CollectionActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapter.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@CollectionActivity, {
                reloadView.isVisible = it
            })
        }
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this, { (id, collect) ->
            if (collect) {
                viewModel.refresh()
            } else {
                val position = adapter.data.indexOfFirst { it.originId == id }
                if (position != -1) removeItem(position)
            }

        })
    }

    private fun removeItem(position: Int) {
        adapter.removeAt(position)
        emptyView.isVisible = adapter.data.isEmpty()
    }

}