package com.padippob.menu

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.padippob.R
import kotlinx.android.synthetic.main.activity_web_view.*
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val dataResponse = intent.getSerializableExtra("ulr").toString()
        val webSettings = WebView.settings
        webSettings.javaScriptEnabled = true
        WebView.loadUrl(dataResponse)
        WebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }
}
