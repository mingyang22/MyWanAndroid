package com.yang.wanandroid.ext

import androidx.core.text.HtmlCompat

/**
 * @author ym on 11/17/20
 * String 扩展函数
 */
fun String?.htmlToSpanned() =
    if (this.isNullOrEmpty()) "" else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)