package com.yang.wanandroid.ui.setting

import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.model.api.RetrofitClient
import com.yang.wanandroid.model.store.UserInfoStore

/**
 * @author ym on 1/25/21
 *
 */
class SettingViewModel : BaseViewModel() {
    fun logout() {
        UserInfoStore.clearUserInfo()
        RetrofitClient.clearCoolie()
        Bus.post(USER_LOGIN_STATE_CHANGED, false)
    }
}