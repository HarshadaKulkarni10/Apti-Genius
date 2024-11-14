package com.example.apti_geniusadmin.Models;

public class CategoryModel {

    private  String categoryName,key;
    int setNum;

    public CategoryModel(String categoryName,  String key, int setNum) {
        this.categoryName = categoryName;

        this.key = key;
        this.setNum = setNum;
    }

    public CategoryModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }




    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }
}
