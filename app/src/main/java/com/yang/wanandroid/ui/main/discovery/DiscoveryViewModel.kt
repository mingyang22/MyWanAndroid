package com.yang.wanandroid.ui.main.discovery

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.Banner
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.model.bean.Frequently
import com.yang.wanandroid.model.bean.HotWord
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.main.home.HomeRepository

/**
 * @author ym on 1/18/21
 * 发现
 */
class DiscoveryViewModel : BaseViewModel() {

    private val repository by lazy { DiscoveryRepository() }

    val banners = MutableLiveData<MutableList<Banner>>()

    // 热门搜索
    val hotWords = MutableLiveData<MutableList<HotWord>>()

    // 常用网址
    val frequentlyList = MutableLiveData<MutableList<Frequently>>()

    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getData() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                banners.value = repository.getBanners()
                hotWords.value = repository.getHotWords()
                frequentlyList.value = repository.getFrequentlyWebsites()

                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value =
                    banners.value.isNullOrEmpty() && hotWords.value.isNullOrEmpty() && frequentlyList.value.isNullOrEmpty()
            }
        )
    }


}