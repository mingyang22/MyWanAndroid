package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/21/21
 *
 */
@Keep
data class PointRecord(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String,
)
