package com.yang.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * @author ym on 1/22/21
 *
 */
@Keep
data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>,
)
