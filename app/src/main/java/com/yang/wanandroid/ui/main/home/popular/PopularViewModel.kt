package com.yang.wanandroid.ui.main.home.popular

import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.main.home.HomeRepository
import com.yang.wanandroid.ui.common.HomeBaseViewModel

/**
 * @author ym on 1/7/21
 * 热门
 */
class PopularViewModel : HomeBaseViewModel() {

    private val homeRepository by lazy { HomeRepository() }

    fun refreshArticleList() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val topListDeferred = async {
                    homeRepository.getTopArticleList()
                }
                val paginationDeferred = async {
                    homeRepository.getArticleList(INITIAL_PAGE)
                }
                val topList = topListDeferred.await().onEach { it.top = true }
                val pagination = paginationDeferred.await()
                page = pagination.curPage
                articleList.value = (topList + pagination.datas).toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )

    }

    fun loadMoreArticleList() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = homeRepository.getArticleList(page)
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