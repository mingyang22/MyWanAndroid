package com.yang.wanandroid.ui.history

import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.room.RoomHelper

/**
 * @author ym on 1/25/21
 * 浏览历史
 */
class HistoryRepository {
    suspend fun getReadHistory() = RoomHelper.queryAllReadHistory()

    suspend fun deleteHistory(article: Article) = RoomHelper.deleteReadHistory(article)
}