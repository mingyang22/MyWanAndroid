package com.yang.wanandroid.ext

import android.app.Activity

/**
 * @author ym on 1/12/21
 * 系统设置
 */

/**
 * 设置导航栏颜色
 * @receiver Activity
 * @param color Int
 */
fun Activity.setNavigationBarColor(color: Int) {
    window.navigationBarColor = color
}

/**
 * 设置Activity的亮度
 * @receiver Activity
 * @param brightness Float
 */
fun Activity.setBrightness(brightness: Float) {
    val attributes = window.attributes
    attributes.screenBrightness = brightness
    window.attributes = attributes
}
