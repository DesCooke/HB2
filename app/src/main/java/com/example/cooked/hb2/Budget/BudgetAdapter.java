package com.example.cooked.hb2.Budget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.Database.RecordCategoryBudget;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;


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
            String lText = String.format(Locale.ENGLISH,
                    "£%.2f / £%.2f / £%.2f",
                    detailInfo.total, detailInfo.spent, detailInfo.outstanding);
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
            final RecordBudgetGroup headerInfo = (RecordBudgetGroup) getGroup(groupPosition);
            if (view == null)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetgroup, parent, false);
    
            LinearLayout cellbudgetgroup = view.findViewById(R.id.cellbudgetgroup);
            ImageView image = view.findViewById(R.id.indicator);
            TextView budget_summary = view.findViewById(R.id.budget_summary);

            if(headerInfo.groupedBudget) {

                RecordCategoryBudget rcb = MyDatabase.MyDB().getCategoryBudget(
                        headerInfo.CategoryId, headerInfo.BudgetMonth,
                        headerInfo.BudgetYear);
                if (rcb.BudgetMonth == 0) {
                    rcb.CategoryId = headerInfo.CategoryId;
                    rcb.BudgetMonth = headerInfo.BudgetMonth;
                    rcb.BudgetYear = headerInfo.BudgetYear;
                    rcb.BudgetAmount = -250.00f;
                    if(headerInfo.DefaultBudgetType==RecordCategory.mDBTSameMonthLastYear)
                    {
                        rcb.BudgetAmount=MyDatabase.MyDB().getCategoryBudgetSameMonthLastYear(rcb.BudgetYear,
                                rcb.BudgetMonth, rcb.CategoryId);
                    }
                    if(headerInfo.DefaultBudgetType==RecordCategory.mDBTAverage)
                    {
                        rcb.BudgetAmount=MyDatabase.MyDB().getCategoryBudgetAverage(rcb.CategoryId);
                    }
                    if(headerInfo.DefaultBudgetType==RecordCategory.mDBTLastMonth)
                    {
                        rcb.BudgetAmount=MyDatabase.MyDB().getCategoryBudgetLastMonth(rcb.BudgetYear,
                                rcb.BudgetMonth, rcb.CategoryId);
                    }
                    if(rcb.BudgetAmount>=-0.00001 && rcb.BudgetAmount <=0.00001)
                    {
                        int lYear=rcb.BudgetYear;
                        int lMonth=rcb.BudgetMonth;
                        while(rcb.BudgetAmount>=-0.00001 && rcb.BudgetAmount <=0.00001) {
                            rcb.BudgetAmount = MyDatabase.MyDB().getCategoryBudgetLastMonth(lYear,
                                    lMonth, rcb.CategoryId);
                            if(rcb.BudgetAmount < -0.00001)
                                break;
                            if(rcb.BudgetAmount > 0.00001)
                                break;
                            lMonth--;
                            if(lMonth==0)
                            {
                                lYear--;
                                lMonth=12;
                            }
                            if(lYear<2000)
                                break;
                        }
                    }
                    if(rcb.BudgetAmount<-0.00001 || rcb.BudgetAmount >0.00001) {
                        MyDatabase.MyDB().addCategoryBudget(rcb);
                    }
                }
                headerInfo.total = rcb.BudgetAmount;
                headerInfo.outstanding = headerInfo.total - headerInfo.spent;
            }



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
            Button btnBudget = view.findViewById(R.id.btnBudget);
            if(headerInfo.groupedBudget) {
                btnBudget.setText(String.format(Locale.ENGLISH, "£%.2f", headerInfo.total));
                btnBudget.setFocusable(false);
                btnBudget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Select a Budget for the Group");

                        final EditText input = new EditText(context);
                        input.setText(String.format(Locale.ENGLISH, "%.2f", headerInfo.total));

                        input.setInputType(InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                InputType.TYPE_NUMBER_FLAG_SIGNED);
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();
                                RecordCategoryBudget rcb = MyDatabase.MyDB().getCategoryBudget(
                                        headerInfo.CategoryId, headerInfo.BudgetMonth,
                                        headerInfo.BudgetYear);
                                if(rcb.BudgetMonth==0)
                                {
                                    Toast.makeText(context, "Budget for Category/Period Added", LENGTH_LONG).show();

                                    rcb.CategoryId = headerInfo.CategoryId;
                                    rcb.BudgetMonth = headerInfo.BudgetMonth;
                                    rcb.BudgetYear = headerInfo.BudgetYear;
                                    rcb.BudgetAmount = Float.valueOf(m_Text);

                                    MyDatabase.MyDB().addCategoryBudget(rcb);
                                }
                                else
                                {
                                    Toast.makeText(context, "Budget for Category/Period Updated", LENGTH_LONG).show();
                                    rcb.BudgetAmount = Float.valueOf(m_Text);

                                    MyDatabase.MyDB().updateCategoryBudget(rcb);
                                }
                                headerInfo.total = Float.valueOf(m_Text);
                                headerInfo.outstanding = headerInfo.total - headerInfo.spent;
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();                    }
                });
            }
            else
            {
                btnBudget.setVisibility(View.GONE);
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