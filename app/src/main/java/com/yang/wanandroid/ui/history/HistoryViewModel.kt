package com.yang.wanandroid.ui.history

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.store.UserInfoStore
import com.yang.wanandroid.ui.common.HomeBaseViewModel

/**
 * @author ym on 1/25/21
 *
 */
class HistoryViewModel : HomeBaseViewModel() {

    private val repository by lazy { HistoryRepository() }

    val emptyStatus = MutableLiveData<Boolean>()

    fun getData() {
        launch(
            block = {
                emptyStatus.value = false

                val readHistory = repository.getReadHistory()
                val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: emptyList()
                readHistory.forEach { it.collect = collectIds.contains(it.id) }

                articleList.value = readHistory.toMutableList()
                emptyStatus.value = readHistory.isEmpty()
            }
        )
    }

    fun deleteHistory(article: Article) {
        launch(
            block = {
                repository.deleteHistory(article)
            }
        )

    }

}