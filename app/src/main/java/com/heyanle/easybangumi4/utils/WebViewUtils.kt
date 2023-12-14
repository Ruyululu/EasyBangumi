package com.heyanle.easybangumi4.utils

import android.webkit.WebView
import com.heyanle.easybangumi4.source.utils.LightweightGettingWebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.text.StringEscapeUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by LoliBall on 2023/12/14 22:53.
 * https://github.com/WhichWho
 */

fun WebView.clearWeb() {
    clearHistory()
    clearFormData()
    clearMatches()
}

fun WebView.stop() {
    stopLoading()
    pauseTimers()
}

suspend fun WebView.getHtml(): String {
    val raw = evaluateJavascriptSync("(function() { return document.documentElement.outerHTML })()")
    return withContext(Dispatchers.Default) { StringEscapeUtils.unescapeEcmaScript(raw) }
}

suspend fun WebView.evaluateJavascriptSync(code: String? = null): String {
    if (code.isNullOrEmpty()) return ""
    return suspendCoroutine { con ->
        evaluateJavascript(code) {
            con.resume(it.orEmpty())
        }
    }
}

fun WebView.evaluateJavascript(code: String? = null) {
    if (code.isNullOrEmpty()) return
    evaluateJavascript(code, null)
//    loadUrl("javascript:(function(){$code})()") // 超级慢
}

suspend fun WebView.waitUntilLoadFinish(timeoutMs: Long = 8000L) {
    waitUntil(null, timeoutMs)
}

suspend fun WebView.waitUntil(regex: Regex? = null, timeoutMs: Long = 8000L): String {
    return withContext(Dispatchers.Main) {
        suspendCoroutine { con ->
            launch {
                delay(timeoutMs)
                con.resume("")
            }
            webViewClient = object : LightweightGettingWebViewClient(regex) {
                override fun onPageFinished(view: WebView?, url: String?) {
                    "onPageFinished: $url".logi("WebView.waitUntil")
                    if (regex == null) con.resume("")
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    "onLoadResource: $url".logi("WebView.waitUntil")
                    if (regex != null && url != null && regex.matches(url)) {
                        con.resume(url)
                    }
                }
            }
        }
    }
}
