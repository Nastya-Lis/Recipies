package com.example.lab6.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.lab6.R;
import com.example.lab6.fragments.RecipeDetailFragment;
import com.example.lab6.fragments.RecipeListFragment;
import com.example.lab6.recyclerViewPack.RecipeAdapter;
import com.example.lab6.units.Category;
import com.example.lab6.units.ListExistingRecipesManager;
import com.example.lab6.units.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //для фрагментов
    FrameLayout listFragmentLayout;
    FrameLayout detailFragmentLayout;

    private RecipeListFragment listFragment;
    private RecipeDetailFragment detailFragment;

    private FragmentManager fragmentManager;

    int orientation;

    //для бд
    DatabaseReference databaseReference;

    String userId;
    //ресайкл
    RecipeAdapter recipeAdapter;
    RecyclerView recyclerView;
    PopupMenu popupMenu;


    List<Recipe> recipesFromDb;
    final String nameUser = "User_ID";
    Map<String,Recipe> forListManager = new HashMap<String, Recipe>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        userId = getDataFromPrevActivity();

            // String userIdFromDataBase;
            // вроде как создается ссылка на корень бд
   //         databaseReference = FirebaseDatabase.getInstance().getReference(userId);

            //if(databaseReference.child(userId) == null) {
            //     String userIdFromDataBase = databaseReference.push().getKey();
            //   }

            //создаем узел на определенного юзера
            //    databaseReference.child(userIdFromDataBase).setValue(userId);


            // чтение данных из бд
   //         recipesFromDb = new ArrayList<>();
          /*  recyclerView = (RecyclerView) findViewById(R.id.myRecycler);

            recyclerView.setAdapter(recipeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
            listFragmentLayout = findViewById(R.id.recipe_list_case);
            detailFragmentLayout = findViewById(R.id.recipe_details_case);

            fragmentManager = getSupportFragmentManager();
            listFragment = new RecipeListFragment();


        orientation = getResources().getConfiguration().orientation;

            listFragment.getUserIdFromActivity(userId,orientation);
     //       createListFromDb();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.recipe_list_case,listFragment);
        transaction.commit();

       //     recipeAdapter = new RecipeAdapter(recipesFromDb);

            //checkOrientationConfig(orientation);
       //     creationOfPopupMenu(orientation);
    }

    private void createListFromDb(){
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


          //      listFragment.getDataFromDbActivity(recipesFromDb,MainActivity.this);
               // creationOfPopupMenu(orientation);
              //  listFragment.recipeAdapter.notifyDataSetChanged();
   //             recipeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateListFragment(){
        listFragment = listFragment.returnActualListFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.recipe_list_case,listFragment);
        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        creationOfPopupMenu(orientation);
    }

    private void creationOfPopupMenu(int orientation) {

        //createListFromDb();
        //listFragment.getget(recipesFromDb);

    //    listFragment = listFragment.returnActualListFragment();
      //  updateListFragment();
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            detailFragmentLayout.setVisibility(View.GONE);
            listFragment.setOnRecipeFragmentClickListener(recipe -> {
                Intent intent = new Intent(this, ShowCurrentRecipeActivity.class);
                intent.putExtra(Recipe.class.getSimpleName(), recipe);
                startActivity(intent);
            });
            listFragment.updateFragmentData();
        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            detailFragmentLayout.setVisibility(View.VISIBLE);
            //создать транзацию детального фрагмента и передать ему данные нажатого лист фрагмента

            listFragment.setOnRecipeFragmentClickListener(recipe -> {
                detailFragment = RecipeDetailFragment.newInstance(recipe);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.recipe_details_case,detailFragment).
                        addToBackStack(null);
                fragmentTransaction.commit();
            });
        }

        listFragment.setOnRecipeFragmentLongClickListener((recipe, view) -> {
            popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.editId:
                             editRecipe(recipe);
                            break;
                        case R.id.deleteId:
                            deleteRecipe(recipe);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
            return true;
        });

        listFragment.updateFragmentData();
    //    recyclerView.setAdapter(recipeAdapter);

    }

    private void editRecipe(Recipe recipe){
        Intent intent = new Intent(this,UpdateRecipeActivity.class);
        String currentKey = "";
        for (String key: listFragment.forListManager.keySet()) {
            if( recipe == listFragment.forListManager.get(key))
            currentKey = key;
        }
        intent.putExtra(Recipe.class.getSimpleName(),recipe);
        intent.putExtra(nameUser,userId);
        intent.putExtra("currentKey",currentKey);
        startActivity(intent);
    }

    private void deleteRecipe(Recipe recipe){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Warning!").
                setMessage("Are you really want to delete this element?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            ListExistingRecipesManager listExistingRecipesManager =
                                    new ListExistingRecipesManager(recipesFromDb,listFragment.forListManager,userId);
                            listExistingRecipesManager.removeElementV2(recipe);
                        }
                        catch (Exception e){

                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        android.app.AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_bar, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recipeAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.addIcon:
                Intent intent = new Intent(this,AddRecipeActivity.class);
                intent.putExtra(nameUser,userId);
                startActivity(intent);
                break;

            case R.id.sorting_by_default:
                listFragment.createListFromDb();
                listFragment.recipeAdapter = new RecipeAdapter(listFragment.recipesFromDb);
                listFragment.recyclerView.setAdapter(listFragment.recipeAdapter);
                listFragment.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                reCallMethod();
                break;

            case R.id.sorting_by_name:
                listFragment.createListFromDb();
                listFragment.recipesFromDb.
                        sort((recipe1,recipe2) -> recipe1.getName().toUpperCase().
                                compareTo(recipe2.getName().toUpperCase()));

                RecipeAdapter recipeAdapterFoName = new RecipeAdapter(listFragment.recipesFromDb);
                listFragment.recyclerView.setAdapter(recipeAdapterFoName);
                listFragment.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                reCallMethod();
                break;

            case R.id.sorting_by_category:
                listFragment.createListFromDb();
                listFragment.recipesFromDb.
                        sort((recipe1,recipe2) -> recipe1.getCategory().
                                compareTo(recipe2.getCategory()));

                RecipeAdapter recipeAdapterForCategory = new RecipeAdapter(listFragment.recipesFromDb);
                listFragment.recyclerView.setAdapter(recipeAdapterForCategory);
                listFragment.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                reCallMethod();
                break;
            case R.id.up:
                listFragment.recyclerView.scrollToPosition(0);
                break;
            case R.id.logOutIcon:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Warning!").setMessage("Do you really want to exit from account")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               FirebaseAuth.getInstance().signOut();
                               Intent intent1 = new
                                       Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }).setNegativeButton("No", (dialogInterface, i) -> { });
                alertDialog.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getDataFromPrevActivity(){
        try{
        Bundle bundle = getIntent().getExtras();
        String userCurrentId = bundle.get("currentUser").toString();
        return userCurrentId;
        }
        catch (Exception e){
            return null;
        }
    }

    private void upScroll(){

    }

    private void reCallMethod(){
        creationOfPopupMenu(orientation);
    }

}