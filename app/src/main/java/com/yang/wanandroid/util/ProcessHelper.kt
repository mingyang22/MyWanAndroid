package com.yang.wanandroid.util

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * @author ym on 1/4/21
 * 获取进程名称
 */
fun isMainProcess(context: Context) = context.packageName == currentProcessName(context)

private fun currentProcessName(context: Context): String {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (process in manager.runningAppProcesses) {
        if (process.pid == Process.myPid()) {
            return process.processName
        }
    }
    return ""
}