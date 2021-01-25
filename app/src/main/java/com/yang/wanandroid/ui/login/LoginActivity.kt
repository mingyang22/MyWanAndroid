package com.yang.wanandroid.ui.login

import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author ym on 1/12/21
 * 登录
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    override fun getLayoutId() = R.layout.activity_login

    override fun viewModelClass() = LoginViewModel::class.java

    override fun initView() {
        ivClose.setOnClickListener { ActivityHelper.finish(LoginActivity::class.java) }
        tvGoRegister.setOnClickListener { ActivityHelper.start(RegisterActivity::class.java) }

        tietPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_GO) {
                btnLogin.performClick()
                true
            } else {
                false
            }
        }
        btnLogin.setOnClickListener {
            tilAccount.error = ""
            tilPassword.error = ""
            val account = tietAccount.text.toString()
            val password = tietPassword.text.toString()
            when {
                account.isEmpty() -> tilAccount.error = getString(R.string.account_can_not_be_empty)
                password.isEmpty() -> tilPassword.error =
                    getString(R.string.password_can_not_be_empty)
                else -> viewModel.login(account, password)
            }
        }
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            submitting.observe(this@LoginActivity, {
                if (it) showProgressDialog(R.string.logging_in) else dismissProgressDialog()
            })
            loginResult.observe(this@LoginActivity, {
                if (it) {
                    ActivityHelper.finish(LoginActivity::class.java)
                }
            })
        }
    }

}