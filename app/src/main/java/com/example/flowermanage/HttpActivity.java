package com.example.flowermanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HttpActivity extends AppCompatActivity {

    JSONObject jsonData = null;
    ArrayList<flowerData> flowerDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        flowerDataList = new ArrayList<flowerData>();
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        try {
            jsonData = new JSONObject(text);
            JSONArray suggestions = (JSONArray) jsonData.get("suggestions");

            for (int i = 0;i < suggestions.length();i++){
                JSONObject jo = (JSONObject) suggestions.get(i);

                JSONObject plant_details = (JSONObject) jo.get("plant_details");
                JSONObject wiki_description = (JSONObject) plant_details.get("wiki_description");
                JSONObject taxonomy = (JSONObject) plant_details.get("taxonomy");

                JSONArray similar_images = (JSONArray) jo.get("similar_images");
                JSONObject similar_image = (JSONObject) similar_images.get(0);

                flowerDataList.add(new flowerData("정확도 : "+String.format("%.2f", (Double)jo.get("probability")),""+jo.get("plant_name"),
                        ""+taxonomy,""+similar_image.get("url"),""+wiki_description));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testRES",e.toString());
        }

        ListView listView = (ListView)findViewById(R.id.resList);
        final result_list_adapter myAdapter = new result_list_adapter(this,flowerDataList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getDetailData(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}