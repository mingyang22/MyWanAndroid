package com.yang.wanandroid.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.bean.Tag

/**
 * @author ym on 1/25/21
 * 数据库持有者
 */
@Database(entities = [Article::class, Tag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readHistoryDao(): ReadHistoryDao
}