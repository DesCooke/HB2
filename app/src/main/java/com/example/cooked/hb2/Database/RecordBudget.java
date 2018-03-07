package com.example.cooked.hb2.Database;

import java.util.Date;

class RecordBudget
{
    int SubCategoryId;
    Float Amount;
    Date NextDueDate;

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

}