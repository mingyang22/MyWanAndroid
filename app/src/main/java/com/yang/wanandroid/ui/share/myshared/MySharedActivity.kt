package com.yang.wanandroid.ui.share.myshared

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.adapter.ArticleAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import com.yang.wanandroid.ui.share.ShareActivity
import kotlinx.android.synthetic.main.activity_my_shared.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/22/21
 * 我的分享
 */
class MySharedActivity : BaseVmActivity<MySharedViewModel>() {

    private lateinit var adapter: ArticleAdapter

    override fun viewModelClass() = MySharedViewModel::class.java

    override fun getLayoutId() = R.layout.activity_my_shared

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                viewModel.refreshArticleList()
            }
        }
        adapter = ArticleAdapter().apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadMoreArticleList()
            }
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@MySharedActivity)
                    .setMessage(R.string.confirm_delete_shared)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        viewModel.deleteShared(data[position].id)
                        adapter.removeAt(position)
                        this@MySharedActivity.emptyView.isVisible = data.isEmpty()

                    }.show()
                true
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

        btnReload.setOnClickListener {
            viewModel.refreshArticleList()
        }
        ivAdd.setOnClickListener {
            ActivityHelper.start(ShareActivity::class.java)
        }

        ivBack.setOnClickListener {
            ActivityHelper.finish(MySharedActivity::class.java)
        }

        viewModel.refreshArticleList()

    }

    override fun observe() {
        super.observe()
        viewModel.run {
            articleList.observe(this@MySharedActivity, {
                adapter.setNewInstance(it)
            })

            refreshStatus.observe(this@MySharedActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            emptyStatus.observe(this@MySharedActivity, {
                emptyView.isVisible = it
            })
            loadMoreStatus.observe(this@MySharedActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapter.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@MySharedActivity, {
                reloadView.isVisible = it
            })

        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this, {
            viewModel.updateListCollectState()
        })
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this, {
            viewModel.updateItemCollectState(it)
        })
    }

}