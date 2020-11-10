package com.example.lab6.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab6.R;
import com.example.lab6.units.Recipe;

public class ShowCurrentRecipeActivity extends AppCompatActivity {

    TextView name,time,category,ingredient,cookingRecipe;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_current_recipe);


        name = (TextView) findViewById(R.id.nameCurrentRecipeId);
        time = (TextView) findViewById(R.id.timeCurrentRecipeId);
        category = (TextView) findViewById(R.id.categoryCurrentRecipeId);
        ingredient = (TextView) findViewById(R.id.ingredientCurrentRecipeId);
        cookingRecipe = (TextView) findViewById(R.id.cookingCurrentRecipeId);
        image = (ImageView) findViewById(R.id.photoCurrentRecipeId);

        setDataInCurrentRecipe();

    }

    private Recipe getDataCurrentRecipe(){
        Bundle bundle = getIntent().getExtras();
        Recipe getRecipe = (Recipe) bundle.getSerializable(Recipe.class.getSimpleName());
        return  getRecipe;
    }

    private void setDataInCurrentRecipe(){
      Recipe currentRecipe = getDataCurrentRecipe();
      name.setText(currentRecipe.getName());
      time.setText(currentRecipe.getTimeCooking());
      ingredient.setText(currentRecipe.getIngredient());
      cookingRecipe.setText(currentRecipe.getCookingRecipe());
      category.setText(currentRecipe.getCategory().toString());
      //image.setImageURI(Uri.parse(currentRecipe.getPhoto()));
        image.setImageResource(R.drawable.recipebook);
    }
}