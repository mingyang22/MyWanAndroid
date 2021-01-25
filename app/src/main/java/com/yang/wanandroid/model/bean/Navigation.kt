package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/20/21
 *
 */
@Keep
data class Navigation(
    val cid: Int,
    val name: String,
    val articles: List<Article>,
)
