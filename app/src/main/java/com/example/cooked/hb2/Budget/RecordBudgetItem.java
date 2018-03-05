package com.example.cooked.hb2.Budget;

import java.util.ArrayList;

public class RecordBudgetItem
{
    public String budgetItemName;
    public Integer SubCategoryId;
    public Integer RecCount;
    public Float total;
    public Float spent;
    public Float outstanding;

    public RecordBudgetItem()
    {
        budgetItemName = "";
        SubCategoryId = 0;
        RecCount = 0;
        total=0.00f;
        spent = 0.00f;
        outstanding = 0.00f;
    }
}
