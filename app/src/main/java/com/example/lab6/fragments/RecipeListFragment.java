package com.example.lab6.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.lab6.R;
import com.example.lab6.activities.MainActivity;
import com.example.lab6.activities.ShowCurrentRecipeActivity;
import com.example.lab6.activities.UpdateRecipeActivity;
import com.example.lab6.recyclerViewPack.RecipeAdapter;
import com.example.lab6.units.ListExistingRecipesManager;
import com.example.lab6.units.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class RecipeListFragment extends Fragment {


    RecipeListFragment currentRecipeListFragment = this;

    View ListFragmentView;

    public RecyclerView recyclerView;
    public RecipeAdapter recipeAdapter;

    public List<Recipe> recipeList = new ArrayList<>();
    Context context;

    DatabaseReference databaseReference;
    String userId;
    int orientation;

    public List<Recipe> recipesFromDb;
    final String nameUser = "User_ID";
    public Map<String,Recipe> forListManager = new HashMap<String, Recipe>() ;



    RecipeAdapter.OnRecipeClickListener onRecipeClickListener;
    RecipeAdapter.OnRecipeLongClickListener onRecipeLongClickListener;

    public void setOnRecipeFragmentClickListener(RecipeAdapter.OnRecipeClickListener
                                                         onRecipeClickListener){
        this.onRecipeClickListener = onRecipeClickListener;
//
    }

    public void setOnRecipeFragmentLongClickListener(RecipeAdapter.OnRecipeLongClickListener
                                                     onRecipeLongClickListener){
        this.onRecipeLongClickListener = onRecipeLongClickListener;
  //
    }



    public RecipeListFragment() {
        // Required empty public constructor
    }

    public void updateFragmentData(){
        if(onRecipeClickListener!=null)
        recipeAdapter.setOnRecipeClickListener(onRecipeClickListener);
        if(onRecipeLongClickListener!=null)
        recipeAdapter.setOnRecipeLongClickListener(onRecipeLongClickListener);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(nameUser,userId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(userId!=null){
        databaseReference = FirebaseDatabase.getInstance().getReference(userId);
        recipesFromDb = new ArrayList<>();
        createListFromDb();

        }
        else if(savedInstanceState!=null){
            userId = savedInstanceState.getString(nameUser);
            databaseReference = FirebaseDatabase.getInstance().getReference(userId);
            recipesFromDb = new ArrayList<>();
            createListFromDb();

        }
    }



    public void createListFromDb(){
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(recipesFromDb.size() > 0 ) recipesFromDb.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Recipe recipe  = snap.getValue(Recipe.class);
                    String str = snap.getKey();
                    forListManager.put(str,recipe);
                    recipesFromDb.add(recipe);
                }

                // creationOfPopupMenu(orientation);
                //  listFragment.recipeAdapter.notifyDataSetChanged()

                recipeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       
    }


    @Override
    public void onStart() {
        super.onStart();
    /*    View view = getView();
        if(view!=null){
            recyclerView = view.findViewById(R.id.myFragmentRecycler);
            recipeAdapter = new RecipeAdapter(recipeList);
            recyclerView.setAdapter(recipeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }*/


    }

    public RecipeListFragment returnActualListFragment(){
        return currentRecipeListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListFragmentView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        recyclerView = ListFragmentView.findViewById(R.id.myFragmentRecycler);
        recipeAdapter = new RecipeAdapter(recipesFromDb);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return ListFragmentView;
    }


    public void getUserIdFromActivity(String userId,int orientation){
        this.userId = userId;
        this.orientation = orientation;
    }


    public void getget(List<Recipe> recipeList){

        recipeAdapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void getDataFromDbActivity(List<Recipe> recipeList,Context context){
        this.recipeList.addAll(recipeList);
        this.context = context;

        View view = getView();
        recyclerView = ListFragmentView.findViewById(R.id.myFragmentRecycler);
        recipeAdapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

      /*  List<Recipe> newRecipeList = recipeList;
        return newRecipeList;*/
    }



}