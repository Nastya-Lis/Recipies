package com.example.lab6.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6.R;
import com.example.lab6.units.Recipe;

public class RecipeDetailFragment extends Fragment {

    TextView nameView,ingredientView,cookingRecipeView, timingView,categoryView;
    ImageView imageView;


    private Recipe recipe;
    private static String RECIPE_ARG = "recipe_arg";

    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(Recipe recipeSend) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_ARG, recipeSend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
            recipe = (Recipe) getArguments().getSerializable(RECIPE_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        nameView = view.findViewById(R.id.nameCurrentRecipeFragmentId);
        ingredientView = view.findViewById(R.id.ingredientCurrentRecipeFragmentId);
        cookingRecipeView = view.findViewById(R.id.cookingCurrentRecipeFragmentId);
        timingView = view.findViewById(R.id.timeCurrentRecipeFragmentId);
        categoryView = view.findViewById(R.id.categoryCurrentRecipeFragmentId);
        imageView = view.findViewById(R.id.photoCurrentRecipeFragmentId);

        try{
            showData(recipe);
        }
        catch (Exception e){
            Toast.makeText(getContext(),"cheto ne to",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void showData(Recipe recipe){
        nameView.setText(recipe.getName());
        ingredientView.setText(recipe.getIngredient());
        categoryView.setText(recipe.getCategory().toString());
        cookingRecipeView.setText(recipe.getCookingRecipe());
        timingView.setText(recipe.getTimeCooking());
        imageView.setImageResource(R.drawable.recipebook);
    }
}