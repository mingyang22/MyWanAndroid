package com.yang.wanandroid.ui.points

import com.yang.wanandroid.model.api.RetrofitClient

/**
 * @author ym on 1/21/21
 * 积分
 */
class PointsRepository {

    /**
     * 获取我的积分
     * @return PointRank
     */
    suspend fun getMyPoints() = RetrofitClient.apiService.getPoints().apiData()

    /**
     * 我的积分获取记录
     * @param page Int
     * @return Pagination<PointRecord>
     */
    suspend fun getPointsRecord(page: Int) =
        RetrofitClient.apiService.getPointsRecord(page).apiData()

    /**
     * 获取积分排行
     * @param page Int
     * @return Pagination<PointRank>
     */
    suspend fun getPointsRank(page: Int) = RetrofitClient.apiService.getPointsRank(page).apiData()
}