package com.yang.wanandroid.util

import android.content.Context

/**
 * @author ym on 1/19/21
 *
 */

/**
 * 获取屏幕高度
 */
fun getScreenHeight(context: Context) = context.resources.displayMetrics.heightPixels

/**
 * 获取屏幕宽度
 */
fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels