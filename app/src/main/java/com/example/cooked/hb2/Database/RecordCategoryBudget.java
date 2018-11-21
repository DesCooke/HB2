package com.example.cooked.hb2.Database;

public class RecordCategoryBudget
{
    public Integer CategoryId;
    public Integer BudgetMonth;
    public Integer BudgetYear;
    public Float BudgetAmount;

    RecordCategoryBudget
            (
                    Integer pCategoryId,
                    Integer pBudgetMonth,
                    Integer pBudgetYear,
                    Float pBudgetAmount

            )
    {
        CategoryId = pCategoryId;
        BudgetMonth = pBudgetMonth;
        BudgetYear = pBudgetYear;
        BudgetAmount = pBudgetAmount;
    }

    public RecordCategoryBudget()
    {
        CategoryId = 0;
        BudgetMonth = 0;
        BudgetYear = 0;
        BudgetAmount = 0.00f;
    }

}
