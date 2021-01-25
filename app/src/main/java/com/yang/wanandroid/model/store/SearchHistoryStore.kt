package com.yang.wanandroid.model.store

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yang.wanandroid.App
import com.yang.wanandroid.util.getSpValue
import com.yang.wanandroid.util.putSpValue

/**
 * @author ym on 1/14/21
 * 搜索历史记录
 */
object SearchHistoryStore {

    private const val SP_SEARCH_HISTORY = "sp_search_history"
    private const val KEY_SEARCH_HISTORY = "searchHistory"
    private val gson by lazy { Gson() }

    fun saveSearchHistory(words: String) {
        val history = getSearchHistory()
        if (history.contains(words)) {
            history.remove(words)
        }
        history.add(0, words)
        val listStr = gson.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, App.instance, KEY_SEARCH_HISTORY, listStr)
    }

    fun deleteSearchHistory(words: String) {
        val history = getSearchHistory()
        history.remove(words)
        val listStr = gson.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, App.instance, KEY_SEARCH_HISTORY, listStr)
    }

    fun getSearchHistory(): MutableList<String> {
        val listStr = getSpValue(SP_SEARCH_HISTORY, App.instance, KEY_SEARCH_HISTORY, "")
        return if (listStr.isEmpty()) {
            mutableListOf()
        } else {
            gson.fromJson(listStr, object : TypeToken<MutableList<String>>() {}.type)
        }
    }

}