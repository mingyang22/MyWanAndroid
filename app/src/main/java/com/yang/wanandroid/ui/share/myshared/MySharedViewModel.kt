package com.yang.wanandroid.ui.share.myshared

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.share.ShareRepository

/**
 * @author ym on 1/22/21
 * 我的分享
 */
class MySharedViewModel : HomeBaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 1
    }

    private val repository by lazy { ShareRepository() }

    val emptyStatus = MutableLiveData<Boolean>()

    private var pages = INITIAL_PAGE

    fun refreshArticleList() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val pagination = repository.getSharedArticleList(INITIAL_PAGE).shareArticles
                pages = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                emptyStatus.value = pagination.datas.isEmpty()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = pages == INITIAL_PAGE
            }
        )
    }

    fun loadMoreArticleList() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = repository.getSharedArticleList(pages + 1).shareArticles
                pages = pagination.curPage

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

    fun deleteShared(id: Int) {
        launch(
            block = {
                repository.deleteShared(id)
            }
        )
    }

}