package com.yang.wanandroid.model.room

import androidx.room.*
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.bean.Tag

/**
 * @author ym on 1/25/21
 * 操作数据库方法
 */
@Dao
interface ReadHistoryDao {

    @Insert
    suspend fun insert(article: Article)

    @Insert
    suspend fun insertArticleTag(tag: Tag)

    @Query("select * from article")
    suspend fun queryAll(): List<ReadHistory>

    @Query("select * from article where id = :id")
    suspend fun queryArticle(id: Int): Article?

    @Delete
    suspend fun deleteArticle(article: Article)

    @Update
    suspend fun updateArticle(article: Article)

}