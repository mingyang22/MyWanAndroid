package com.yang.wanandroid.model.api

import java.lang.RuntimeException

/**
 * @author ym on 11/17/20
 *
 */
class ApiException(var code: Int, override var message: String) : RuntimeException()