package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.cooked.hb2.CategoryUI.CategoryListAdapter;
import com.example.cooked.hb2.CategoryUI.SubCategoryItem;
import com.example.cooked.hb2.CategoryUI.CategoryItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class activityCategory extends AppCompatActivity {

    private LinkedHashMap<String, CategoryItem> subjects = new LinkedHashMap<String, CategoryItem>();
    private ArrayList<CategoryItem> deptList = new ArrayList<CategoryItem>();

    private CategoryListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Categories");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // add data for displaying in expandable list view
        loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.categoryListView);
        // create the adapter by passing your ArrayList data
        listAdapter = new CategoryListAdapter(activityCategory.this, deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                CategoryItem headerInfo = deptList.get(groupPosition);
                //get the child info
                SubCategoryItem detailInfo =  headerInfo.getProductList().get(childPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getName()
                        + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                CategoryItem headerInfo = deptList.get(groupPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getName(),
                        Toast.LENGTH_LONG).show();

                return false;
            }
        });


    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        addCategory("Income","Salary");
        addCategory("Income","Shares");
        addCategory("Bills","Mortgage");
        addCategory("Bills","Car Loan");
        addCategory("Bills","Car Tax");
        addCategory("Bills","Anti Virus");
        addCategory("Bills","Council Tax");
        addCategory("Bills","Food");
        addCategory("Bills","Gas and Electric");
        addCategory("Bills","Gym");
        addCategory("Bills","Lottery");
        addCategory("Bills","Mobile Phone");
        addCategory("Bills","Sky");
        addCategory("Bills","TV Licence");
        addCategory("Bills","Water");
        addCategory("Insurance","Buildings and Contents");
        addCategory("Insurance","Car Insurance");
        addCategory("Insurance","Critical Illness 1");
        addCategory("Insurance","Critical Illness 2");
        addCategory("Insurance","Payment Protection");
        addCategory("Savings","General");
        addCategory("Savings","Family");
        addCategory("Savings","Long Term");
        addCategory("Savings","Sam and Holly");
    }

    //here we maintain our products in various departments
    private int addCategory(String category, String subcategory){

        int groupPosition = 0;

        //check the hash map if the group already exists
        CategoryItem headerInfo = subjects.get(category);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new CategoryItem();
            headerInfo.setName(category);
            subjects.put(category, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<SubCategoryItem> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        SubCategoryItem detailInfo = new SubCategoryItem();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(subcategory);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }
}
