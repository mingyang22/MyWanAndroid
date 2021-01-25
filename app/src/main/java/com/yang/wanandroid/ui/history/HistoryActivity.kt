package com.yang.wanandroid.ui.history

import android.app.AlertDialog
import androidx.core.view.isVisible
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.ui.adapter.ArticleAdapter
import com.yang.wanandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_history.*

/**
 * @author ym on 1/25/21
 * 浏览历史
 */
class HistoryActivity : BaseVmActivity<HistoryViewModel>() {

    private lateinit var adapter: ArticleAdapter

    override fun viewModelClass() = HistoryViewModel::class.java

    override fun getLayoutId() = R.layout.activity_history

    override fun initView() {
        adapter = ArticleAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            addChildClickViewIds(R.id.iv_collect)
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        viewModel.uncollect(article.id)
                    } else {
                        viewModel.collect(article.id)
                    }
                }
            }
            setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@HistoryActivity)
                    .setMessage(R.string.confirm_delete_history)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        viewModel.deleteHistory(data[position])
                        adapter.removeAt(position)
                        this@HistoryActivity.emptyView.isVisible = data.isEmpty()
                    }.show()
                true
            }
        }
        recyclerView.adapter = adapter

        ivBack.setOnClickListener { ActivityHelper.finish(HistoryActivity::class.java) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    override fun observe() {
        super.observe()
        viewModel.run {
            articleList.observe(this@HistoryActivity, {
                adapter.setNewInstance(it)
            })
            emptyStatus.observe(this@HistoryActivity, {
                emptyView.isVisible = it
            })

        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this, {
            viewModel.updateListCollectState()
        })
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this, {
            viewModel.updateItemCollectState(it)
        })
    }

}