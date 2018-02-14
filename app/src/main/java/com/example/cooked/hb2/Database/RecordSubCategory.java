package com.example.cooked.hb2.Database;

public class RecordSubCategory
{
    public Integer CategoryId;
    public Integer SubCategoryId;
    public String SubCategoryName;

    public RecordSubCategory
            (
                     Integer pCategoryId,
                     Integer pSubCategoryId,
                     String pSubCategoryName
            )
    {
        CategoryId = pCategoryId;
        SubCategoryId = pSubCategoryId;
        SubCategoryName = pSubCategoryName;
    }
    
    public RecordSubCategory()
    {
        CategoryId = 0;
        SubCategoryId = 0;
        SubCategoryName = "";
    }
}
