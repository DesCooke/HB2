package com.example.cooked.hb2.Database;

import java.util.Date;

class RecordBudget
{
    int SubCategoryId;
    Float Amount;
    Date NextDueDate;
    Boolean AutoMatchTransaction;

    RecordBudget
            (
                    int pSubCategoryId,
                    Float pAmount,
                    Date pNextDueDate,
                    Boolean pAutoMatchTransaction
            )
    {
        SubCategoryId = pSubCategoryId;
        Amount = pAmount;
        NextDueDate = pNextDueDate;
        AutoMatchTransaction = pAutoMatchTransaction;
    }

}