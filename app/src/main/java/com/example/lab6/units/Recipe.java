package com.example.lab6.units;

import java.io.Serializable;
import java.sql.Time;

public class Recipe implements Serializable {
    String name;
    String ingredient;
    String photo;
    String cookingRecipe;
    String timeCooking;
    Category category;

    public Recipe(){
        this.category = Category.OTHERS;
        this.name = "Default";
        this.ingredient="Default";
        this.photo = null;
        this.timeCooking = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCookingRecipe() {
        return cookingRecipe;
    }

    public void setCookingRecipe(String cookingRecipe) {
        this.cookingRecipe = cookingRecipe;
    }
    public String getTimeCooking() {
        return timeCooking;
    }

    public void setTimeCooking(String timeCooking) {
        this.timeCooking = timeCooking;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
