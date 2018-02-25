package com.example.cooked.hb2.Database;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RecordBudget
{
    int SubCategoryId;
    Float Amount;
    Date NextDueDate;

    RecordBudget
            (
                    int pSubCategoryId,
                    Float pAmount
            )
    {
        SubCategoryId = pSubCategoryId;
        Amount = pAmount;
        NextDueDate = null;

    }
    RecordBudget
            (
                    int pSubCategoryId,
                    Float pAmount,
                    Date pNextDueDate
            )
    {
        SubCategoryId = pSubCategoryId;
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
        Amount = 0.00f;
        NextDueDate = null;
    }
}