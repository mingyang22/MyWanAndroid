package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/20/21
 *
 */
@Keep
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String,
)
