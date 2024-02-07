package com.example.cookup.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookup.RecipeDetailsActivity;
import com.example.cookup.model.Ingredient;
import com.example.cookup.model.Recipe;
import com.example.cookup.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;

    public RecipeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public RecipeAdapter() {
        // Default constructor without initial data
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        // Extract and set the first two words of the title
        String truncatedTitle = getFirstTwoWords(recipe.getTitle());
        holder.tvRecipeTitle.setText(truncatedTitle);

        holder.tvRecipeDetails.setText("Prep Time: " + recipe.getReadyInMinutes() + " mins");

        // Utilisation de la bibliothèque Glide pour charger l'image depuis l'URL
        Glide.with(holder.itemView.getContext())
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.ic_store)
                .into(holder.ivRecipeImage);

        // Ajoutez un OnClickListener pour l'image
        holder.ivRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ajoutez le code pour ouvrir l'activité des détails ici

                // Créez un Intent pour ouvrir RecipeDetailsActivity
                Intent intent = new Intent(view.getContext(), RecipeDetailsActivity.class);

                // Ajoutez les données nécessaires à l'Intent
                intent.putExtra("recipeTitle", recipe.getTitle());
                intent.putExtra("recipeImage", recipe.getImageUrl());
                // Ajoutez d'autres données si nécessaire

                // Ajoutez la clé "recipeIngredients" avec les ingrédients au intent
                StringBuilder ingredientsStringBuilder = new StringBuilder();
                List<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients != null) {
                    for (Ingredient ingredient : ingredients) {
                        ingredientsStringBuilder.append(ingredient.getName()).append(", ");
                    }
                }
                intent.putExtra("recipeIngredients", ingredientsStringBuilder.toString());

                // Démarrez l'activité des détails avec l'Intent
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    private String getFirstTwoWords(String fullTitle) {
        String[] words = fullTitle.split("\\s+");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            return fullTitle; // If the title has fewer than two words, return the full title
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivRecipeImage;
        private TextView tvRecipeTitle;
        private TextView tvRecipeDetails;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvRecipeDetails = itemView.findViewById(R.id.tvRecipeDetails);
        }
    }
}
