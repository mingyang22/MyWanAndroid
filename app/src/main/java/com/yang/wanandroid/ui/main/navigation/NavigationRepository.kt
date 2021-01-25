package com.yang.wanandroid.ui.main.navigation

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/20/21
 *
 */
class NavigationRepository {
    suspend fun getNavigations() = RetrofitClient.apiService.getNavigations().apiData()
}