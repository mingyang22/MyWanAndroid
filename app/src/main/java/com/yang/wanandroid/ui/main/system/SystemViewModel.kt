package com.yang.wanandroid.ui.main.system

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.ui.common.HomeBaseViewModel
import com.yang.wanandroid.ui.main.home.HomeRepository

/**
 * @author ym on 1/18/21
 * 体系
 */
class SystemViewModel : BaseViewModel() {

    private val systemRepository by lazy { SystemRepository() }

    val categories = MutableLiveData<MutableList<Category>>()
    val loadingStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getArticleCategories() {
        launch(
            block = {
                loadingStatus.value = true
                reloadStatus.value = false
                categories.value = systemRepository.getArticleCategories()
                loadingStatus.value = false
            },
            error = {
                loadingStatus.value = false
                reloadStatus.value = true
            }
        )
    }


}