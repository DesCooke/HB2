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
    public Boolean Monitor;

    RecordCategory
            (
                     Integer pCategoryId,
                     String pCategoryName,
                     Boolean pGroupedBudget,
                     Integer pDefaultBudgetType,
                     Boolean pMonitor
            )
    {
        CategoryId = pCategoryId;
        CategoryName = pCategoryName;
        GroupedBudget = pGroupedBudget;
        DefaultBudgetType = pDefaultBudgetType;
        Monitor = pMonitor;
    }
    
    public RecordCategory()
    {
        CategoryId = 0;
        CategoryName = "";
        GroupedBudget = false;
        DefaultBudgetType = mDBTSameMonthLastYear;
        Monitor=false;
    }
    
}
