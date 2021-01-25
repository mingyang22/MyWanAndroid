package com.yang.wanandroid.model.room

import androidx.room.Embedded
import androidx.room.Relation
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.bean.Tag

/**
 * @author ym on 1/25/21
 * room 多表查询
 */
data class ReadHistory(
    @Embedded // 内嵌对象
    var article: Article,
    @Relation // 多表查询
        (parentColumn = "id", // 来自Article的唯一key
        entityColumn = "article_id" // 来自Tag的唯一key
    )
    var tags: List<Tag>,
)
