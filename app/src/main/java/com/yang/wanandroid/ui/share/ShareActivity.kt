package com.yang.wanandroid.ui.share

import android.view.inputmethod.EditorInfo
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ext.hideSoftInput
import com.yang.wanandroid.ext.showToast
import kotlinx.android.synthetic.main.activity_share.*

/**
 * @author ym on 1/6/21
 * 分享文章
 */
class ShareActivity : BaseVmActivity<ShareViewModel>() {

    override fun viewModelClass() = ShareViewModel::class.java

    override fun getLayoutId() = R.layout.activity_share

    override fun initView() {
        ivBack.setOnClickListener { ActivityHelper.finish(ShareActivity::class.java) }
        acetlink.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tvSubmit.performClick()
                true
            } else {
                false
            }
        }
        tvSubmit.setOnClickListener {
            val title = acetTitle.text.toString().trim()
            val link = acetlink.text.toString().trim()
            if (title.isEmpty()) {
                showToast(R.string.title_toast)
                return@setOnClickListener
            }
            if (link.isEmpty()) {
                showToast(R.string.link_toast)
                return@setOnClickListener
            }
            tvSubmit.hideSoftInput()
            viewModel.shareArticle(title, link)
        }
        viewModel.getUserInfo()
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            userInfo.observe(this@ShareActivity, {
                val sharePeople = if (it.nickname.isEmpty()) it.username else it.nickname
                acetSharePeople.setText(sharePeople)
            })
            submitting.observe(this@ShareActivity, {
                if (it) showProgressDialog(R.string.share_article) else dismissProgressDialog()
            })
            shareResult.observe(this@ShareActivity, {
                if (it) {
                    showToast(R.string.share_article_success)
                }
            })

        }
    }

}