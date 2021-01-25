package com.yang.wanandroid.util

import com.yang.wanandroid.model.api.RetrofitClient
import com.yang.wanandroid.model.store.UserInfoStore

/**
 * @author ym on 1/12/21
 * 登录状态
 */
fun isLogin() = UserInfoStore.getUserInfo() != null && RetrofitClient.hasCookie()