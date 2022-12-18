package com.xtianxian.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Message
import android.webkit.*

@SuppressLint("SetJavaScriptEnabled")
class ViewSetup(
    private val view: WebView,
    private val viewCallback: ViewCallback
) {

    private val baseUrl = with(view.context) {
        getStr("minimize") +
        "://" +
        getStr("offender") +
        "/"
    }

    init {
        with(view) {
            CookieManager.getInstance().apply {
                setAcceptCookie(true)
                setAcceptThirdPartyCookies(view, true)
            }

            webViewClient = webViewClient()
            webChromeClient = webChromeClient(view.context)

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = false
                userAgentString = userAgentString.fix
            }
        }
    }

    fun onBackPressed() {
        if (view.canGoBack()) {
            view.goBack()
        }
    }

    private fun webViewClient(): WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            checkUrl(url = url)
            CookieManager.getInstance().flush()
        }
    }

    private fun webChromeClient(
        context: Context
    ): WebChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri?>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            viewCallback.onFileChoose(
                valueCallback = filePathCallback
            )
            return true
        }
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            val newWebView = WebView(context)
            newWebView.webChromeClient = this
            with (newWebView.settings) {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                userAgentString = userAgentString.fix
                domStorageEnabled = true
                setSupportMultipleWindows(true)
            }
            val transport = resultMsg?.obj as? WebView.WebViewTransport
            transport?.webView = newWebView
            resultMsg?.sendToTarget()
            return true
        }
    }

    private fun checkUrl(url: String?) {
        when {
            url == baseUrl -> {
                viewCallback.onEmpty()
            }
            url?.contains(baseUrl) == false -> {
                viewCallback.onDataSave(
                    data = url
                )
            }
        }
    }
}

interface ViewCallback {
    fun onDataSave(data: String?)
    fun onFileChoose(valueCallback: ValueCallback<Array<Uri?>>?)
    fun onEmpty()
}