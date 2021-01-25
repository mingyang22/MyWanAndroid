package com.yang.wanandroid.ui.main.home.plaza

import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.main.home.HomeRepository

/**
 * @author ym on 1/7/21
 *
 */
class PlazaViewModel : HomeBaseViewModel() {

    private val homeRepository by lazy { HomeRepository() }

    fun refreshArticleList() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val pagination = homeRepository.getUserArticleList(INITIAL_PAGE)
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()

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

                val pagination = homeRepository.getUserArticleList(page)
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