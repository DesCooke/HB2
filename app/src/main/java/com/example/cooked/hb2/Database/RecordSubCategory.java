package com.example.cooked.hb2.Database;

public class RecordSubCategory
{
    public static int mSCTMonthlyExpense = 0;
    public static int mSCTMonthlyIncome = 1;
    public static int mSCTExtraExpense = 2;
    public static int mSCTExtraIncome = 3;

    public Integer CategoryId;
    public String CategoryName;
    public Integer SubCategoryId;
    public String SubCategoryName;
    public Integer SubCategoryType;
    public boolean Monitor;
    public boolean Old;

    RecordSubCategory
            (
                     Integer pCategoryId,
                     String pCategoryName,
                     Integer pSubCategoryId,
                     String pSubCategoryName,
                     Integer pSubCategoryType,
                     boolean pMonitor,
                     boolean pOld
            )
    {
        CategoryId = pCategoryId;
        CategoryName = pCategoryName;
        SubCategoryId = pSubCategoryId;
        SubCategoryName = pSubCategoryName;
        SubCategoryType=pSubCategoryType;
        Monitor = pMonitor;
        Old = pOld;
    }
    
    public RecordSubCategory()
    {
        CategoryId = 0;
        CategoryName = "";
        SubCategoryId = 0;
        SubCategoryName = "";
        SubCategoryType=mSCTMonthlyExpense;
        Monitor = false;
        Old = false;
    }
}
