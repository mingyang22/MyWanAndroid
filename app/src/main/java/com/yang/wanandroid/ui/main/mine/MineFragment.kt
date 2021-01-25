package com.yang.wanandroid.ui.main.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseFragment
import com.yang.wanandroid.base.BaseVmFragment
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.interfaces.ScrollToTop
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.store.UserInfoStore
import com.yang.wanandroid.ui.collection.CollectionActivity
import com.yang.wanandroid.ui.detail.DetailActivity
import com.yang.wanandroid.ui.history.HistoryActivity
import com.yang.wanandroid.ui.points.mine.MinePointsActivity
import com.yang.wanandroid.ui.points.rank.PointsRankActivity
import com.yang.wanandroid.ui.setting.SettingActivity
import com.yang.wanandroid.ui.share.myshared.MySharedActivity
import com.yang.wanandroid.util.isLogin
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * @author ym on 1/6/21
 * 我的
 */
class MineFragment : BaseVmFragment<MineViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }

    override fun getLayoutId() = R.layout.fragment_mine

    override fun viewModelClass() = MineViewModel::class.java

    override fun initView() {
        clHeader.setOnClickListener {
            checkLogin {/* 上传头像 */ }
        }
        llMyPoints.setOnClickListener {
            checkLogin { ActivityHelper.start(MinePointsActivity::class.java) }
        }
        llPointsRank.setOnClickListener {
            checkLogin { ActivityHelper.start(PointsRankActivity::class.java) }
        }
        llMyShare.setOnClickListener {
            checkLogin { ActivityHelper.start(MySharedActivity::class.java) }
        }
        llMyCollect.setOnClickListener {
            checkLogin { ActivityHelper.start(CollectionActivity::class.java) }
        }
        llHistory.setOnClickListener {
            ActivityHelper.start(HistoryActivity::class.java)
        }
        llAboutAuthor.setOnClickListener {
            ActivityHelper.start(DetailActivity::class.java,
                mapOf(DetailActivity.PARAM_ARTICLE to Article(title = getString(R.string.my_about_author),
                    link = "https://blog.csdn.net/ym4189")))
        }
        llOpenSource.setOnClickListener {

        }
        llSetting.setOnClickListener {
            ActivityHelper.start(SettingActivity::class.java)
        }

        updateUi()

    }

    override fun observe() {
        super.observe()
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            updateUi()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        val isLogin = isLogin()
        val userInfo = UserInfoStore.getUserInfo()
        tvLoginRegister.isGone = isLogin
        tvNickName.isVisible = isLogin
        tvId.isVisible = isLogin
        if (isLogin && userInfo != null) {
            tvNickName.text = userInfo.nickname
            tvId.text = "ID: ${userInfo.id}"
        }

    }

}