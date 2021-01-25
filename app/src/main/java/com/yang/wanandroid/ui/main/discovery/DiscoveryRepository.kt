package com.yang.wanandroid.ui.main.discovery

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/20/21
 * 发现
 */
class DiscoveryRepository {
    /**
     * 获取 banner
     * @return List<Banner>
     */
    suspend fun getBanners() = RetrofitClient.apiService.getBanners().apiData()

    /**
     * 获取热门搜索
     * @return List<HotWord>
     */
    suspend fun getHotWords() = RetrofitClient.apiService.getHotWords().apiData()

    /**
     * 获取常用网址
     * @return List<Frequently>
     */
    suspend fun getFrequentlyWebsites() =
        RetrofitClient.apiService.getFrequentlyWebsites().apiData()
}