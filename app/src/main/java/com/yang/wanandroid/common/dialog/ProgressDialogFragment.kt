package com.yang.wanandroid.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.yang.wanandroid.R
import kotlinx.android.synthetic.main.dialog_fragment_progress.*

/**
 * @author ym on 11/16/20
 * 加载等待框，进度条 ContentLoadingProgressBar 为延时显示，避免闪烁
 */
class ProgressDialogFragment : DialogFragment() {

    private var messageResId: Int? = null

    companion object {
        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvMessage.text = getString(messageResId ?: R.string.loading)
    }

    fun show(
        fragmentManager: FragmentManager,
        @StringRes messageResId: Int,
        isCancelable: Boolean = false
    ) {
        this.messageResId = messageResId
        this.isCancelable = isCancelable
        try {
            show(fragmentManager, "ProgressDialogFragment")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}