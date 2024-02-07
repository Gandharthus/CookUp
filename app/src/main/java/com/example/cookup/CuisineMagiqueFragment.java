package com.example.cookup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookup.adapter.RecipeAdapter;
import com.example.cookup.model.Recipe;
import com.example.cookup.model.RecipeResponse;
import com.example.cookup.service.RecipeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;


import androidx.recyclerview.widget.LinearLayoutManager;

public class CuisineMagiqueFragment extends Fragment {
    private List<Recipe> recipes = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private RecyclerView recyclerViewRecipes;
    private EditText etIngredients;
    private Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuisine_magique, container, false);

        recyclerViewRecipes = view.findViewById(R.id.recipe_recycler_view);
        etIngredients = view.findViewById(R.id.editTextIngredients);
        btnSearch = view.findViewById(R.id.buttonSearch);

        // Configure the RecyclerView
        recyclerViewRecipes.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeAdapter = new RecipeAdapter();
        recyclerViewRecipes.setAdapter(recipeAdapter);

        // Set up the search functionality
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredIngredients = etIngredients.getText().toString().trim();
                if (!enteredIngredients.isEmpty()) {
                    // Clear previous recipes
                    recipes.clear();
                    recipeAdapter.notifyDataSetChanged();
                    // Fetch recipes based on entered ingredients
                    fetchRecipesByIngredients(enteredIngredients);
                }
            }
        });

        return view;
    }

    private void fetchRecipesByIngredients(String ingredients) {
        ProgressBar loadingIndicator = getView().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        RecipeService recipeService = RetrofitClientInstance.getRetrofitInstance().create(RecipeService.class);
        Call<RecipeResponse> call = recipeService.getRandomRecipesTheme("d9e92c5ed6cf4f4dae6c299d6843241c", 4, ingredients);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                loadingIndicator.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    List<Recipe> fetchedRecipes = response.body().getRecipes();
                    if (fetchedRecipes != null) {
                        recipes.addAll(fetchedRecipes);
                        recipeAdapter.setRecipes(recipes);
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                // Handle failure
            }
        });
    }
}