package com.yang.wanandroid.ui.search

import com.yang.wanandroid.model.api.RetrofitClient
import com.yang.wanandroid.model.store.SearchHistoryStore

/**
 * @author ym on 1/13/21
 *
 */
class SearchRepository {

    /**
     * 获取热门搜索
     */
    suspend fun getHotSearch() = RetrofitClient.apiService.getHotWords().apiData()

    fun saveSearchHistory(searchWords: String) {
        SearchHistoryStore.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        SearchHistoryStore.deleteSearchHistory(searchWords)
    }

    fun getSearchHistory() = SearchHistoryStore.getSearchHistory()

    /**
     * 搜索
     * @param keywords String
     * @param page Int
     */
    suspend fun search(keywords: String, page: Int) =
        RetrofitClient.apiService.search(keywords, page).apiData()

}