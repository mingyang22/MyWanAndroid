package com.yang.wanandroid.ui.splash

import android.os.Bundle
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ui.main.MainActivity

/**
 * @author ym on 1/4/21
 * 启动页
 */
class SplashActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.postDelayed({
            ActivityHelper.start(MainActivity::class.java)
            ActivityHelper.finish(SplashActivity::class.java)
        }, 1000)
    }

}