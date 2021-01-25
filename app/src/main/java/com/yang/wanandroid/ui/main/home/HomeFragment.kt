package com.yang.wanandroid.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseFragment
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ui.adapter.SimpleFragmentPagerAdapter
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.ui.main.MainActivity
import com.yang.wanandroid.ui.main.home.latest.LatestFragment
import com.yang.wanandroid.ui.main.home.plaza.PlazaFragment
import com.yang.wanandroid.ui.main.home.popular.PopularFragment
import com.yang.wanandroid.ui.main.home.project.ProjectFragment
import com.yang.wanandroid.ui.main.home.wechat.WechatFragment
import com.yang.wanandroid.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author ym on 1/6/21
 * 首页 Fragment
 * 热门 最新 广场 项目 公众号
 */
class HomeFragment : BaseFragment(), ScrollToTop {

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragments = listOf(
            PopularFragment.newInstance(),
            LatestFragment.newInstance(),
            PlazaFragment.newInstance(),
            ProjectFragment.newInstance(),
            WechatFragment.newInstance()
        )

        val titles = listOf(
            getString(R.string.popular_articles),
            getString(R.string.latest_project),
            getString(R.string.plaza),
            getString(R.string.project_category),
            getString(R.string.wechat_public)
        )

        viewPager.adapter = SimpleFragmentPagerAdapter(
            fm = childFragmentManager,
            fragments = fragments,
            titles = titles
        )
        viewPager.offscreenPageLimit = fragments.size
        tabLayout.setupWithViewPager(viewPager)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (activity is MainActivity && this.currentOffset != verticalOffset) {
                (activity as MainActivity).animateBottomNavigationView(verticalOffset > currentOffset)
                currentOffset = verticalOffset
            }
        })

        llSearch.setOnClickListener { ActivityHelper.start(SearchActivity::class.java) }

    }

    override fun scrollToTop() {
        if (!this::fragments.isInitialized) return
        val currentFragment = fragments[viewPager.currentItem]
        if (currentFragment is ScrollToTop && currentFragment.isVisible) {
            currentFragment.scrollToTop()
        }
    }
}