package com.yang.wanandroid.ui.search.result

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.search.SearchRepository

/**
 * @author ym on 1/13/21
 *
 */
class SearchResultViewModel : HomeBaseViewModel() {

    private val searchRepository by lazy { SearchRepository() }


    val emptyStatus = MutableLiveData<Boolean>()
    private var currentKeywords = ""

    fun search(keywords: String = currentKeywords) {
        launch(
            block = {
                if (currentKeywords != keywords) {
                    currentKeywords = keywords
                    articleList.value = emptyList<Article>().toMutableList()
                }

                refreshStatus.value = true
                emptyStatus.value = false
                reloadStatus.value = false

                val pagination = searchRepository.search(keywords, INITIAL_PAGE)
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
                emptyStatus.value = pagination.datas.isEmpty()
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMore() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = searchRepository.search(currentKeywords, page)
                page = pagination.curPage
                val currentList = articleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)
                articleList.value = currentList
                loadMoreStatus.value =
                    if (pagination.offset >= pagination.total) LoadMoreStatus.END else LoadMoreStatus.COMPLETED
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )
    }

}