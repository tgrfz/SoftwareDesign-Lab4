package com.example.softwaredesign_lab4

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = this.web_view_holder
        val link = intent.getStringExtra("link") ?: ""

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                this@WebViewActivity.progress_bar.visibility = ProgressBar.INVISIBLE
            }
        }
        webView.settings.loadsImagesAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.setAppCachePath(filesDir.absolutePath + "/cache")
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (!isNetworkAvailable) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        }
        webView.loadUrl(link)
    }
}

