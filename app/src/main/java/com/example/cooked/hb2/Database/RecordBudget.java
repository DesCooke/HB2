package com.example.cooked.hb2.Database;

import java.util.Date;

class RecordBudget
{
    int SubCategoryId;
    Float Amount;
    Date NextDueDate;
    Boolean AutoMatchTransaction;
    int mPlannedId;
    public Boolean AnnualBudget;
    public Boolean DueThisMonth;

    RecordBudget
            (
                    int pSubCategoryId,
                    Float pAmount,
                    Date pNextDueDate,
                    Boolean pAutoMatchTransaction,
                    int pPlannedId
            )
    {
        AnnualBudget = false;
        DueThisMonth = false;
        SubCategoryId = pSubCategoryId;
        Amount = pAmount;
        NextDueDate = pNextDueDate;
        AutoMatchTransaction = pAutoMatchTransaction;
        mPlannedId = pPlannedId;
    }

}