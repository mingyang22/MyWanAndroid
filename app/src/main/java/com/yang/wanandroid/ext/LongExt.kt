package com.yang.wanandroid.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author ym on 1/21/21
 *
 */
fun Long.toDateTime(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)