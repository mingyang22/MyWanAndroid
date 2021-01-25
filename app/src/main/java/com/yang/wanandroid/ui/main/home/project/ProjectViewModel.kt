package com.yang.wanandroid.ui.main.home.project

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.main.home.HomeRepository

/**
 * @author ym on 1/7/21
 * 项目
 */
class ProjectViewModel : HomeBaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 1
        const val INITIAL_CHECKED = 0
    }

    private val homeRepository by lazy { HomeRepository() }

    // 类别
    val categories = MutableLiveData<MutableList<Category>>()

    // 选中的类别
    val checkedCategory = MutableLiveData<Int>()

    // 类别重新加载状态
    val reloadListStatus = MutableLiveData<Boolean>()


    private var pages = INITIAL_PAGE + 1

    /**
     * 获取类别
     */
    fun getProjectCategory() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val categoryList = homeRepository.getProjectCategories()
                val checkedPosition = INITIAL_CHECKED
                val cid = categoryList[checkedPosition].id
                val pagination = homeRepository.getProjectListByCid(INITIAL_PAGE, cid)
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

    /**
     * 根据类别获取文章数据
     */
    fun refreshProjectList(checkedPosition: Int = checkedCategory.value ?: INITIAL_CHECKED) {
        launch(
            block = {
                refreshStatus.value = true
                reloadListStatus.value = false

                if (checkedPosition != checkedCategory.value) {
                    articleList.value = mutableListOf()
                    checkedCategory.value = checkedPosition
                }

                val categoryList = categories.value ?: return@launch
                val cid = categoryList[checkedPosition].id
                val pagination = homeRepository.getProjectListByCid(INITIAL_PAGE, cid)
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

    fun loadMoreProjectList() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING

                val categoryList = categories.value ?: return@launch
                val checkedPosition = checkedCategory.value ?: return@launch
                val cid = categoryList[checkedPosition].id
                val pagination = homeRepository.getProjectListByCid(pages + 1, cid)
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