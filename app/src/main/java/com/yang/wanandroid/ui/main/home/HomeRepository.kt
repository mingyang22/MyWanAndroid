package com.yang.wanandroid.ui.main.home

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/7/21
 * 首页数据请求
 */
class HomeRepository {

    // 热门置顶文章
    suspend fun getTopArticleList() = RetrofitClient.apiService.getTopArticleList().apiData()

    // 热门所有文章
    suspend fun getArticleList(page: Int) = RetrofitClient.apiService.getArticleList(page).apiData()

    // 最新文章
    suspend fun getProjectList(page: Int) = RetrofitClient.apiService.getProjectList(page).apiData()

    // 广场文章
    suspend fun getUserArticleList(page: Int) =
        RetrofitClient.apiService.getUserArticleList(page).apiData()

    // 获取项目类别
    suspend fun getProjectCategories() = RetrofitClient.apiService.getProjectCategories().apiData()

    // 获取类别下文章
    suspend fun getProjectListByCid(page: Int, cid: Int) =
        RetrofitClient.apiService.getProjectListByCid(page, cid).apiData()

    suspend fun getWechatCategories() = RetrofitClient.apiService.getWechatCategories().apiData()

    suspend fun getWechatArticleList(page: Int, id: Int) =
        RetrofitClient.apiService.getWechatArticleList(page, id).apiData()

}