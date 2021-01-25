package com.yang.wanandroid.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author ym on 1/4/21
 * 消息总线框架，使用 LiveEventBus
 */
object Bus {

    /**
     * 发布消息
     * @param channel String 渠道
     * @param value T 内容
     */
    inline fun <reified T> post(channel: String, value: T) {
        LiveEventBus.get(channel, T::class.java).post(value)
    }

    /**
     * 订阅消息
     * @param channel String 渠道
     * @param owner LifecycleOwner 生命周期 owner
     * @param observer Observer<T> 观察者
     */
    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observe(owner, observer)
    }



}