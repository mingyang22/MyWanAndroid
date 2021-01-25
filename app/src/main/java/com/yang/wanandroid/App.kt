package com.yang.wanandroid

import android.app.Application
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.util.isMainProcess

/**
 * @author ym on 11/16/20
 *
 */
class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (isMainProcess(this)) {
            init()
        }
    }

    private fun init() {
        ActivityHelper.init(this)
    }

}