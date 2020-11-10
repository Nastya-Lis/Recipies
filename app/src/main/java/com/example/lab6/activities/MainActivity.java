package com.example.lab6.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.lab6.R;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    DatabaseReference databaseReference;

    String userId;
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
            databaseReference = FirebaseDatabase.getInstance().getReference(userId);

            //if(databaseReference.child(userId) == null) {
            //     String userIdFromDataBase = databaseReference.push().getKey();
            //   }



            //создаем узел на определенного юзера
            //    databaseReference.child(userIdFromDataBase).setValue(userId);


            // чтение данных из бд
            recipesFromDb = new ArrayList<>();


            recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
            recipeAdapter = new RecipeAdapter(recipesFromDb);
            recyclerView.setAdapter(recipeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            createListFromDb();

            creationOfPopupMenu();

       /* Recipe recipe = new Recipe();
        recipe.setName("Pizza");
        recipe.setCategory(Category.BLUE_PLATE);
        recipe.setCookingRecipe("take cheese and pepper");
        recipe.setIngredient("cheese \n pepper");
        recipe.setTimeCooking("20.35");
        recipe.setPhoto("content://media/external/images/media/62");

        Recipe recipe1 = new Recipe();
        recipe1.setName("Pasta");
        recipe1.setCategory(Category.MAIN_DISHES);
        recipe1.setCookingRecipe("take macaroni");
        recipe1.setIngredient("macaroni");
        recipe1.setTimeCooking("10.00");
        recipe1.setPhoto("content://media/external/images/media/62");


        String recipeFirstKey = databaseReference.push().getKey();
        String recipeSecondKey = databaseReference.push().getKey();

        databaseReference.child(recipeFirstKey).setValue(recipe);
        databaseReference.child(recipeSecondKey).setValue(recipe1);

*/

//        recyclerView = findViewById(R.id.myRecycler);
//        recyclerView.setAdapter(new RecyclerView.Adapter() {
//            Recipe recipe;
//
//            @NonNull
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 0;
//            }
//        });
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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

                recipeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void creationOfPopupMenu() {

        recipeAdapter.setOnRecipeClickListener(recipe -> {
            Intent intent = new Intent(this, ShowCurrentRecipeActivity.class);
            intent.putExtra(Recipe.class.getSimpleName(), recipe);
            startActivity(intent);
        });

        recipeAdapter.setOnRecipeLongClickListener((recipe, view) -> {
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

        recyclerView.setAdapter(recipeAdapter);

    }

    private void editRecipe(Recipe recipe){
        Intent intent = new Intent(this,UpdateRecipeActivity.class);
        String currentKey = "";
        for (String key: forListManager.keySet()) {
            if( recipe == forListManager.get(key))
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
                                    new ListExistingRecipesManager(recipesFromDb,forListManager,userId);
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
                createListFromDb();
                recipeAdapter = new RecipeAdapter(recipesFromDb);
                recyclerView.setAdapter(recipeAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;

            case R.id.sorting_by_name:
                createListFromDb();
                recipesFromDb.
                        sort((recipe1,recipe2) -> recipe1.getName().toUpperCase().
                                compareTo(recipe2.getName().toUpperCase()));

                RecipeAdapter recipeAdapterFoName = new RecipeAdapter(recipesFromDb);
                recyclerView.setAdapter(recipeAdapterFoName);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;

            case R.id.sorting_by_category:
                createListFromDb();
                recipesFromDb.
                        sort((recipe1,recipe2) -> recipe1.getCategory().
                                compareTo(recipe2.getCategory()));

                RecipeAdapter recipeAdapterForCategory = new RecipeAdapter(recipesFromDb);
                recyclerView.setAdapter(recipeAdapterForCategory);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void stupidAlertDialog(List<Recipe> recipes){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        StringBuilder stringBuilder = new StringBuilder();
        if(recipes.size()!=0)
        for (Recipe recipe: recipes) {
            stringBuilder.append(recipe.getName() + "\n");
        }

        alertDialog.setMessage("ammount" + recipes.size());
       // alertDialog.setMessage("t:" + stringBuilder /*+ " " + recipes.get(0).getName()*/);
        alertDialog.create().show();
    }
}