package com.example.flowermanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HttpActivity extends AppCompatActivity {

    JSONObject jsonData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        TextView text_tv = findViewById(R.id.result_text);
        text_tv.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        String res = "";
        try {
            jsonData = new JSONObject(text);
            JSONArray ja = (JSONArray) jsonData.get("suggestions");

            Log.d("testJSON","ja.length : "+Integer.toString(ja.length()));
            for (int i = 0;i < ja.length();i++){
                JSONObject jo = (JSONObject) ja.get(i);
                String msg = "ja["+i+"] : "+jo.get("probability")+" _ "+jo.get("plant_name");
                res += msg+"\n";
                Log.d("testJSON",msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testJSON",e.toString());
        }
        text_tv.setText(res);
    }
}