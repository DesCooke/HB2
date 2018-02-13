package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.cooked.hb2.CategoryUI.CategoryListAdapter;
import com.example.cooked.hb2.CategoryUI.SubCategoryItem;
import com.example.cooked.hb2.CategoryUI.CategoryItem;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class activityCategory extends AppCompatActivity{

    public ArrayList<RecordCategory> mCategoryList;
    private CategoryListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_category_list);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    
            setTitle("Categories");
    
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(activityCategory.this, activityCategoryItem.class);
                    intent.putExtra("ACTIONTYPE", "ADD");
                    startActivity(intent);
            
                }
            });
    
            // add data for displaying in expandable list view
            MyLog.WriteLogMessage("About to load data");
            loadData();
            MyLog.WriteLogMessage("data loaded " + Integer.toString(mCategoryList.size()));
    
            //get reference of the ExpandableListView
            MyLog.WriteLogMessage("getting simpleExpandableListView");
            simpleExpandableListView = (ExpandableListView) findViewById(R.id.categoryListView);

            // create the adapter by passing your ArrayList data
            MyLog.WriteLogMessage("creating listAdapter");
            listAdapter = new CategoryListAdapter(activityCategory.this, mCategoryList);

            // attach the adapter to the expandable list view
            MyLog.WriteLogMessage("attach listAdapter to listview");
            simpleExpandableListView.setAdapter(listAdapter);
    
            MyLog.WriteLogMessage("done");

            //expand all the Groups
            //expandAll();
    
            // setOnChildClickListener listener for child row click
            /*
            simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
            {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
                {
                    //get the group header
                    CategoryItem headerInfo = deptList.get(groupPosition);
                    //get the child info
                    SubCategoryItem detailInfo = headerInfo.getProductList().get(childPosition);
                    //display it or do something with it
                    Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getName()
                        + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            // setOnGroupClickListener listener for group heading click
            simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
            {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                {
                    //get the group header
                    CategoryItem headerInfo = deptList.get(groupPosition);
                    //display it or do something with it
                    Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getName(),
                        Toast.LENGTH_LONG).show();
            
                    return false;
                }
            });
            */
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in activityCategory::onCreate", e.getMessage());
        }

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
    private void loadData()
    {
         mCategoryList = MyDatabase.MyDB().getCategoryList();
    }

    //here we maintain our products in various departments
    /*
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
    */
    
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        loadData();
            //get reference of the ExpandableListView
            MyLog.WriteLogMessage("getting simpleExpandableListView");
            simpleExpandableListView = (ExpandableListView) findViewById(R.id.categoryListView);

            // create the adapter by passing your ArrayList data
            MyLog.WriteLogMessage("creating listAdapter");
            listAdapter = new CategoryListAdapter(activityCategory.this, mCategoryList);

            // attach the adapter to the expandable list view
            MyLog.WriteLogMessage("attach listAdapter to listview");
            simpleExpandableListView.setAdapter(listAdapter);
    
    }
}

