package com.example.flowermanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class HttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        Intent intent = getIntent();
        String text = intent.getStringExtra("text");

        TextView text_tv = findViewById(R.id.result_text);
        text_tv.setMovementMethod(new ScrollingMovementMethod());
        text_tv.setText(text);
    }
}