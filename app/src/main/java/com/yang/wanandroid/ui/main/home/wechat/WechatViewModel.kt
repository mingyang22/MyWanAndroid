package com.yang.wanandroid.ui.main.home.wechat

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.main.home.HomeRepository

/**
 * @author ym on 1/7/21
 * 公众号
 */
class WechatViewModel : HomeBaseViewModel() {

    companion object {
        const val INITIAL_CHECKED = 0
        const val INITIAL_PAGE = 1
    }

    private val homeRepository by lazy { HomeRepository() }

    val categories = MutableLiveData<MutableList<Category>>()
    val checkedCategory = MutableLiveData<Int>()
    val reloadListStatus = MutableLiveData<Boolean>()

    private var pages = INITIAL_PAGE

    fun getWechatCategory() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val categoryList = homeRepository.getWechatCategories()
                val checkedPosition = INITIAL_CHECKED
                val id = categoryList[checkedPosition].id
                val pagination = homeRepository.getWechatArticleList(INITIAL_PAGE, id)
                pages = pagination.curPage

                categories.value = categoryList
                checkedCategory.value = checkedPosition
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = true
            }
        )
    }

    fun refreshWechatArticleList(checkedPosition: Int = checkedCategory.value ?: INITIAL_CHECKED) {
        launch(
            block = {
                refreshStatus.value = true
                reloadListStatus.value = false

                if (checkedPosition != checkedCategory.value) {
                    articleList.value = mutableListOf()
                    checkedCategory.value = checkedPosition
                }

                val categoryList = categories.value ?: return@launch
                val id = categoryList[checkedPosition].id
                val pagination = homeRepository.getWechatArticleList(INITIAL_PAGE, id)
                pages = pagination.curPage

                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadListStatus.value = articleList.value?.isEmpty()
            }
        )
    }

    fun loadMoreWechatArticleList() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING

                val categoryList = categories.value ?: return@launch
                val checkedPosition = checkedCategory.value ?: return@launch
                val id = categoryList[checkedPosition].id
                val pagination = homeRepository.getWechatArticleList(page + 1, id)
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

}