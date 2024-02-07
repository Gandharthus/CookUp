package com.example.cookup;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookup.R;
import com.bumptech.glide.Glide;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get data from Intent
        // Get data from Intent
        String recipeTitle = getIntent().getStringExtra("recipeTitle");
        String recipeImageUrl = getIntent().getStringExtra("recipeImage");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");

        // Set data to Views in RecipeDetailsActivity
        TextView tvDetailsTitle = findViewById(R.id.tvDetailsTitle);
        ImageView ivDetailsRecipeImage = findViewById(R.id.ivDetailsRecipeImage);

        TextView tvIngredients = findViewById(R.id.tvIngredients);

        tvDetailsTitle.setText(recipeTitle);
        // Utilisation de la bibliothèque Glide pour charger l'image depuis l'URL
        Glide.with(this)
                .load(recipeImageUrl)
                .placeholder(R.drawable.ic_store)
                .into(ivDetailsRecipeImage);

        tvIngredients.setText(recipeIngredients);

        // You can add more data and views as needed
    }
}
