package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import com.example.cooked.hb2.CategoryUI.SubCategoryAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class activitySubCategory extends AppCompatActivity
{

    public String categoryName;
    public Integer categoryId;
    public ArrayList<RecordSubCategory> mDataset;
    public RecyclerView mSubCategoryList;
    public RecyclerView.LayoutManager mLayoutManagerCurrent;
    public SubCategoryAdapter mSubCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_subcategory_list);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            categoryId = getIntent().getIntExtra("CATEGORYID", 0);
            categoryName = getIntent().getStringExtra("CATEGORYNAME");

            setTitle("Sub Categories for " + categoryName);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(activitySubCategory.this, activitySubCategoryItem.class);
                    intent.putExtra("ACTIONTYPE", "ADD");
                    intent.putExtra("CATEGORYID", categoryId);
                    intent.putExtra("CATEGORYNAME", categoryName);
                    startActivity(intent);

                }
            });

            CreateRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void CreateRecyclerView()
    {
        try
        {
            mDataset = MyDatabase.MyDB().getSubCategoryList(categoryId);
            mSubCategoryList = findViewById(R.id.subcategoryList);
            mSubCategoryList.setHasFixedSize(true);
            mLayoutManagerCurrent = new LinearLayoutManager(this);
            mSubCategoryList.setLayoutManager(mLayoutManagerCurrent);
            mSubCategoryAdapter = new SubCategoryAdapter(mDataset);
            mSubCategoryList.setAdapter(mSubCategoryAdapter);

            mSubCategoryAdapter.setOnItemClickListener(new SubCategoryAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, RecordSubCategory obj)
                {
                    Intent intent = new Intent(getApplicationContext(), activitySubCategoryItem.class);
                    intent.putExtra("ACTIONTYPE", "EDIT");
                    intent.putExtra("SUBCATEGORYID", obj.SubCategoryId);
                    intent.putExtra("CATEGORYID", obj.CategoryId);
                    intent.putExtra("SUBCATEGORYNAME", obj.SubCategoryName);
                    startActivity(intent);
                }
            });
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        try
        {
            CreateRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }
}

