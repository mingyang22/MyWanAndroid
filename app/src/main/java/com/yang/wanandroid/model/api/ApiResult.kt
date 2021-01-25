package com.yang.wanandroid.model.api

import androidx.annotation.Keep

/**
 * @author ym on 1/6/21
 *
 */
@Keep
data class ApiResult<T>(val errorCode: Int, val errorMsg: String, val data: T) {
    fun apiData(): T {
        if (errorCode == 0) {
            return data
        } else {
            throw ApiException(errorCode, errorMsg)
        }
    }
}