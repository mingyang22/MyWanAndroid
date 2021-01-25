package com.yang.wanandroid.ext

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

/**
 * @author ym on 1/12/21
 *
 */
/**
 * 在浏览器打开链接
 * @receiver Fragment
 * @param link String?
 */
fun Fragment.openInExplorer(link: String?) {
    startActivity(Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(link)
    })
}