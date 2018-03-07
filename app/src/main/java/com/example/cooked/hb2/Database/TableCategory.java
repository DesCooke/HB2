package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;

class TableCategory extends TableBase
{
    
    TableCategory(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblCategory";
        executeSQL(lSql, "TableCategory::dropTableIfExists", db);
    }
    
    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);
        
        String lSql =
            "CREATE TABLE tblCategory " +
                " (" +
                "   CategoryId INTEGER PRIMARY KEY, " +
                "   CategoryName TEXT " +
                ") ";
        
        executeSQL(lSql, "TableCategory::onCreate", db);
    }
    
    private int getNextCategoryId()
    {
        String lSql = "SELECT MAX(CategoryId) FROM tblCategory ";
        
        return (getMaxPlus1(lSql, "TableCategory::getNextCategoryId"));
    }
    
    void addCategory(RecordCategory rc)
    {
        rc.CategoryId = getNextCategoryId();
        
        String lSql =
            "INSERT INTO tblCategory " +
                "(CategoryId, CategoryName) " +
                "VALUES (" +
                Integer.toString(rc.CategoryId) + "," +
                "'" + rc.CategoryName + "') ";
        
        executeSQL(lSql, "TableCategory::addCategory", null);
    }
    
    void updateCategory(RecordCategory rc)
    {
        String lSql =
            "UPDATE tblCategory " +
                "  SET CategoryName = '" + rc.CategoryName + "' " +
                "WHERE CategoryId = " + rc.CategoryId.toString();
        
        executeSQL(lSql, "TableCategory::updateCategory", null);
    }
    
    void deleteCategory(RecordCategory rc)
    {
        String lSql =
            "DELETE FROM tblCategory " +
                "WHERE CategoryId = " + rc.CategoryId.toString();
        
        executeSQL(lSql, "TableCategory::deleteCategory", null);
    }
    
    ArrayList<RecordCategory> getCategoryList()
    {
        ArrayList<RecordCategory> list;
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    String lString =
                        "SELECT CategoryId, CategoryName " +
                            "FROM tblCategory " +
                            "ORDER BY CategoryName";
                    Cursor cursor = db.rawQuery(lString, null);
                    list = new ArrayList<>();
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            do
                            {
                                list.add
                                    (
                                        new RecordCategory
                                            (
                                                Integer.parseInt(cursor.getString(0)),
                                                cursor.getString(1)
                                            )
                                    );
                            } while (cursor.moveToNext());
                        }
                        finally
                        {
                            cursor.close();
                        }
                    }
                }
                finally
                {
                    db.close();
                }
            }
        }
        catch (Exception e)
        {
            list = new ArrayList<>();
            ErrorDialog.Show("Error in TableCategory.getCategoryList", e.getMessage());
        }
        return list;
    }
    
    
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 1 && newVersion == 2)
        {
            MyLog.WriteLogMessage("Creating table tblCategory");
            
            onCreate(db);
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion) );
    }
    
}
