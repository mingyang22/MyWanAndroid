package com.yang.wanandroid.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ui.login.LoginActivity
import com.yang.wanandroid.util.isLogin

/**
 * @author ym on 11/18/20
 *
 */
abstract class BaseVmFragment<VM : BaseViewModel> : BaseFragment() {

    protected lateinit var viewModel: VM
    private var lazyLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observe()
        initData()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
        }
    }

    open fun lazyLoadData() {
        // Override if need
    }

    /**
     * 初始化view
     */
    open fun initView() {
        // Override if need
    }

    /**
     * 初始化数据
     */
    open fun initData() {
        // Override if need
    }

    /**
     * 订阅，LiveData、Bus
     */
    @CallSuper
    open fun observe() {
        viewModel.loginStatusInvalid.observe(viewLifecycleOwner, {
            if (it) {
                Bus.post(USER_LOGIN_STATE_CHANGED, false)
                ActivityHelper.start(LoginActivity::class.java)
            }
        })
    }

    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (isLogin()) {
            then?.invoke()
            true
        } else {
            ActivityHelper.start(LoginActivity::class.java)
            false
        }
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(viewModelClass())
    }

    abstract fun viewModelClass(): Class<VM>

}