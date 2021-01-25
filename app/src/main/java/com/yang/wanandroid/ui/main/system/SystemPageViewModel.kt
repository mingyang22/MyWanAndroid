package com.yang.wanandroid.ui.main.system

import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import kotlinx.coroutines.Job

/**
 * @author ym on 1/18/21
 * 体系
 */
class SystemPageViewModel : HomeBaseViewModel() {

    private val systemRepository by lazy { SystemRepository() }

    private var id = -1
    private var refreshJob: Job? = null

    fun refreshArticleList(cid: Int) {
        if (cid != id) {
            cancelJob(refreshJob)
            id = cid
            articleList.value = mutableListOf()
        }
        refreshJob = launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val pagination = systemRepository.getArticleListByCid(INITIAL_PAGE, cid)
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = articleList.value?.isEmpty()
            }
        )

    }

    fun loadMoreArticleList(cid: Int) {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = systemRepository.getArticleListByCid(page, cid)
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