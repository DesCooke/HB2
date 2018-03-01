package com.example.cooked.hb2.Budget;

import java.util.ArrayList;

public class RecordBudgetGroup
{
    public String budgetGroupName;
    public ArrayList<RecordBudgetItem> budgetItems;
    public boolean divider;
    public Integer CategoryId;
    public Integer RecCount;
    public Float total;
    public Float spent;
    public Float outstanding;

    public RecordBudgetGroup()
    {
        budgetItems = new ArrayList<>();
        budgetGroupName = "";
        divider=false;
        CategoryId = 0;
        RecCount = 0;
        total=0.00f;
        spent = 0.00f;
        outstanding = 0.00f;
    }
}
