package com.example.cookup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class foodDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_food_details);
        final TextView productNameView = findViewById(R.id.productNameView);
        final ImageView productImageView = findViewById(R.id.productImageView);
        final TextView energyView = findViewById(R.id.energyView);
        final TextView proteinView = findViewById(R.id.proteinView);
        final TextView fibreView = findViewById(R.id.fibresView);
        final TextView carbsView = findViewById(R.id.carbsView);
        final TextView fatView = findViewById(R.id.fatView);
        final Button backBtn = findViewById(R.id.button2);
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);

        backBtn.setOnClickListener(view -> startActivity(intent2));



        Intent intent = getIntent();
        String barcodeValue = intent.getStringExtra("barcodeValue");


        RequestQueue queue = Volley.newRequestQueue(foodDetails.this);
        String url = "https://world.openfoodfacts.org/api/v0/product/".concat(barcodeValue).concat(".json");
        productNameView.setText(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        if(response.getInt("status")==0){
                            productNameView.setText("Product Not Found !");
                        }else{
                            JSONObject product = response.getJSONObject("product");
                            productNameView.setText(product.getString("product_name"));
                            Picasso.get().load(product.getString("image_front_url")).into(productImageView);
                            JSONObject nutriments = product.getJSONObject("nutriments");
                            energyView.setText(nutriments.getString("energy-kcal_100g"));
                            proteinView.setText(nutriments.getString("proteins_100g"));
                            fibreView.setText(nutriments.getString("fiber_100g"));
                            carbsView.setText(nutriments.getString("carbohydrates_100g"));
                            fatView.setText(nutriments.getString("fat_100g"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        productNameView.setText(error.toString());
                    }
        });
        queue.add(request);
    }
}