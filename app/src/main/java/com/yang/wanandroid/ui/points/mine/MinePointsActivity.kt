package com.yang.wanandroid.ui.points.mine

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.loadmore.CommonLoadMoreView
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.adapter.MinePointsAdapter
import com.yang.wanandroid.ui.points.rank.PointsRankActivity
import kotlinx.android.synthetic.main.activity_mine_points.*
import kotlinx.android.synthetic.main.header_mine_points.view.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/20/21
 * 我的积分
 */
class MinePointsActivity : BaseVmActivity<MinePointsViewModel>() {

    private lateinit var adapter: MinePointsAdapter
    private lateinit var headerView: View

    override fun viewModelClass() = MinePointsViewModel::class.java

    override fun getLayoutId() = R.layout.activity_mine_points

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
        headerView = LayoutInflater.from(this).inflate(R.layout.header_mine_points, null)
        adapter = MinePointsAdapter().apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadMoreRecord()
            }
        }
        recyclerView.adapter = adapter
        ivBack.setOnClickListener { ActivityHelper.finish(MinePointsActivity::class.java) }
        ivRank.setOnClickListener {
            ActivityHelper.start(PointsRankActivity::class.java)
        }
        btnReload.setOnClickListener { viewModel.refresh() }
        viewModel.refresh()

    }

    override fun observe() {
        super.observe()
        viewModel.run {
            totalPoints.observe(this@MinePointsActivity, {
                if (adapter.headerLayoutCount == 0) {
                    adapter.setHeaderView(headerView)
                }
                headerView.tvTotalPoints.text = it.coinCount.toString()
                headerView.tvLevelRank.text = getString(R.string.level_rank, it.level, it.rank)
            })
            pointsList.observe(this@MinePointsActivity, {
                adapter.setNewInstance(it)
            })

            refreshStatus.observe(this@MinePointsActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@MinePointsActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> adapter.loadMoreModule.loadMoreComplete()
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@MinePointsActivity, {
                reloadView.isVisible = it
            })

        }
    }


}