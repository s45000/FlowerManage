package com.example.flowermanage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView test_view;
    Button take_button;
    Button upload_button;
    Bitmap bitmap;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test_view = (ImageView) findViewById(R.id.test_view);
        take_button = (Button) findViewById(R.id.take_picture_button);
        upload_button = (Button) findViewById(R.id.upload_picture_button);

        take_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);
            }
        });
        upload_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultUpload.launch(intent);
            }
        });
    }
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        bitmap = (Bitmap) extras.get("data");
                        test_view.setImageBitmap(bitmap);
                        new Thread(() -> {
                            try {
                                    httpRequest();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("testAPI",e.toString());
                            }
                        }).start();
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> activityResultUpload = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            test_view.setImageBitmap(bitmap);
                            new Thread(() -> {
                                try {
                                    httpRequest();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("testAPI",e.toString());
                                }
                            }).start();
                        }catch ( Exception e ){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    void httpRequest() throws Exception {
        String apiKey = "bW7pPK5klYtfb1OYpdyZN1hgCPl59I8C2rAOl4x5nKUwHM9rHE";

        // read image from local file system and encode
        String [] flowers = new String[] {"../img/photo1.jpg", "../img/photo2.jpg", "../img/photo3.jpg"};


        JSONObject data = new JSONObject();
        data.put("api_key", apiKey);

        // bitmap >> base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bImage = baos.toByteArray();
        String base64 = Base64.encodeToString(bImage, Base64.DEFAULT);

        // add images
        JSONArray images = new JSONArray();
        images.put(base64);
        data.put("images", images);

        // add modifiers
        // modifiers info: https://github.com/flowerchecker/Plant-id-API/wiki/Modifiers
        JSONArray modifiers = new JSONArray()
                .put("crops_fast")
                .put("similar_images");
        data.put("modifiers", modifiers);

        // add language
        data.put("plant_language", "en");

        // add plant details
        // more info here: https://github.com/flowerchecker/Plant-id-API/wiki/Plant-details
        JSONArray plantDetails = new JSONArray()
                .put("common_names")
                .put("url")
                .put("name_authority")
                .put("wiki_description")
                .put("taxonomy")
                .put("synonyms");
        data.put("plant_details", plantDetails);

        sendPostRequest("https://api.plant.id/v2/identify", data);
    }
    public String sendPostRequest(String urlString, JSONObject data) throws Exception {

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        OutputStream os = con.getOutputStream();
        os.write(data.toString().getBytes());
        os.close();

        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        //String response = new String(is.readAllBytes());

        Log.d("testAPI","test");
        Log.d("testAPI","Response code : " + con.getResponseCode());
        Log.d("testAPI","Response : " + response);
        con.disconnect();

        //String response = "{\"id\": 57994756, \"custom_id\": null, \"meta_data\": {\"latitude\": null, \"longitude\": null, \"date\": \"2022-08-12\", \"datetime\": \"2022-08-12\"}, \"uploaded_datetime\": 1660290841.155482, \"finished_datetime\": 1660290841.6483, \"images\": [{\"file_name\": \"2be315486c78438dadd9c73ef57d0a48.jpg\", \"url\": \"https://plant.id/media/images/2be315486c78438dadd9c73ef57d0a48.jpg\"}], \"suggestions\": [{\"id\": 335650777, \"plant_name\": \"Tulipa\", \"plant_details\": {\"common_names\": [\"Tulip\"], \"url\": \"https://en.wikipedia.org/wiki/Tulipa\", \"name_authority\": \"Tulipa L.\", \"wiki_description\": {\"value\": \"Tulips (Tulipa) form a genus of spring-blooming perennial herbaceous bulbiferous geophytes (having bulbs as storage organs). The flowers are usually large, showy and brightly colored, generally red, pink, yellow, or white (usually in warm colors). They often have a different colored blotch at the base of the tepals (petals and sepals, collectively), internally. Because of a degree of variability within the populations, and a long history of cultivation, classification has been complex and controversial. The tulip is a member of the lily family, Liliaceae, along with 14 other genera, where it is most closely related to Amana, Erythronium and Gagea in the tribe Lilieae. There are about 75 species, and these are divided among four subgenera. The name \"tulip\" is thought to be derived from a Persian word for turban, which it may have been thought to resemble. Tulips originally were found in a band stretching from Southern Europe to Central Asia, but since the seventeenth century have become widely naturalised and cultivated (see map). In their natural state they are adapted to steppes and mountainous areas with temperate climates. Flowering in the spring, they become dormant in the summer once the flowers and leaves die back, emerging above ground as a shoot from the underground bulb in early spring.\nOriginally growing wild in the valleys of the Tian Shan Mountains, tulips were cultivated in Constantinople as early as 1055. By the 15th century, tulips were among the most prized flowers; becoming the symbol of the Ottomans. While tulips had probably been cultivated in Persia from the tenth century, they did not come to the attention of the West until the sixteenth century, when Western diplomats to the Ottoman court observed and reported on them. They were rapidly introduced into Europe and became a frenzied commodity during Tulip mania. Tulips were frequently depicted in Dutch Golden Age paintings, and have become associated with the Netherlands, the major producer for world markets, ever since. In the seventeenth century Netherlands, during the time of the Tulip mania, an infection of tulip bulbs by the tulip breaking virus created variegated patterns in the tulip flowers that were much admired and valued. While truly broken tulips do not exist anymore, the closest available specimens today are part of the group known as the Rembrandts \u2013 so named because Rembrandt painted some of the most admired breaks of his time.\nBreeding programs have produced thousands of hybrid and cultivars in addition to the original species (known in horticulture as botanical tulips). They are popular throughout the world, both as ornamental garden plants and as cut flowers.\", \"citation\": \"https://en.wikipedia.org/wiki/Tulipa\", \"license_name\": \"CC BY-SA 3.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Magnoliopsida\", \"family\": \"Liliaceae\", \"genus\": \"Tulipa\", \"kingdom\": \"Plantae\", \"order\": \"Liliales\", \"phylum\": \"Magnoliophyta\"}, \"synonyms\": [\"Eduardoregelia\", \"Liriactis\", \"Liriopogon\", \"Orithyia\", \"Podonix\"], \"language\": \"en\", \"scientific_name\": \"Tulipa\", \"structured_name\": {\"genus\": \"tulipa\"}}, \"probability\": 0.39336888522562025, \"confirmed\": false, \"similar_images\": [{\"id\": \"d61f42e90d6031e5ec645372d1353efd\", \"similarity\": 10.480458771380238, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/d61/f42e90d6031e5ec645372d1353efd.jpg\"}";
        Intent intent = new Intent(this, HttpActivity.class);
        //입력한 input값을 intent로 전달한다.
        intent.putExtra("text", response.toString());
        //액티비티 이동
        startActivity(intent);

        //return response;
        return "";
    }
}