package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.cooked.hb2.CategoryUI.CategoryAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;

public class activityCategory extends AppCompatActivity
{

    public ArrayList<RecordCategory> mDataset;
    public RecyclerView mCategoryList;
    public RecyclerView.LayoutManager mLayoutManagerCurrent;
    public CategoryAdapter mCategoryAdapter;
    public String mActionType;
    public Boolean pickOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_category_list);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            setTitle("Categories");

            pickOnly=false;
            mActionType = getIntent().getStringExtra("ACTIONTYPE");
            if(mActionType!=null)
                if(mActionType.compareTo("PICK")==0)
                    pickOnly=true;


            FloatingActionButton fab = findViewById(R.id.fab);
            if(pickOnly)
            {
                fab.hide();
            }
            else
            {
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
            }

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
            mDataset = MyDatabase.MyDB().getCategoryList();
            mCategoryList = findViewById(R.id.categoryList);
            mCategoryList.setHasFixedSize(true);
            mLayoutManagerCurrent = new LinearLayoutManager(this);
            mCategoryList.setLayoutManager(mLayoutManagerCurrent);
            mCategoryAdapter = new CategoryAdapter(mDataset);
            mCategoryAdapter.mPickOnly = pickOnly;
            mCategoryList.setAdapter(mCategoryAdapter);

            if(pickOnly==false)
            {
                mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecordCategory obj) {
                        Intent intent = new Intent(getApplicationContext(), activityCategoryItem.class);
                        intent.putExtra("ACTIONTYPE", "EDIT");
                        intent.putExtra("CATEGORYID", obj.CategoryId);
                        intent.putExtra("CATEGORYNAME", obj.CategoryName);
                        startActivity(intent);
                    }
                });
                mCategoryAdapter.setOnShowClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecordCategory obj) {
                        Intent intent = new Intent(getApplicationContext(), activitySubCategory.class);
                        intent.putExtra("CATEGORYID", obj.CategoryId);
                        intent.putExtra("CATEGORYNAME", obj.CategoryName);
                        startActivity(intent);
                    }
                });
            }
            else
            {
                mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, RecordCategory obj)
                    {
                        Intent intent = new Intent();

                        intent.putExtra("NEWCATEGORYID", obj.CategoryId);
                        intent.putExtra("NEWCATEGORYNAME", obj.CategoryName);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
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

