package com.cn.dafeng.where;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    static String id = "cfc5d88d2824c3bd";
    String androidId;
    MyApplication app;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (id.equals(androidId)) {
            biometricPrompt.authenticate(promptInfo);
        } else if (!BuildConfig.DEBUG) {
            showCopy_androidId_Dialog();
        } else {
            goW();
        }
    }

    private void init() {
        app = (MyApplication) getApplication();
        androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        System.out.println(androidId);
        initBiometricPromptAndPromptInfo();
        //        日志
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppReportDelay(0);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), true);
        CrashReport.initCrashReport(getApplicationContext(), "eca0dff366", true);

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
                goW();
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
                .setMessage(androidId)
                .setPositiveButton("复制", (dialog1, which) -> {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("id", androidId);
                    clipboardManager.setPrimaryClip(mClipData);
                    Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                    finish();
                }).create();
        dialog.show();
    }

    private void goW() {
        app.setAuthorized(true);
        startActivity(new Intent(MainActivity.this, WsWebview.class));
        finish();
    }
}
