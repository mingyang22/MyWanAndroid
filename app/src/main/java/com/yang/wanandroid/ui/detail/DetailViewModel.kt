package com.yang.wanandroid.ui.detail

import androidx.lifecycle.MutableLiveData
import com.yang.wanandroid.base.BaseViewModel
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.store.UserInfoStore
import com.yang.wanandroid.ui.common.CollectRepository
import com.yang.wanandroid.util.isLogin
import java.net.IDN

/**
 * @author ym on 1/11/21
 * 文章详情
 */
class DetailViewModel : BaseViewModel() {

    private val collectRepository by lazy { CollectRepository() }
    private val detailRepository by lazy { DetailRepository() }

    val collect = MutableLiveData<Boolean>()

    fun collect(id: Int) {
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                collect.value = true
            },
            error = {
                collect.value = false
            }
        )

    }

    fun uncollect(id: Int) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                collect.value = false
            },
            error = {
                collect.value = true
            }
        )

    }

    fun updateCollectStatus(id: Int) {
        collect.value = if (isLogin()) {
            UserInfoStore.getUserInfo()?.collectIds?.contains(id)
        } else {
            false
        }

    }

    fun saveReadHistory(article: Article) {
        launch(
            block = {
                detailRepository.saveReadHistory(article)
            }
        )
    }
}