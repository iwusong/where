package com.cn.dafeng.where;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.cn.dafeng.where.utils.WebViewUtils.getJs;
import static com.cn.dafeng.where.utils.WebViewUtils.initWebview;

public class WebView2 extends AppCompatActivity {
    private WebView webView;

    private final Activity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);
        initWebview(webView);
        webView.setWebViewClient(
                new WebViewClient(){

                    @Nullable
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                        Log.d("shouldInterceptRequest", "shouldInterceptRequest: "+request.getUrl().getHost());
                        return super.shouldInterceptRequest(view, request);

                    }
                }
        );
        webView.loadUrl("https://yun.vivo.com.cn/#/welcome");


    }
}