package com.yang.wanandroid.ui.main.system

import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.dialog.SystemCategoryDialogFragment
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.ui.adapter.SimpleFragmentPagerAdapter
import com.yang.wanandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_system.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * @author ym on 1/6/21
 * 体系
 */
class SystemFragment : BaseVmFragment<SystemViewModel>(), ScrollToTop {

    companion object {
        @JvmStatic
        fun newInstance() = SystemFragment()
    }

    private var currentOffset = 0
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<SystemPageFragment>()
    private var filterDialog: SystemCategoryDialogFragment? = null

    override fun getLayoutId() = R.layout.fragment_system

    override fun viewModelClass() = SystemViewModel::class.java

    override fun initView() {
        reloadView.btnReload.setOnClickListener {
            viewModel.getArticleCategories()
        }
        ivFilter.setOnClickListener {
            filterDialog?.show(childFragmentManager)
        }
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (activity is MainActivity && this.currentOffset != verticalOffset) {
                (activity as MainActivity).animateBottomNavigationView(verticalOffset > currentOffset)
                currentOffset = verticalOffset
            }
        })
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            categories.observe(viewLifecycleOwner, { categories ->
                ivFilter.visibility = View.VISIBLE
                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                // 过滤空分类
                val newCategories = categories.filter {
                    it.children.isNotEmpty()
                }.toMutableList()
                setupViewPage(newCategories)
                filterDialog = SystemCategoryDialogFragment.newInstance(ArrayList(newCategories))

            })
            loadingStatus.observe(viewLifecycleOwner, {
                progressBar.isVisible = it
            })
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })

        }
    }

    /**
     * 设置数据
     * @param categories 体系分类集合
     */
    private fun setupViewPage(categories: MutableList<Category>) {
        titles.clear()
        fragments.clear()
        categories.forEach {
            titles.add(it.name)
            fragments.add(SystemPageFragment.newInstance(it.children))
        }
        viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
        viewPager.offscreenPageLimit = titles.size
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun lazyLoadData() {
        viewModel.getArticleCategories()
    }

    override fun scrollToTop() {
        if (fragments.isEmpty() || viewPager == null) return
        fragments[viewPager.currentItem].scrollToTop()
    }

    /**
     * 获取当前所在主分类和子分类
     * @return Pair<Int,Int>
     */
    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty() || viewPager == null) return 0 to 0
        val first = viewPager.currentItem
        val second = fragments[first].checkedPosition
        return first to second
    }

    /**
     * 选中分类后切换分类
     * @param position Pair<Int, Int> 主分类、子分类
     */
    fun check(position: Pair<Int, Int>) {
        if (fragments.isEmpty() || viewPager == null) return
        viewPager.currentItem = position.first
        fragments[position.first].check(position.second)

    }

}