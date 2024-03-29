package com.example.cooked.hb2.Records;

public class RecordBudgetItem
{
    public Integer BudgetClassId;
    public Integer BudgetGroupId;
    public Integer BudgetItemId;
    public String budgetItemName;
    public String BudgetGroupName;
    public String CategoryName;
    public String SubCategoryName;
    public Integer SubCategoryId;
    public Integer RecCount;
    public Float total;
    public Float origTotal;
    public Float spent;
    public Float outstanding;
    public Boolean groupedBudget;
    public Boolean Monitor;

    public RecordBudgetItem()
    {
        BudgetClassId=0;
        BudgetGroupId=0;
        BudgetItemId=0;
        budgetItemName = "";
        SubCategoryId = 0;
        RecCount = 0;
        total=0.00f;
        spent = 0.00f;
        outstanding = 0.00f;
        groupedBudget = false;
        Monitor=false;
        origTotal = 0.00f;
        BudgetGroupName = "";
        CategoryName="";
        SubCategoryName="";
    }
}
