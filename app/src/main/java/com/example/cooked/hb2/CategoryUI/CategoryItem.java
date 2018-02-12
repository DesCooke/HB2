package com.example.cooked.hb2.CategoryUI;

import java.util.ArrayList;

public class CategoryItem {

    private String name;
    private ArrayList<SubCategoryItem> list = new ArrayList<SubCategoryItem>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SubCategoryItem> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<SubCategoryItem> productList) {
        this.list = productList;
    }

}