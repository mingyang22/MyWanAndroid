package com.yang.wanandroid.model.room

import androidx.room.Room
import com.yang.wanandroid.App
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.bean.Tag

/**
 * @author ym on 1/25/21
 * Room 帮助类
 */
object RoomHelper {

    private val appDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "database_wanandroid").build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    suspend fun queryAllReadHistory() = readHistoryDao.queryAll()
        .map { it.article.apply { tags = it.tags } }.reversed()

    suspend fun addReadHistory(article: Article) {
        readHistoryDao.queryArticle(article.id)?.let {
            readHistoryDao.deleteArticle(it)
        }
        // 插入第一条
        readHistoryDao.insert(article.apply { primaryKeyId = 0 })
        article.tags.forEach {
            readHistoryDao.insertArticleTag(Tag(id = 0,
                articleId = article.id.toLong(),
                name = it.name,
                url = it.url))
        }
    }

    suspend fun deleteReadHistory(article: Article) = readHistoryDao.deleteArticle(article)

}