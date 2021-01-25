package com.yang.wanandroid.ui.detail

import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.room.RoomHelper

/**
 * @author ym on 1/11/21
 * 文章详情
 */
class DetailRepository : BaseViewModel() {

    suspend fun saveReadHistory(article: Article) = RoomHelper.addReadHistory(article)
}