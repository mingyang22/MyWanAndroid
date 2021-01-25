package com.yang.wanandroid.ui.points.rank

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.common.loadmore.LoadMoreStatus
import com.yang.wanandroid.model.bean.PointRank
import com.yang.wanandroid.model.bean.PointRecord
import com.yang.wanandroid.ui.points.PointsRepository

/**
 * @author ym on 1/21/21
 *
 */
class PointsRankViewModel : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 1
    }

    private val repository by lazy { PointsRepository() }

    val pointsRank = MutableLiveData<MutableList<PointRank>>()

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    var page = INITIAL_PAGE

    fun refreshData() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                val points = repository.getMyPoints()
                val pagination = repository.getPointsRank(INITIAL_PAGE)
                page = pagination.curPage
                pointsRank.value = pagination.datas.toMutableList()

                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )

    }

    fun loadMoreData() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING

                val pagination = repository.getPointsRank(page + 1)
                page = pagination.curPage
                pointsRank.value?.addAll(pagination.datas)

                loadMoreStatus.value =
                    if (pagination.offset >= pagination.total) LoadMoreStatus.END else LoadMoreStatus.COMPLETED
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )

    }

}