package com.yang.wanandroid.ui.common

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/7/21
 * 文章收藏、取消收藏
 */
class CollectRepository {
    suspend fun collect(id: Int) = RetrofitClient.apiService.collect(id).apiData()
    suspend fun uncollect(id: Int) = RetrofitClient.apiService.uncollect(id).apiData()
}