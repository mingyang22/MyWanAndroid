package com.yang.wanandroid.ui.detail

import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup.LayoutParams
import android.webkit.ConsoleMessage
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.yang.wanandroid.R
import com.yang.wanandroid.base.BaseVmActivity
import com.yang.wanandroid.common.bus.Bus
import com.yang.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.yang.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.yang.wanandroid.common.core.ActivityHelper
import com.yang.wanandroid.common.dialog.ActionDialogFragment
import com.yang.wanandroid.ext.htmlToSpanned
import com.yang.wanandroid.ext.setBrightness
import com.yang.wanandroid.model.store.SettingsStore
import com.yang.wanandroid.model.bean.Article
import com.yang.wanandroid.util.isNightMode
import com.yang.wanandroid.util.whiteHostList
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * @author ym on 1/11/21
 * 文章详情
 */
class DetailActivity : BaseVmActivity<DetailViewModel>() {

    companion object {
        const val TAG = "DetailActivity"
        const val PARAM_ARTICLE = "param_article"
    }

    private lateinit var article: Article

    private var agentWeb: AgentWeb? = null

    override fun getLayoutId() = R.layout.activity_detail

    override fun viewModelClass() = DetailViewModel::class.java

    override fun initView() {
        article = intent?.getParcelableExtra(PARAM_ARTICLE) ?: return
        tvTitle.text = article.title.htmlToSpanned()
        ivBack.setOnClickListener { ActivityHelper.finish(DetailActivity::class.java) }
        ivMore.setOnClickListener {
            ActionDialogFragment.newInstance(article).show(supportFragmentManager)
        }

        if (isNightMode(this)) {
            setBrightness(0.08f)
        }

        if (article.id != 0) {
            viewModel.saveReadHistory(article)
        }

        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(webContainer, LayoutParams(-1, -1))
            .useDefaultIndicator(getColor(R.color.textColorPrimary), 2)
            .interceptUnkownUrl()
            .setMainFrameErrorView(R.layout.include_reload, R.id.btnReload)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(webViewClient)
            .createAgentWeb()
            .ready()
            .get()
        agentWeb?.webCreator?.webView?.run {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.run {
                javaScriptCanOpenWindowsAutomatically = false
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                textZoom = SettingsStore.getWebTextZoom()
            }
        }
        agentWeb?.urlLoader?.loadUrl(article.link)

    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            setTitle(title)
            super.onReceivedTitle(view, title)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.d(TAG, "${consoleMessage?.message()}")
            return super.onConsoleMessage(consoleMessage)
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return !whiteHostList().contains(request?.url?.host)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.loadUrl(customJs(url))
        }
    }

    /**
     * 加载js，去掉掘金、简书、CSDN等H5页面的Title、底部操作栏，以及部分广告
     */
    private fun customJs(url: String? = article.link): String {
        val js = StringBuilder()
        js.append("javascript:(function(){")
        when (Uri.parse(url).host) {
            "juejin.im" -> {
                js.append("var headerList = document.getElementsByClassName('main-header-box');")
                js.append("if(headerList&&headerList.length){headerList[0].parentNode.removeChild(headerList[0])}")
                js.append("var openAppList = document.getElementsByClassName('open-in-app');")
                js.append("if(openAppList&&openAppList.length){openAppList[0].parentNode.removeChild(openAppList[0])}")
                js.append("var actionBox = document.getElementsByClassName('action-box');")
                js.append("if(actionBox&&actionBox.length){actionBox[0].parentNode.removeChild(actionBox[0])}")
                js.append("var actionBarList = document.getElementsByClassName('action-bar');")
                js.append("if(actionBarList&&actionBarList.length){actionBarList[0].parentNode.removeChild(actionBarList[0])}")
                js.append("var columnViewList = document.getElementsByClassName('column-view');")
                js.append("if(columnViewList&&columnViewList.length){columnViewList[0].style.margin = '0px'}")
            }
            "www.jianshu.com" -> {
                js.append("var jianshuHeader = document.getElementById('jianshu-header');")
                js.append("if(jianshuHeader){jianshuHeader.parentNode.removeChild(jianshuHeader)}")
                js.append("var headerShimList = document.getElementsByClassName('header-shim');")
                js.append("if(headerShimList&&headerShimList.length){headerShimList[0].parentNode.removeChild(headerShimList[0])}")
                js.append("var fubiaoList = document.getElementsByClassName('fubiao-dialog');")
                js.append("if(fubiaoList&&fubiaoList.length){fubiaoList[0].parentNode.removeChild(fubiaoList[0])}")
                js.append("var ads = document.getElementsByClassName('note-comment-above-ad-wrap');")
                js.append("if(ads&&ads.length){ads[0].parentNode.removeChild(ads[0])}")
                js.append("var lazyShimList = document.getElementsByClassName('v-lazy-shim');")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[0]){lazyShimList[0].parentNode.removeChild(lazyShimList[0])}")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[1]){lazyShimList[1].parentNode.removeChild(lazyShimList[1])}")
                js.append("var callAppBtnList = document.getElementsByClassName('call-app-btn');")
                js.append("if(callAppBtnList&&callAppBtnList.length){callAppBtnList[0].parentNode.removeChild(callAppBtnList[0])}")
            }
            "blog.csdn.net" -> {
                js.append("var csdnToolBar = document.getElementById('csdn-toolbar');")
                js.append("if(csdnToolBar){csdnToolBar.parentNode.removeChild(csdnToolBar)}")
                js.append("var csdnMain = document.getElementById('main');")
                js.append("if(csdnMain){csdnMain.style.margin='0px'}")
                js.append("var operate = document.getElementById('operate');")
                js.append("if(operate){operate.parentNode.removeChild(operate)}")
                js.append("var feedSignSpanList = document.getElementsByClassName('feed-Sign-span');")
                js.append("if(feedSignSpanList&&feedSignSpanList.length){feedSignSpanList[0].parentNode.removeChild(feedSignSpanList[0])}")
            }
        }
        js.append("})()")
        return js.toString()
    }

    private fun setTitle(title: String?) {
        tvTitle.text = title
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event) == true) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    fun changeCollect() {
        if (article.collect) {
            viewModel.uncollect(article.id)
        } else {
            viewModel.collect(article.id)
        }
    }

    fun refreshPage() {
        agentWeb?.urlLoader?.reload()
    }

    override fun observe() {
        super.observe()
        viewModel.collect.observe(this, {
            if (article.collect != it) {
                article.collect = it
                Bus.post(USER_COLLECT_UPDATED, article.id to it)
            }
        })
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this, {
            viewModel.updateCollectStatus(article.id)
        })
    }

}