package com.yang.wanandroid.ui.share

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/20/21
 *
 */
class ShareRepository {
    /**
     * 分享文章
     * @param title String
     * @param link String
     * @return Any
     */
    suspend fun shareArticle(title: String, link: String) =
        RetrofitClient.apiService.shareArticle(title, link).apiData()

    /**
     * 获取分享文章列表
     * @param page Int
     * @return Shared
     */
    suspend fun getSharedArticleList(page: Int) =
        RetrofitClient.apiService.getSharedArticleList(page).apiData()

    /**
     * 删除分享文章
     * @param id Int
     * @return Any
     */
    suspend fun deleteShared(id: Int) = RetrofitClient.apiService.deleteShare(id).apiData()

}