package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/21/21
 *
 */
@Keep
data class PointRank(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String,
)
