package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.cooked.hb2.CategoryUI.CategoryPickerAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;


public class CategoryPicker extends Dialog implements View.OnClickListener
{
    public ArrayList<RecordSubCategory> mDataset;
    public MyInt MySubCategoryId;
    public TextView edtSubCategoryName;

    public CategoryPicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in CategoryPicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.category_picker);

            CreateRecyclerView();

        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    private void CreateRecyclerView()
    {
        mDataset = MyDatabase.MyDB().getSubCategoryList(0);
        RecyclerView mCategoryList = findViewById(R.id.categoryList);
        mCategoryList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerCurrent = new LinearLayoutManager(MainActivity.context);
        mCategoryList.setLayoutManager(mLayoutManagerCurrent);
        CategoryPickerAdapter mCategoryPickerAdapter = new CategoryPickerAdapter(mDataset);
        mCategoryList.setAdapter(mCategoryPickerAdapter);

        mCategoryPickerAdapter.setOnItemClickListener(new CategoryPickerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordSubCategory obj)
            {
                MySubCategoryId.Value = obj.SubCategoryId;
                edtSubCategoryName.setText(obj.SubCategoryName);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        dismiss();
    }

}
