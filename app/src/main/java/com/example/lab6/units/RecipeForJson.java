package com.example.lab6.units;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeForJson implements Serializable {
  public List<Recipe> recipeList = new ArrayList<Recipe>();
  //хз надо ли было иницилизировать лист рецептов ци не
  public RecipeForJson(){

  }
  public RecipeForJson(List<Recipe> list){
    recipeList = list;
  }
}
