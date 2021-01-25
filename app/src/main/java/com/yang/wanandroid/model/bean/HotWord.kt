package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/14/21
 * 热门搜索
 */
@Keep
data class HotWord(
    val id: Int,
    val link: String,
    val order: Int,
    val name: String,
    val visible: Int,
)
