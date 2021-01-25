package com.yang.wanandroid.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseActivity
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ext.hideSoftInput
import com.yang.wanandroid.ui.search.history.SearchHistoryFragment
import com.yang.wanandroid.ui.search.result.SearchResultFragment
import kotlinx.android.synthetic.main.activity_search.*

/**
 * @author ym on 1/13/21
 * 搜索
 */
class SearchActivity : BaseActivity() {

    companion object {
        const val SEARCH_WORDS = "search_words"
    }

    override fun getLayoutId() = R.layout.activity_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val historyFragment = SearchHistoryFragment.newInstance()
        val resultFragment = SearchResultFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, historyFragment)
            .add(R.id.flContainer, resultFragment)
            .show(historyFragment)
            .hide(resultFragment)
            .commit()
        ivBack.setOnClickListener {
            if (resultFragment.isVisible) {
                supportFragmentManager.beginTransaction().hide(resultFragment).commit()
            } else {
                ActivityHelper.finish(SearchActivity::class.java)
            }
        }
        ivDone.setOnClickListener {
            val searchWords = acetInput.text.toString()
            if (searchWords.isEmpty()) return@setOnClickListener
            it.hideSoftInput()
            historyFragment.addSearchHistory(searchWords)
            resultFragment.doSearch(searchWords)
            supportFragmentManager.beginTransaction().show(resultFragment).commit()
        }
        acetInput.run {
            addTextChangedListener(afterTextChanged = {
                ivClearSearch.isGone = it.isNullOrEmpty()
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ivDone.performClick()
                    true
                } else {
                    false
                }
            }
        }
        ivClearSearch.setOnClickListener { acetInput.setText("") }


    }

    /**
     * 搜索
     * @param keywords String 搜索关键字
     */
    fun fillSearchInput(keywords: String) {
        acetInput.setText(keywords)
        acetInput.setSelection(keywords.length)
        ivDone.performClick()
    }

    override fun onBackPressed() {
        ivBack.performClick()
    }
}