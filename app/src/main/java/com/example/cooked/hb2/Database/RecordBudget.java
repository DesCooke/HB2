package com.example.cooked.hb2.Database;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RecordBudget
{
    int SubCategoryId;
    int CategoryId;
    String SubCategoryName;
    String CategoryName;
    Boolean MonthlyBudget;
    Float Amount;
    Date NextDueDate;

    RecordBudget
            (
                    int pSubCategoryId,
                    int pCategoryId,
                    Float pAmount,
                    String pSubCategoryName,
                    String pCategoryName,
                    Boolean pMonthlyBudget,
                    Date pNextDueDate
            )
    {
        SubCategoryId = pSubCategoryId;
        CategoryId = pCategoryId;
        SubCategoryName = pSubCategoryName;
        CategoryName = pCategoryName;
        MonthlyBudget = pMonthlyBudget;
        Amount = pAmount;
        NextDueDate = pNextDueDate;
    }

    public Date getNextDueDate()
    {
        return(NextDueDate);
    }

    public RecordBudget()
    {
        SubCategoryId = 0;
        CategoryId = 0;
        SubCategoryName = "";
        CategoryName = "";
        MonthlyBudget = false;
        Amount = 0.00f;
        NextDueDate = null;
    }
}