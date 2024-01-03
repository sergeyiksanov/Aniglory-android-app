package com.example.aniglory_app.for_player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout

class NewWebChromeClient(val mWebView: WebView, val mFullScreenContainer: FrameLayout, val actvity: Activity) : WebChromeClient() {

    var mFullScreenView: View? = null
    var mFullscreenViewCallback: WebChromeClient.CustomViewCallback? = null

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)

        mWebView.visibility = View.GONE
        mFullScreenContainer.visibility = View.VISIBLE
        mFullScreenContainer.addView(view)

        mFullScreenView = view!!
        mFullscreenViewCallback = callback!!

        actvity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    override fun onHideCustomView() {
        super.onHideCustomView()

        mFullScreenContainer.removeView(mFullScreenView)
        mFullscreenViewCallback?.onCustomViewHidden()
        mFullScreenView = null

        mWebView.visibility = View.VISIBLE
        mFullScreenContainer.visibility = View.GONE

        actvity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}