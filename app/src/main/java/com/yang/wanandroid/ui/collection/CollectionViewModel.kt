package com.yang.wanandroid.ui.collection

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.UserInfo
import com.yang.wanandroid.model.store.UserInfoStore
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.share.ShareRepository

/**
 * @author ym on 1/22/21
 * 我的收藏
 */
class CollectionViewModel : HomeBaseViewModel() {

    private val repository by lazy { CollectionRepository() }

    val emptyStatus = MutableLiveData<Boolean>()

    fun refresh() {
        launch(
            block = {
                refreshStatus.value = true
                emptyStatus.value = false
                reloadStatus.value = false

                val pagination = repository.getCollectionList(INITIAL_PAGE)
                pagination.datas.forEach { it.collect = true }
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                emptyStatus.value = pagination.datas.isEmpty()
                refreshStatus.value = false
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
                val pagination = repository.getCollectionList(page)
                pagination.datas.forEach { it.collect = true }
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

    override fun uncollect(id: Int) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                Bus.post(USER_COLLECT_UPDATED, id to false)
            }
        )

    }


}