package com.example.lab6.recyclerViewPack;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.R;
import com.example.lab6.units.Recipe;
import com.example.lab6.units.RecipeForJson;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeView>
implements Filterable {

    RecipeForJson recipeForJson;
    List<Recipe> recipesList;
    List<Recipe> recipeListCopy;

  //  Context context;

  /*  public RecipeAdapter(List<Recipe> recipeList,Context context){
        this.context = context;
        this.recipesList = recipeList;
        recipeListCopy = new ArrayList<>(recipeForJson.recipeList);
    }
*/
    public RecipeAdapter(List<Recipe> recipeList){
        this.recipesList = recipeList;
        recipeListCopy = new ArrayList<>(recipesList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Recipe> filteredListRecipes = new ArrayList<>();

                if(charSequence == null || charSequence.length() == 0){
                    filteredListRecipes.addAll(recipeListCopy);
                }
                else{
                    String enteringString = charSequence.toString().toLowerCase().trim();

                    for (Recipe recipe: recipeListCopy) {
                        if(recipe.getName().contains(enteringString)){
                            filteredListRecipes.add(recipe);
                        }
                    }
                }

                FilterResults filteredResult = new FilterResults();
                filteredResult.values = filteredListRecipes;
                return filteredResult;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                recipesList.clear();
                recipesList.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class RecipeView extends RecyclerView.ViewHolder{
        TextView name,ingredient,timeCooking,cookingRecipe;
        ImageView photo;
        public RecipeView(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameElementId);
            ingredient =(TextView) itemView.findViewById(R.id.ingredientElementId);
            timeCooking = (TextView) itemView.findViewById(R.id.timeElementId);
            cookingRecipe =(TextView) itemView.findViewById(R.id.cookingRecipeElementId);
            photo = (ImageView) itemView.findViewById(R.id.pictureElementId);
        }

    }

    public interface OnRecipeClickListener{
        void onRecipeClick(Recipe recipe);
    }

    public interface OnRecipeLongClickListener{
        boolean onRecipeLongClick(Recipe recipe,View view);
    }

    public OnRecipeClickListener onRecipeClickListener;
    public OnRecipeLongClickListener onRecipeLongClickListener;


    public void setOnRecipeClickListener(OnRecipeClickListener onRecipeClickListener){
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public void setOnRecipeLongClickListener(OnRecipeLongClickListener onRecipeLongClickListener){
        this.onRecipeLongClickListener = onRecipeLongClickListener;
    }

    @NonNull
    @Override
    public RecipeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_example_template,
                parent,false);
        return new RecipeView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeView holder, int position) {
        ArrayList<Recipe> recipeArrayList = (ArrayList<Recipe>) recipesList;
        Recipe recipe = recipeArrayList.get(position);

        holder.name.setText(recipe.getName());
        holder.cookingRecipe.setText(recipe.getCookingRecipe());
        holder.ingredient.setText(recipe.getIngredient());
        holder.timeCooking.setText(recipe.getTimeCooking());
        //holder.photo.setImageURI(Uri.parse(recipe.getPhoto()));
        holder.photo.setImageResource(R.drawable.recipebook);

        if(onRecipeClickListener!=null){
            holder.itemView.setOnClickListener(view -> onRecipeClickListener.onRecipeClick(recipe));
        }
        if(onRecipeLongClickListener != null){
            holder.itemView.setOnLongClickListener(view ->
                    onRecipeLongClickListener.onRecipeLongClick(recipe,view));
        }

    }

    @Override
    public int getItemCount() {
       // return recipesList.size();
        return  recipesList == null ? 0 : recipesList.size();
    }

}
