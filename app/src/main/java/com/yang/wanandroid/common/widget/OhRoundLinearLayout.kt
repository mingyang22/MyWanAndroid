package com.yang.wanandroid.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.yang.wanandroid.util.oh.OhRoundViewDrawable
import com.yang.wanandroid.util.oh.OhViewHelper

class OhRoundLinearLayout : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val bg = OhRoundViewDrawable.fromAttributeSet(context, attrs, defStyleAttr)
        OhViewHelper.setBackgroundKeepingPadding(this, bg)
    }
}