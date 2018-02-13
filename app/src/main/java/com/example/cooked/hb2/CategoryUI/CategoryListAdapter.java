package com.example.cooked.hb2.CategoryUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.cooked.hb2.CategoryUI.SubCategoryItem;
import com.example.cooked.hb2.CategoryUI.CategoryItem;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;


public class CategoryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<RecordCategory> categoryList;

    public CategoryListAdapter(Context context, ArrayList<RecordCategory> lcategoryList) {
        this.context = context;
        this.categoryList = lcategoryList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        try
        {
            //ArrayList<SubCategoryItem> productList = deptList.get(groupPosition).getProductList();
            //return productList.get(childPosition);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryListAdapter::getChild", e.getMessage());
        }
        return(null);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        try
        {
            /*
            SubCategoryItem detailInfo = (SubCategoryItem) getChild(groupPosition, childPosition);
            if (view == null)
            {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.subcategory_item, null);
            }
    
            TextView sequence = (TextView) view.findViewById(R.id.sequence);
            sequence.setText(detailInfo.getSequence().trim() + ". ");
            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(detailInfo.getName().trim());
    
            return view;
            */
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryListAdapter::getChildView", e.getMessage());
        }
        return(view);
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        try
        {
            //ArrayList<SubCategoryItem> productList = deptList.get(groupPosition).getProductList();
            //return productList.size();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryListAdapter::getChildrenCount", e.getMessage());
        }
        return(0);
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        try
        {
            return categoryList.get(groupPosition);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryListAdapter::getGroup", e.getMessage());
        }
        return(null);
    }

    @Override
    public int getGroupCount()
    {
        try
        {
            return categoryList.size();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryListAdapter::getGroupCount", e.getMessage());
        }
        return(0);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        try
        {
            MyLog.WriteLogMessage("Starting getGroupView");

            RecordCategory category = (RecordCategory) getGroup(groupPosition);
            if (view == null)
            {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.category_item, null);
            }
    
            TextView heading = (TextView) view.findViewById(R.id.heading);
            heading.setText(Integer.toString(category.CategoryId) + ":" + category.CategoryName);
    
            MyLog.WriteLogMessage("End getGroupView");
    
            return view;
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryListAdapter::getGroupView", e.getMessage());
        }
        return(null);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}