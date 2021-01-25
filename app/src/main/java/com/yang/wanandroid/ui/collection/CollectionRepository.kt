package com.yang.wanandroid.ui.collection

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/22/21
 *
 */
class CollectionRepository {
    /**
     * 获取收藏列表
     * @param page Int
     * @return Pagination<Article>
     */
    suspend fun getCollectionList(page: Int) =
        RetrofitClient.apiService.getCollectionList(page).apiData()
}