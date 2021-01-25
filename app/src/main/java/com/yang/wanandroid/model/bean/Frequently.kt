package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/20/21
 *
 */
@Keep
data class Frequently(
    val icon: String,
    val id: Int,
    val name: String,
    val link: String,
    val order: Int,
    val visible: Int,
)
