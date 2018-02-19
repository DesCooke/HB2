package com.example.cooked.hb2.Database;

public class RecordSubCategory
{
    public Integer CategoryId;
    public String CategoryName;
    public Integer SubCategoryId;
    public String SubCategoryName;

    RecordSubCategory
            (
                     Integer pCategoryId,
                     String pCategoryName,
                     Integer pSubCategoryId,
                     String pSubCategoryName
            )
    {
        CategoryId = pCategoryId;
        CategoryName = pCategoryName;
        SubCategoryId = pSubCategoryId;
        SubCategoryName = pSubCategoryName;
    }
    
    public RecordSubCategory()
    {
        CategoryId = 0;
        CategoryName = "";
        SubCategoryId = 0;
        SubCategoryName = "";
    }
}
