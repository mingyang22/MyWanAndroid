package com.yang.wanandroid.ui.points.mine

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.PointRank
import com.yang.wanandroid.model.bean.PointRecord
import com.yang.wanandroid.ui.points.PointsRepository

/**
 * @author ym on 1/21/21
 * 我的积分
 */
class MinePointsViewModel : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 1
    }

    private val repository by lazy { PointsRepository() }

    val totalPoints = MutableLiveData<PointRank>()
    val pointsList = MutableLiveData<MutableList<PointRecord>>()

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    var page = INITIAL_PAGE

    fun refresh() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val points = repository.getMyPoints()
                val pagination = repository.getPointsRecord(INITIAL_PAGE)
                page = pagination.curPage
                totalPoints.value = points
                pointsList.value = pagination.datas.toMutableList()

                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )

    }

    fun loadMoreRecord() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING

                val pagination = repository.getPointsRecord(page + 1)
                page = pagination.curPage
                pointsList.value?.addAll(pagination.datas)

                loadMoreStatus.value =
                    if (pagination.offset >= pagination.total) LoadMoreStatus.END else LoadMoreStatus.COMPLETED
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )

    }

}