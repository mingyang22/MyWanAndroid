package com.yang.wanandroid.ui.main.navigation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseFragment
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.ui.adapter.NavigationAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import com.yang.wanandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_navigation.*

/**
 * @author ym on 1/6/21
 * 导航
 */
class NavigationFragment : BaseVmFragment<NavigationViewModel>(), ScrollToTop {

    private lateinit var adapter: NavigationAdapter
    private var currentPosition = 0

    companion object {
        const val TAG = "NavigationFragment"

        @JvmStatic
        fun newInstance() = NavigationFragment()
    }

    override fun getLayoutId() = R.layout.fragment_navigation

    override fun viewModelClass() = NavigationViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                viewModel.getNavigations()
            }
        }

        adapter = NavigationAdapter().apply {
            onItemTagClickListener = {
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to Article(title = it.title,
                        link = it.link)))
            }
        }
        recyclerView.adapter = adapter
        /**
         * scrollY 一直为0 ，oldScrollY 上滑为负 下滑为正 数值和速度相关
         */
        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
//            Log.e(TAG, "scrollY: $scrollY , oldScrollY: $oldScrollY")
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            // 下滑
            if (scrollY < oldScrollY) {
                tvFloatTitle.text = adapter.data[currentPosition].name
            }
            val lm = recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                Log.e(
                    TAG,
                    "nextView.top: ${nextView.top} --> ${tvFloatTitle.measuredHeight}",
                )
                tvFloatTitle.translationY = if (nextView.top < tvFloatTitle.measuredHeight) {
                    (nextView.top - tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
                Log.e(TAG, "getY: ${tvFloatTitle.y} , getTop: ${tvFloatTitle.top}")
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            // 上滑
            if (scrollY > oldScrollY) {
                tvFloatTitle.text = adapter.data[currentPosition].name
            }
        }
        reloadView.setOnClickListener { viewModel.getNavigations() }


    }

    override fun observe() {
        super.observe()
        viewModel.run {
            navigations.observe(viewLifecycleOwner, {
                tvFloatTitle.isGone = it.isEmpty()
                tvFloatTitle.text = it[0].name
                adapter.setNewInstance(it)
            })

            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })

        }
    }

    override fun lazyLoadData() {
        viewModel.getNavigations()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }

}