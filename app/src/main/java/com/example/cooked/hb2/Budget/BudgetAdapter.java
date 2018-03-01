package com.example.cooked.hb2.Budget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Locale;


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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetitem, parent, false);

            TextView childItem = view.findViewById(R.id.childItem);
            childItem.setText(detailInfo.budgetItemName.trim());
            TextView budget_summary = view.findViewById(R.id.budget_summary);

            String lText = String.format(Locale.ENGLISH, "£%.2f / £%.2f / £%.2f",detailInfo.total, detailInfo.spent,
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetgroup, parent, false);
    
            LinearLayout cellbudgetgroup = view.findViewById(R.id.cellbudgetgroup);
            ImageView image = view.findViewById(R.id.indicator);
            TextView budget_summary = view.findViewById(R.id.budget_summary);

            String lText = String.format(Locale.ENGLISH, "£%.2f / £%.2f / £%.2f",headerInfo.total, headerInfo.spent,
                    headerInfo.outstanding);
            budget_summary.setText(lText);

            TextView heading = view.findViewById(R.id.heading);
            heading.setText(headerInfo.budgetGroupName.trim());
            if (headerInfo.budgetGroupName.compareTo(context.getResources().getString(R.string.budget_header_monthly_expenses))==0 ||
                    headerInfo.budgetGroupName.compareTo(context.getResources().getString(R.string.budget_header_monthly_income))==0 ||
                    headerInfo.budgetGroupName.compareTo(context.getResources().getString(R.string.budget_header_extra_expenses))==0 ||
                    headerInfo.budgetGroupName.compareTo(context.getResources().getString(R.string.budget_header_extra_income))==0
                    )
            {
                cellbudgetgroup.setBackgroundColor(ContextCompat.getColor(context, R.color.budgetHeader));
                heading.setTextColor(Color.WHITE);
                budget_summary.setTextColor(Color.WHITE);
            }
            else
            {
                cellbudgetgroup.setBackgroundColor(ContextCompat.getColor(context, R.color.budgetSubHeader));
                heading.setTextColor(Color.BLACK);
                budget_summary.setTextColor(Color.BLACK);
            }
    if (image != null)
    {
        if (getChildrenCount(groupPosition) == 0)
        {
            image.setVisibility(View.INVISIBLE);
        }
        else
        {
            image.setVisibility(View.VISIBLE);
            if(isLastChild)
            {
                image.setImageResource(R.drawable.expanded);
            }
            else
            {
                image.setImageResource(R.drawable.collapsed);
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