package com.yang.wanandroid.model.store

import com.google.gson.Gson
import com.yang.wanandroid.App
import com.yang.wanandroid.model.bean.UserInfo
import com.yang.wanandroid.util.clearSpValue
import com.yang.wanandroid.util.getSpValue
import com.yang.wanandroid.util.putSpValue

/**
 * @author ym on 1/12/21
 * 用户信息存储
 */
object UserInfoStore {

    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"
    private val gson by lazy { Gson() }

    fun getUserInfo(): UserInfo? {
        val userInfoStr = getSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, "")
        return if (userInfoStr.isNotEmpty()) {
            gson.fromJson(userInfoStr, UserInfo::class.java)
        } else {
            null
        }
    }

    fun setUserInfo(userInfo: UserInfo) =
        putSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, gson.toJson(userInfo))

    fun clearUserInfo() {
        clearSpValue(SP_USER_INFO, App.instance)
    }

    fun removeCollectId(collectId: Int) {
        getUserInfo()?.let {
            if (collectId in it.collectIds) {
                it.collectIds.remove(collectId)
                setUserInfo(it)
            }
        }
    }

    fun addCollectId(collectId: Int) {
        getUserInfo()?.let {
            if (collectId !in it.collectIds) {
                it.collectIds.add(collectId)
                setUserInfo(it)
            }
        }
    }

}