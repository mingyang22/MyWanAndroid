package com.yang.wanandroid.ui.share

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.model.bean.UserInfo
import com.yang.wanandroid.model.store.UserInfoStore

/**
 * @author ym on 1/20/21
 *
 */
class ShareViewModel : BaseViewModel() {

    private val repository by lazy { ShareRepository() }

    val userInfo = MutableLiveData<UserInfo>()
    val submitting = MutableLiveData<Boolean>()
    val shareResult = MutableLiveData<Boolean>()

    fun getUserInfo() {
        userInfo.value = UserInfoStore.getUserInfo()
    }

    fun shareArticle(title: String, link: String) {
        launch(
            block = {
                submitting.value = true
                repository.shareArticle(title, link)
                shareResult.value = true
                submitting.value = false
            },
            error = {
                shareResult.value = false
                submitting.value = false
            }
        )
    }

}