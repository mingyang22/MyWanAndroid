package com.yang.wanandroid.ui.register

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.model.store.UserInfoStore

/**
 * @author ym on 1/15/21
 *
 */
class RegisterViewModel : BaseViewModel() {

    private val registerRepository by lazy { RegisterRepository() }
    val submitting = MutableLiveData<Boolean>()
    val registerResult = MutableLiveData<Boolean>()

    fun register(account: String, password: String, confirmPassword: String) {
        launch(
            block = {
                submitting.value = true
                val userInfo = registerRepository.register(account, password, confirmPassword)
                UserInfoStore.setUserInfo(userInfo)
                Bus.post(USER_LOGIN_STATE_CHANGED, true)
                registerResult.value = true
                submitting.value = false
            },
            error = {
                registerResult.value = false
                submitting.value = false
            }
        )

    }
}