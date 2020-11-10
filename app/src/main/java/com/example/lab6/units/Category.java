package com.example.lab6.units;

public enum Category {
    MAIN_DISHES,
    OTHERS,
    SOUP,
    SALAD,
    DELLY,
    BLUE_PLATE;

    int count;

    public int getCount() {
        return count;
    }

    public void setCount() {
        count++;
    }
}
