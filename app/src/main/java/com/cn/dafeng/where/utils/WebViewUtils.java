package com.cn.dafeng.where.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public class WebViewUtils {

    public static String getJs(Activity activity, String name) throws IOException {
        Resources resources = activity.getResources();
        final int r = resources.getIdentifier(name, "raw", "com.cn.dafeng.where");
        StringBuilder stringBuilder;
        try (InputStream jsinputStream = resources.openRawResource(r)) {
            int c;
            stringBuilder = new StringBuilder();
            while ((c = jsinputStream.read()) != -1) {
                stringBuilder.append((char) c);
            }
        }
        return String.valueOf(stringBuilder);
    }


    public static  void  initWebview(WebView webView){
        WebView.setWebContentsDebuggingEnabled(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        settings.setDatabaseEnabled(true);   //开启 database storage API 功能
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }
}
