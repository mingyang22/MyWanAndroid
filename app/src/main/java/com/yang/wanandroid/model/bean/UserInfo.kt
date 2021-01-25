package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/12/21
 *
 */
@Keep
data class UserInfo(
    val admin: Boolean,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String,
    val collectIds: MutableList<Int>,
    val chapterTops: List<Any>,
)
