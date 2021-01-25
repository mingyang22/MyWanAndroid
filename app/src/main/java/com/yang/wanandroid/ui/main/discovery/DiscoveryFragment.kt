package com.yang.wanandroid.ui.main.discovery

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.ui.adapter.BannerAdapter
import com.yang.wanandroid.ui.adapter.DiscoveryTagAdapter
import com.yang.wanandroid.ui.adapter.HotWordsAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import com.yang.wanandroid.ui.main.MainActivity
import com.yang.wanandroid.ui.search.SearchActivity
import com.yang.wanandroid.ui.share.ShareActivity
import com.youth.banner.transformer.MZScaleInTransformer
import kotlinx.android.synthetic.main.fragment_discovery.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * @author ym on 1/6/21
 * 发现
 */
class DiscoveryFragment : BaseVmFragment<DiscoveryViewModel>(), ScrollToTop {

    companion object {
        @JvmStatic
        fun newInstance() = DiscoveryFragment()
    }

    private lateinit var adapter: HotWordsAdapter

    override fun getLayoutId() = R.layout.fragment_discovery

    override fun viewModelClass() = DiscoveryViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.getData() }
        }
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if ((activity is MainActivity && scrollY != oldScrollY)) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
        }
        ivAdd.setOnClickListener {
            ActivityHelper.start(ShareActivity::class.java)
        }
        ivSearch.setOnClickListener {
            ActivityHelper.start(SearchActivity::class.java)
        }
        bannerView.run {
            setPageTransformer(MZScaleInTransformer())
            isAutoLoop(true)
            setLoopTime(5000)
            scrollTime = 1000
            addBannerLifecycleObserver(this@DiscoveryFragment)
        }
        tagFlowLayout.run {
            setOnTagClickListener { _, position, _ ->
                val frequently = viewModel.frequentlyList.value?.get(position)
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to Article(title = frequently?.name,
                        link = frequently?.link)))
                false
            }
        }
        btnReload.setOnClickListener { viewModel.getData() }
        adapter = HotWordsAdapter().apply {
            setOnItemClickListener { _, _, position ->
                ActivityHelper.start(SearchActivity::class.java,
                    mapOf(SearchActivity.SEARCH_WORDS to data[position].name))
            }
        }
        rvHotWord.adapter = adapter

    }

    override fun lazyLoadData() {
        viewModel.getData()
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            banners.observe(viewLifecycleOwner, {
                bannerView.adapter = BannerAdapter(it)
            })
            hotWords.observe(viewLifecycleOwner, {
                adapter.setNewInstance(it)
                tvHotWordTitle.isVisible = it.isNotEmpty()
            })
            frequentlyList.observe(viewLifecycleOwner, {
                tagFlowLayout.adapter = DiscoveryTagAdapter(it)
                tvFrequently.isGone = it.isEmpty()
            })

            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })
        }

    }

    override fun scrollToTop() {
        nestedScrollView.smoothScrollTo(0, 0)
    }

}