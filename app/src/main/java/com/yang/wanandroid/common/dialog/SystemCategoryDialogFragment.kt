package com.yang.wanandroid.common.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yang.wanandroid.App
import com.yang.wanandroid.R
import com.yang.wanandroid.ext.copyTextIntoClipboard
import com.yang.wanandroid.ext.openInExplorer
import com.yang.wanandroid.ext.showToast
import com.yang.wanandroid.model.bean.Category
import com.yang.wanandroid.ui.adapter.SystemCategoryAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import com.yang.wanandroid.ui.main.system.SystemFragment
import com.yang.wanandroid.util.getScreenHeight
import com.yang.wanandroid.util.share
import kotlinx.android.synthetic.main.dialog_system_category.*

/**
 * @author ym on 1/19/21
 * 体系过滤选择
 */
class SystemCategoryDialogFragment : BottomSheetDialogFragment() {

    companion object {

        private const val CATEGORY_LIST = "category_list"

        fun newInstance(categoryList: ArrayList<Category>): SystemCategoryDialogFragment {
            return SystemCategoryDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    private var behavior: BottomSheetBehavior<View>? = null
    private var height: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.dialog_system_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            val categoryList: List<Category> = getParcelableArrayList(CATEGORY_LIST)!!
            val checked = (parentFragment as SystemFragment).getCurrentChecked()
            val adapter = SystemCategoryAdapter(R.layout.item_system_category,
                categoryList.toMutableList(), checked).apply {
                onCheckedListener = {
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    view.postDelayed({ (parentFragment as SystemFragment).check(it) }, 300)
                }
            }
            recyclerView.adapter = adapter
            view.post {
                (recyclerView.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(checked.first, 0)
            }


        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        // 设置高度
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                height ?: ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: (getScreenHeight(App.instance) * 0.75).toInt()
        if (!this.isAdded) {
            super.show(manager, "SystemCategoryDialogFragment")
        }
    }

}