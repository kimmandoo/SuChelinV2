package com.suchelin.android.feature.view.school

import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.databinding.FragmentSchoolBinding
import com.suchelin.android.util.JONGHAP
import com.suchelin.android.util.AMARENCE


class SchoolFragment :
    BaseFragment<FragmentSchoolBinding, SchoolViewModel>(R.layout.fragment_school) {
    override val viewModel: SchoolViewModel by viewModels()

    override fun initView() {
        binding.apply {
            webView.apply {
                webViewClient = MyWebViewClient()
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                loadUrl(JONGHAP)
            }
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            val requestedUrl = request?.url.toString()
            when {
                requestedUrl.contains("1793") -> loadUrlWithLogging(view, AMARENCE)
                requestedUrl.contains("1792") -> loadUrlWithLogging(view, JONGHAP)
                else -> return false
            }

            return true
        }
    }

    private fun loadUrlWithLogging(view: WebView?, url: String) {
        view?.loadUrl(url)
    }
}