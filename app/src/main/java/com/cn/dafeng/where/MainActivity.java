package com.cn.dafeng.where;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.concurrent.Executor;

import static android.provider.Settings.Secure;


public class MainActivity extends AppCompatActivity {

    static String allowedId = "cfc5d88d2824c3bd";
    //    static String id = "dfc546beaf51b353";
    String deviceId;
    MyApplication app;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (BuildConfig.DEBUG) {
            authorizationSucceeded();
        } else if (allowedId.equals(deviceId)) {
            biometricPrompt.authenticate(promptInfo);
        } else {
            showCopy_androidId_Dialog();
        }
    }

    private void init() {
        app = (MyApplication) getApplication();
        deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        System.out.println(deviceId);
        initBiometricPromptAndPromptInfo();
        //        日志
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppReportDelay(0);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), true);
        CrashReport.initCrashReport(getApplicationContext(), "eca0dff366", true);


        Button b1 = findViewById(R.id.button1);
        b1.setOnClickListener(
                (view) -> startActivity(new Intent(MainActivity.this, WsWebview.class))
        );
        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(
                (view)->startActivity(new Intent(MainActivity.this, WebView2.class))
        );
    }

    /**
     * 初始化 指纹识别
     */
    private void initBiometricPromptAndPromptInfo() {
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                finish();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authorizationSucceeded();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("身份验证")
                .setSubtitle("使用您的生物识别凭证登录")
                .setNegativeButtonText("不验证并退出")
                .build();

    }

    /**
     * 去 webView
     */

    private void showCopy_androidId_Dialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("当前设备id")
                .setMessage(deviceId)
                .setPositiveButton("复制", (dialog1, which) -> {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("id", deviceId);
                    clipboardManager.setPrimaryClip(mClipData);
                    Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                    finish();
                }).create();
        dialog.show();
    }

    private void authorizationSucceeded() {

        app.setAuthorized(true);
//        startActivity(new Intent(MainActivity.this, WsWebview.class));
//        finish();
    }
}
