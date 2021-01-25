package com.yang.wanandroid.common.core

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.yang.wanandroid.common.simple.ActivityLifecycleCallbacksAdapter
import com.yang.wanandroid.ext.putExtras

/**
 * @author ym on 1/5/21
 *
 */
object ActivityHelper {
    private val activities = mutableListOf<Activity>()

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                activities.add(activity)
            },
            onActivityDestroyed = { activity ->
                activities.remove(activity)
            }
        ))
    }

    fun start(clazz: Class<out Activity>, params: Map<String, Any> = emptyMap()) {
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
    }

    /**
     * finish指定的一个或多个Activity
     */
    fun finish(vararg clazz: Class<out Activity>) {
        activities.forEach {
            if (clazz.contains(it::class.java)) {
                it.finish()
            }
        }
    }

}