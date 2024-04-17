package com.example.poliapp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.webkit.WebViewClient
class web : Fragment() {

    companion object {
        fun newInstance() = web()
    }
    private lateinit var webView: WebView
    private lateinit var editText : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_web, container, false)
        editText= view.findViewById(R.id.inputWebView)
        webView = view.findViewById(R.id.webView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val btnSave = view?.findViewById<Button>(R.id.btnWebView)
        btnSave?.setOnClickListener {
            val url = editText.text.toString()

            // Cargar la URL en el WebView
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url)
        }
    }

}