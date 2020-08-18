package com.example.cooked.hb2.Records;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.MainActivity;

import java.util.ArrayList;

public class RecordBudgetGroup
{
    public Integer BudgetClassId;
    public Integer BudgetGroupId;
    public String budgetGroupName;
    public ArrayList<RecordBudgetItem> budgetItems;
    public boolean divider;
    public Integer CategoryId;
    public Integer RecCount;
    public Float total;
    public Float spent;
    public Float outstanding;
    public Boolean groupedBudget;
    public Integer BudgetMonth;
    public Integer BudgetYear;
    public Integer DefaultBudgetType;
    public MainActivity lMainActivity;
    public boolean Expanded;
    public boolean Monitor;

    public RecordBudgetGroup()
    {
        BudgetClassId=0;
        BudgetGroupId=0;
        Expanded = false;
        budgetItems = new ArrayList<>();
        budgetGroupName = "";
        divider=false;
        CategoryId = 0;
        RecCount = 0;
        total=0.00f;
        spent = 0.00f;
        outstanding = 0.00f;
        groupedBudget = false;
        DefaultBudgetType = RecordCategory.mDBTSameMonthLastYear;
        lMainActivity=null;
    }
}
