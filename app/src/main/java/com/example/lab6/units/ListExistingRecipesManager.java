package com.example.lab6.units;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class ListExistingRecipesManager {

    private List<Recipe> recipeList;
    private Map<String,Recipe> map;

    DatabaseReference db;


    public ListExistingRecipesManager(List<Recipe> recipeList,Map<String,Recipe> map,String userCurrentId){
        this.recipeList = recipeList;
        db = FirebaseDatabase.getInstance().getReference(userCurrentId);
        this.map = map;
    }


    public void removeElementV2(Recipe recipeSend){
    String currentKey = "";
        for (String key:map.keySet()) {
            recipeSend = map.get(key);
            currentKey = key;
        }
       /* Map<String,Recipe> deletedMap = new HashMap<>();
        deletedMap.put(currentKey,recipeSend);*/
        db.child(currentKey).removeValue();
    }
}
