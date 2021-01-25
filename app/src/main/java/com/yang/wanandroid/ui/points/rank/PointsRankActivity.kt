package com.yang.wanandroid.ui.points.rank

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.adapter.PointsRankAdapter
import kotlinx.android.synthetic.main.activity_points_rank.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/21/21
 * 积分排行
 */
class PointsRankActivity : BaseVmActivity<PointsRankViewModel>() {

    private lateinit var adapter: PointsRankAdapter

    override fun viewModelClass() = PointsRankViewModel::class.java

    override fun getLayoutId() = R.layout.activity_points_rank

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                viewModel.refreshData()
            }
        }
        adapter = PointsRankAdapter().apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadMoreData()
            }
        }
        recyclerView.adapter = adapter

        ivBack.setOnClickListener { ActivityHelper.finish(PointsRankActivity::class.java) }
        tvTitle.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
        btnReload.setOnClickListener {
            viewModel.refreshData()
        }
        viewModel.refreshData()

    }

    override fun observe() {
        super.observe()
        viewModel.run {
            pointsRank.observe(this@PointsRankActivity, {
                adapter.setNewInstance(it)
            })

            refreshStatus.observe(this@PointsRankActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@PointsRankActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapter.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@PointsRankActivity, {
                reloadView.isVisible = it
            })

        }
    }

}