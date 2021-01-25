package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/7/21
 *
 */
@Keep
data class Pagination<T>(
    val offset: Int,
    val size: Int,
    val total: Int,
    val pageCount: Int,
    val curPage: Int,
    val over: Boolean,
    val datas: List<T>
)