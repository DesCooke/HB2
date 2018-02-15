package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cooked.hb2.CategoryUI.CategoryAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;

import java.util.ArrayList;

public class activityCategory extends AppCompatActivity{

    public ArrayList<RecordCategory> mDataset;
    public RecyclerView mCategoryList;
    public RecyclerView.LayoutManager mLayoutManagerCurrent;
    public CategoryAdapter mCategoryAdapter;

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

            CreateRecyclerView();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in activityCategory::onCreate", e.getMessage());
        }

    }

    private void CreateRecyclerView()
    {
        mDataset = MyDatabase.MyDB().getCategoryList();
        mCategoryList = (RecyclerView) findViewById(R.id.categoryList);
        mCategoryList.setHasFixedSize(true);
        mLayoutManagerCurrent = new LinearLayoutManager(this);
        mCategoryList.setLayoutManager(mLayoutManagerCurrent);
        mCategoryAdapter = new CategoryAdapter(mDataset);
        mCategoryList.setAdapter(mCategoryAdapter);
        
        mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordCategory obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityCategoryItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("CATEGORYID", obj.CategoryId);
                intent.putExtra("CATEGORYNAME", obj.CategoryName);
                startActivity(intent);
            }
        });
        mCategoryAdapter.setOnShowClickListener(new CategoryAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordCategory obj)
            {
                Intent intent = new Intent(getApplicationContext(), activitySubCategory.class);
                intent.putExtra("CATEGORYID", obj.CategoryId);
                intent.putExtra("CATEGORYNAME", obj.CategoryName);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        CreateRecyclerView();
    }
}

