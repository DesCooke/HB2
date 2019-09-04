package com.example.cooked.hb2.Records;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.MainActivity;

import java.util.ArrayList;

public class RecordBudgetClass
{
    public Integer BudgetClassId;
    public String budgetClassName;
    public ArrayList<RecordBudgetGroup> budgetGroups;
    public boolean divider;
    public Integer CategoryId;
    public Integer RecCount;
    public Float total;
    public Float spent;
    public Float outstanding;
    public Integer BudgetMonth;
    public Integer BudgetYear;
    public Integer DefaultBudgetType;
    public MainActivity lMainActivity;
    public boolean Expanded;

    public RecordBudgetClass()
    {
        BudgetClassId=0;
        Expanded = false;
        budgetGroups = new ArrayList<>();
        budgetClassName = "";
        divider=false;
        CategoryId = 0;
        RecCount = 0;
        total=0.00f;
        spent = 0.00f;
        outstanding = 0.00f;
        DefaultBudgetType = RecordCategory.mDBTSameMonthLastYear;
        lMainActivity=null;
    }
    
    public void ExpandChildren()
    {
        for(int i=0;i<budgetGroups.size();i++)
        {
            budgetGroups.get(i).Expanded = true;
        }
    }
}
