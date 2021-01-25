package com.yang.wanandroid.ui.common

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.store.UserInfoStore
import com.yang.wanandroid.util.isLogin

/**
 * @author ym on 1/8/21
 * 首页父类viewModel
 */
open class HomeBaseViewModel : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 0
    }

    val collectRepository by lazy { CollectRepository() }

    protected var page = INITIAL_PAGE

    // 文章列表
    val articleList = MutableLiveData<MutableList<Article>>()

    // 加载更多状态
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()

    // 刷新状态
    val refreshStatus = MutableLiveData<Boolean>()

    // 重新加载状态
    val reloadStatus = MutableLiveData<Boolean>()

    fun collect(id: Int) {
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                updateItemCollectState(id to true)
                Bus.post(USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateItemCollectState(id to false)
            }
        )
    }

    open fun uncollect(id: Int) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                updateItemCollectState(id to false)
                Bus.post(USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateItemCollectState(id to true)
            }
        )

    }

    /**
     * 更新当前列表的收藏状态
     */
    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (isLogin()) {
            val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        articleList.value = list

    }

    /**
     * 更新Item的收藏状态
     * @param target Pair<Int, Boolean>
     */
    fun updateItemCollectState(target: Pair<Int, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list

    }

}