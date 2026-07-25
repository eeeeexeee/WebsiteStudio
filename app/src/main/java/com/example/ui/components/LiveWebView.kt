package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LiveWebView(
    htmlContent: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = true
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_NO_CACHE
            }
            setBackgroundColor(0xFF0F172A.toInt()) // Match dark slate bg
        }
    }

    AndroidView(
        factory = { webView },
        update = { view ->
            view.loadDataWithBaseURL(
                "https://webstudio.internal/",
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        },
        modifier = modifier.fillMaxSize()
    )
}
