package com.yang.wanandroid.ui.login

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/12/21
 *
 */
class LoginRepository {
    suspend fun login(username: String, password: String) =
        RetrofitClient.apiService.login(username, password).apiData()
}