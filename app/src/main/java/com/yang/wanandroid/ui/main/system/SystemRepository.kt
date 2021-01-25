package com.yang.wanandroid.ui.main.system

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/18/21
 *
 */
class SystemRepository {
    /**
     * 获取体系分类
     */
    suspend fun getArticleCategories() = RetrofitClient.apiService.getArticleCategories().apiData()

    /**
     * 根据分类获取体系数据
     */
    suspend fun getArticleListByCid(page: Int, cid: Int) =
        RetrofitClient.apiService.getArticleListByCid(page, cid).apiData()
}