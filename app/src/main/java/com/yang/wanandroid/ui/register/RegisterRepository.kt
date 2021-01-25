package com.yang.wanandroid.ui.register

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/15/21
 *
 */
class RegisterRepository {
    suspend fun register(username: String, password: String, confirmPassword: String) =
        RetrofitClient.apiService.register(username, password, confirmPassword).apiData()
}