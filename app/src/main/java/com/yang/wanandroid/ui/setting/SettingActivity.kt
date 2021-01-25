package com.yang.wanandroid.ui.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.yang.wanandroid.BuildConfig
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ext.setNavigationBarColor
import com.yang.wanandroid.ext.showToast
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.model.store.SettingsStore
import com.yang.wanandroid.ui.detail.DetailActivity
import com.yang.wanandroid.ui.login.LoginActivity
import com.yang.wanandroid.util.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.view_change_text_zoom.view.*

class SettingActivity : BaseVmActivity<SettingViewModel>() {

    override fun viewModelClass() = SettingViewModel::class.java

    override fun getLayoutId() = R.layout.activity_setting

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setNavigationBarColor(getColor(R.color.bgColorSecondary))

        scDayNight.isChecked = isNightMode(this)
        tvFontSize.text = "${SettingsStore.getWebTextZoom()}%"
        tvClearCache.text = getCacheSize(this)
        tvAboutUs.text = getString(R.string.current_version, BuildConfig.VERSION_NAME)

        ivBack.setOnClickListener { ActivityHelper.finish(SettingActivity::class.java) }
        scDayNight.setOnCheckedChangeListener { _, isChecked ->
            setNightMode(isChecked)
            SettingsStore.setNightMode(isChecked)
        }
        llFontSize.setOnClickListener {
            setFontSize()
        }
        llClearCache.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_clear_cache)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    clearCache(this)
                    tvClearCache.text = getCacheSize(this)
                }.show()
        }
        llCheckVersion.setOnClickListener {
            showToast(getString(R.string.stay_tuned))
        }

        llAboutUs.setOnClickListener {
            ActivityHelper.start(DetailActivity::class.java,
                mapOf(DetailActivity.PARAM_ARTICLE to Article(title = getString(R.string.about_us),
                    link = "https://blog.csdn.net/ym4189")))
        }
        tvLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_logout)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    viewModel.logout()
                    ActivityHelper.start(LoginActivity::class.java)
                    ActivityHelper.finish(SettingActivity::class.java)
                }.show()
        }
        tvLogout.isVisible = isLogin()
        ivTest.setOnClickListener { }

    }

    /**
     * 设置网页字体大小
     */
    @SuppressLint("SetTextI18n")
    private fun setFontSize() {
        val textZoom = SettingsStore.getWebTextZoom()
        var tempTextZoom = textZoom
        AlertDialog.Builder(this)
            .setTitle(R.string.font_size)
            .setView(LayoutInflater.from(this).inflate(R.layout.view_change_text_zoom, null).apply {
                seekBar.progress = textZoom - 50
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean,
                    ) {
                        tempTextZoom = 50 + progress
                        tvFontSize.text = "$tempTextZoom%"
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                })
            })
            .setNegativeButton(R.string.cancel) { _, _ ->
                tvFontSize.text = "$textZoom%"
            }
            .setPositiveButton(R.string.confirm) { _, _ ->
                SettingsStore.setWebTextZoom(tempTextZoom)
            }.show()

    }

}