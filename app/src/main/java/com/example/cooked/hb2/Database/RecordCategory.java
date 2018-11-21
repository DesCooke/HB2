package com.example.cooked.hb2.Database;

public class RecordCategory
{
    public static int mDBTSameMonthLastYear = 0;
    public static int mDBTLastMonth = 1;
    public static int mDBTAverage = 2;

    public Integer CategoryId;
    public String CategoryName;
    public Boolean GroupedBudget;
    public Integer DefaultBudgetType;

    RecordCategory
            (
                     Integer pCategoryId,
                     String pCategoryName,
                     Boolean pGroupedBudget,
                     Integer pDefaultBudgetType

            )
    {
        CategoryId = pCategoryId;
        CategoryName = pCategoryName;
        GroupedBudget = pGroupedBudget;
        DefaultBudgetType = pDefaultBudgetType;
    }
    
    public RecordCategory()
    {
        CategoryId = 0;
        CategoryName = "";
        GroupedBudget = false;
        DefaultBudgetType = mDBTSameMonthLastYear;
    }
    
}
