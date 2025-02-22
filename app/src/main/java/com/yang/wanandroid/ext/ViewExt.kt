package com.yang.wanandroid.ext

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @author ym on 1/13/21
 *
 */

/**
 * 弹出软键盘
 * @receiver View
 */
fun View.showSoftInput() {
    requestFocus()
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 隐藏软键盘
 * @receiver View
 */
fun View.hideSoftInput() {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}