package com.cn.dafeng.where;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class WsWebview extends AppCompatActivity {

    private static final String TAG = "WsWebview";
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(true);
        webView = new WebView(this);
        setContentView(webView);

        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return false;
                    }

                    @Nullable
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                        if (request.getUrl().toString().startsWith("https://id.oneplus.com/cloud_login.htm")) {

                            String result;
                            try {
                                final URL url = new URL(request.getUrl().toString());
                                final URLConnection urlConnection = url.openConnection();
                                final InputStream inputStream = urlConnection.getInputStream();
                                final int contentLength = urlConnection.getContentLength();
                                byte[] chars = new byte[contentLength];
                                //noinspection ResultOfMethodCallIgnored
                                inputStream.read(chars);
                                result = new String(chars);
                                result = result.replace("</body>", "</body><script>" + getJs() + "</script>");
                            } catch (IOException e) {
                                e.printStackTrace();
                                return super.shouldInterceptRequest(view, request);
                            }
                            if (!result.equals("")) {
                                return new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream(result.getBytes()));

                            }

                        }
                        return super.shouldInterceptRequest(view, request);

                    }
                }
        );

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        settings.setDatabaseEnabled(true);   //开启 database storage API 功能
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl("https://cloud.h2os.com/#/");
    }


    private String getJs() throws IOException {
        Resources resources = this.getResources();
        final InputStream jsinputStream = resources.openRawResource(R.raw.ws);
        int c;
        final StringBuilder stringBuilder = new StringBuilder();
        while ((c = jsinputStream.read()) != -1) {
            stringBuilder.append((char) c);
        }
        return String.valueOf(stringBuilder);
    }

    // 重写方法，防止点击返回按钮直接退回上一活动页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            // 返回上个页面
            webView.goBack();
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
