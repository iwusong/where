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

import java.util.concurrent.Executor;

import static android.provider.Settings.Secure;


public class MainActivity extends AppCompatActivity {

    static String id = "dfc546beaf51b353";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,
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
                MyApplication app = (MyApplication) getApplication();
                app.setFlag(true);
                startActivity(new Intent(MainActivity.this, WsWebview.class));
                finish();
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

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("身份验证")
                .setSubtitle("使用您的生物识别凭证登录")
                .setNegativeButtonText("Use account password")
                .build();

        final String androidId;
        androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        System.out.println(androidId);
        if (id.equals(androidId)) {
            biometricPrompt.authenticate(promptInfo);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("当前设备id")
                    .setMessage(androidId)
                    .setPositiveButton("复制", (dialog1, which) -> {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("id", androidId);
                        clipboardManager.setPrimaryClip(mClipData);
                        Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                    }).create();
            dialog.show();
        }
    }
}
