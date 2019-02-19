package jp.techacademy.jun.aoki.myoriginal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient



class HomeFragment : Fragment() {

    private var webView:WebView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val v =inflater.inflate(R.layout.fragment_home, container, false)



        webView = v.findViewById(R.id.web_view)

        //Web内のリンクをタップしたときなどに標準ブラウザを起動しないように設定
        webView!!.setWebViewClient(WebViewClient())

        //jacascriptを許可する
        webView!!.getSettings().javaScriptEnabled = true

        // Google表示
        webView!!.loadUrl("https://www.wsl.waseda.jp/syllabus/JAA101.php")

        return  v
    }
}