package com.example.cooked.hb2.Budget;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.MainActivity;

import java.util.ArrayList;

public class RecordBudgetClass
{
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

    public RecordBudgetClass()
    {
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
}
