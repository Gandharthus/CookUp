package com.example.cookup.service;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.cookup.model.Recipe;
import com.example.cookup.model.RecipeResponse;

public interface RecipeService{
    @GET("recipes/random")
    Call<RecipeResponse> getRandomRecipes(
            @Query("apiKey") String apiKey,
            @Query("number") int number
    );

    @GET("recipes/random")
    Call<RecipeResponse> getRandomRecipesTheme(
            @Query("apiKey") String apiKey,
            @Query("number") int number,
            @Query("include-tags") String tags
    );

    @GET("recipes/findByIngredients")
    Call<List<Recipe>> searchRecipesByIngredients(
            @Query("apiKey") String apiKey,
            @Query("ingredients") String ingredients,
            @Query("number") int number
    );
}
