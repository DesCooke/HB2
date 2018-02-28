package com.example.cooked.hb2.Budget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;


public class BudgetAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<RecordBudgetGroup> budgetList;
    

    public BudgetAdapter(Context context, ArrayList<RecordBudgetGroup> pBudgetList) {
        try
        {
            this.context = context;
            this.budgetList = pBudgetList;
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::BudgetAdapter", e.getMessage());
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        try
        {
            ArrayList<RecordBudgetItem> budgetItemList = budgetList.get(groupPosition).budgetItems;
            return budgetItemList.get(childPosition);
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getChild", e.getMessage());
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
            RecordBudgetItem detailInfo = (RecordBudgetItem) getChild(groupPosition, childPosition);
            if (view == null)
            {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.budgetitem, null);
            }

//        TextView sequence = (TextView) view.findViewById(R.id.sequence);
            //      sequence.setText(detailInfo.getSequence().trim() + ". ");
            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(detailInfo.budgetItemName.trim());
            TextView budget_summary = view.findViewById(R.id.budget_summary);

            String lText = String.format("£%.2f / £%.2f / £%.2f",detailInfo.total, detailInfo.spent,
                    detailInfo.outstanding);
            budget_summary.setText(lText);
    
            return view;
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getChildView", e.getMessage());
        }
        return(null);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try
        {
            ArrayList<RecordBudgetItem> budgetItemList = budgetList.get(groupPosition).budgetItems;
            return budgetItemList.size();
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getChildrenCount", e.getMessage());
        }

        return(0);
    }

    @Override
    public Object getGroup(int groupPosition) {
        try
        {
            return budgetList.get(groupPosition);
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getGroup", e.getMessage());
        }
        return(null);
    }

    @Override
    public int getGroupCount() {
        try
        {
            return budgetList.size();
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getGroup", e.getMessage());
        }
        return(0);
    }

    @Override
    public long getGroupId(int groupPosition) {
        try
        {
            return groupPosition;
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getGroupId", e.getMessage());
        }
        return(0);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        try
        {
            RecordBudgetGroup headerInfo = (RecordBudgetGroup) getGroup(groupPosition);
            if (view == null)
            {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.budgetgroup, null);
            }
    
            LinearLayout cellbudgetgroup = view.findViewById(R.id.cellbudgetgroup);
            ImageView image = view.findViewById(R.id.indicator);
            
            TextView heading = (TextView) view.findViewById(R.id.heading);
            heading.setText(headerInfo.budgetGroupName.trim());
            if (headerInfo.divider)
            {
                //cellbudgetgroup.setBackgroundColor(Color.GREEN);
                //heading.setTextColor(Color.WHITE);
            }
    if (image != null)
    {
        ImageView indicator = (ImageView) image;
        if (getChildrenCount(groupPosition) == 0)
        {
            indicator.setVisibility(View.INVISIBLE);
        }
        else
        {
            indicator.setVisibility(View.VISIBLE);
            if(isLastChild)
            {
                indicator.setImageResource(R.drawable.expanded);
            }
            else
            {
                indicator.setImageResource(R.drawable.collapsed);
            }
            
        }
    }            return view;
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in BudgetAdapter::getGroupView", e.getMessage());
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