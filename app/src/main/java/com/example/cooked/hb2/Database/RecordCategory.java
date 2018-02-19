package com.example.cooked.hb2.Database;

public class RecordCategory
{
    public Integer CategoryId;
    public String CategoryName;

    RecordCategory
            (
                     Integer pCategoryId,
                     String pCategoryName
            )
    {
        CategoryId = pCategoryId;
        CategoryName = pCategoryName;
    }
    
    public RecordCategory()
    {
        CategoryId = 0;
        CategoryName = "";
    }
    
}
