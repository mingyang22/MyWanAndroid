package com.yang.wanandroid.ui.search.history

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.model.bean.HotWord
import com.yang.wanandroid.ui.search.SearchRepository

/**
 * @author ym on 1/13/21
 *
 */
class SearchHistoryViewModel : BaseViewModel() {

    private val searchRepository by lazy { SearchRepository() }

    val hotWords = MutableLiveData<List<HotWord>>()
    val searchHistory = MutableLiveData<MutableList<String>>()

    fun getHotSearch() {
        launch(
            block = {
                hotWords.value = searchRepository.getHotSearch()
            }
        )
    }

    fun getSearchHistory() {
        searchHistory.value = searchRepository.getSearchHistory()
    }

    fun addSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
        }
        history.add(0, searchWords)
        searchHistory.value = history
        searchRepository.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
            searchHistory.value = history
            searchRepository.deleteSearchHistory(searchWords)
        }
    }

}