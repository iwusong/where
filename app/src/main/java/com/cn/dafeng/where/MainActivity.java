package com.cn.dafeng.where;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button but = findViewById(R.id.button);
        but.setOnClickListener(
                view ->
                        startActivity(new Intent(this, WsWebview.class))
        );
    }
}
