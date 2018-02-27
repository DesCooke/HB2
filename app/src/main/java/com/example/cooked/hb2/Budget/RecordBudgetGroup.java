package com.example.cooked.hb2.Budget;

import java.util.ArrayList;

/**
 * Created by cooked on 27/02/2018.
 */

public class RecordBudgetGroup
{
    public String budgetGroupName;
    public ArrayList<RecordBudgetItem> budgetItems;
    public boolean divider;
    public Integer CategoryId;
    public Integer RecCount;

    public RecordBudgetGroup()
    {
        budgetItems = new ArrayList<RecordBudgetItem>();
        budgetGroupName = "";
        divider=false;
        CategoryId = 0;
        RecCount = 0;
    }
}
