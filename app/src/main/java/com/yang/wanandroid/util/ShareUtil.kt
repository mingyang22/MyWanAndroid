package com.yang.wanandroid.util

import android.app.Activity
import androidx.core.app.ShareCompat
import com.yang.wanandroid.R

/**
 * @author ym on 1/12/21
 *
 */
fun share(
    activity: Activity,
    title: String? = activity.getString(R.string.app_name),
    content: String?,
) {
    ShareCompat.IntentBuilder.from(activity)
        .setType("text/plain")
        .setSubject(title)
        .setText(content)
        .setChooserTitle(title)
        .startChooser()
}