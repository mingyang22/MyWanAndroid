package com.yang.wanandroid.ui.main.navigation

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.model.bean.Navigation

/**
 * @author ym on 1/20/21
 *
 */
class NavigationViewModel : BaseViewModel() {

    private val repository by lazy { NavigationRepository() }

    val navigations = MutableLiveData<MutableList<Navigation>>()

    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getNavigations() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                navigations.value = repository.getNavigations()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = navigations.value.isNullOrEmpty()
            }
        )
    }

}