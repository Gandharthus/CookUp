package com.example.cookup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookup.adapter.RecipeAdapter;
import com.example.cookup.model.Recipe;
import com.example.cookup.model.RecipeResponse;
import com.example.cookup.service.RecipeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceuilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceuilFragment extends Fragment {
    private List<Recipe> mexicanRecipes = new ArrayList<>();
    private List<Recipe> chineseRecipes = new ArrayList<>();
    private RecipeAdapter mexicanRecipeAdapter;
    private RecipeAdapter chineseRecipeAdapter;
    private RecyclerView recyclerViewMexican;
    private RecyclerView recyclerViewChinese;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_acceuil, container, false);

        recyclerViewMexican = view.findViewById(R.id.mexican_recipe_recycler_view);
        recyclerViewChinese = view.findViewById(R.id.chinese_recipe_recycler_view);

        // Configure the RecyclerViews for Mexican and Chinese recipes
        recyclerViewMexican.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewChinese.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mexicanRecipeAdapter = new RecipeAdapter();
        chineseRecipeAdapter = new RecipeAdapter();

        recyclerViewMexican.setAdapter(mexicanRecipeAdapter);
        recyclerViewChinese.setAdapter(chineseRecipeAdapter);

        // Fetch and update recipes for Mexican and Chinese cuisines
        fetchRandomRecipes("mexican", 4, mexicanRecipeAdapter, mexicanRecipes);
        fetchRandomRecipes("chinese", 4, chineseRecipeAdapter, chineseRecipes);

        return view;
    }

    private void fetchRandomRecipes(String cuisine, int count, RecipeAdapter adapter, List<Recipe> recipeList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);

        Call<RecipeResponse> call = recipeService.getRandomRecipesTheme("d9e92c5ed6cf4f4dae6c299d6843241c", count, cuisine);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body().getRecipes();
                    // Update the RecyclerView with the fetched recipes
                    Log.d("RecipeAPI", "Number of recipes received for " + cuisine + ": " + recipes.size());
                    updateRecipeList(recipeList, recipes);
                    adapter.setRecipes(recipes);
                    Log.d("RecipeAPI", "REQUEST was successful to " + call.request().url());
                } else {
                    // Handle error
                    Log.e("RecipeAPI", "Error in API response for " + cuisine + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
                Log.e("RecipeAPI", "Error for " + cuisine + ": " + t.getMessage());
            }
        });
    }

    private void updateRecipeList(List<Recipe> recipeList, List<Recipe> recipes) {
        recipeList.clear();
        recipeList.addAll(recipes);
    }
}
