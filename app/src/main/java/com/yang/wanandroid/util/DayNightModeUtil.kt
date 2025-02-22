package com.yang.wanandroid.util

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

/**
 * @author ym on 1/12/21
 * 夜间模式
 */
fun isNightMode(context: Context): Boolean {
    val mode = context.resources.configuration.uiMode and UI_MODE_NIGHT_MASK
    return mode == UI_MODE_NIGHT_MASK
}

fun setNightMode(isNightMode: Boolean) {
    AppCompatDelegate.setDefaultNightMode(if (isNightMode) MODE_NIGHT_YES else MODE_NIGHT_NO)
}