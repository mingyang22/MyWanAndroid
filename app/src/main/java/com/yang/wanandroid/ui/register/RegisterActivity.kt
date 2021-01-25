package com.yang.wanandroid.ui.register

import android.view.inputmethod.EditorInfo
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author ym on 1/15/21
 * 注册
 */
class RegisterActivity : BaseVmActivity<RegisterViewModel>() {

    override fun getLayoutId() = R.layout.activity_register

    override fun viewModelClass() = RegisterViewModel::class.java

    override fun initView() {
        ivBack.setOnClickListener { ActivityHelper.finish(RegisterActivity::class.java) }
        tietConfirmPssword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                btnRegister.performClick()
                true
            } else {
                false
            }
        }
        btnRegister.setOnClickListener {
            tilAccount.error = ""
            tilPassword.error = ""
            tilConfirmPssword.error = ""

            val account = tietAccount.text.toString()
            val password = tietPassword.text.toString()
            val confirmPassword = tietConfirmPssword.text.toString()

            when {
                account.isEmpty() -> tilAccount.error = getString(R.string.account_can_not_be_empty)
                account.length < 3 -> getString(R.string.account_length_over_three)
                password.isEmpty() -> tilPassword.error =
                    getString(R.string.password_can_not_be_empty)
                password.length < 6 -> tilPassword.error =
                    getString(R.string.password_length_over_six)
                confirmPassword.isEmpty() -> tilConfirmPssword.error =
                    getString(R.string.confirm_password_can_not_be_empty)
                password != confirmPassword -> tilConfirmPssword.error =
                    getString(R.string.two_password_are_inconsistent)
                else -> viewModel.register(account, password, confirmPassword)
            }
        }
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            submitting.observe(this@RegisterActivity, {
                if (it) showProgressDialog(R.string.registerring) else dismissProgressDialog()
            })
            registerResult.observe(this@RegisterActivity, {
                if (it) {
                    ActivityHelper.finish(LoginActivity::class.java, RegisterActivity::class.java)
                }
            })
        }
    }

}