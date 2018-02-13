package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.Date;

public class TableCategory extends TableBase
{
    private SQLiteOpenHelper helper;

    TableCategory(SQLiteOpenHelper argHelper)
    {
        helper = argHelper;
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        try
        {
            String lSql =
                "DROP TABLE IF EXISTS tblCategory";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableCategory::dropTableIfExists", e.getMessage());
        }
    }
 
    void onCreate(SQLiteDatabase db)
    {
        try
        {
            dropTableIfExists(db);
    
            String lSql =
                "CREATE TABLE tblCategory " +
                    " (" +
                    "   CategoryId INTEGER PRIMARY KEY, " +
                    "   CategoryName TEXT " +
                    ") ";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableCategory::onCreate", e.getMessage());
        }
    }

    public int getNextCategoryId()
    {
        try {
            String lString;
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("tblCategory", new String[]{"MAX(CategoryId)"},
                    null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                lString = cursor.getString(0);
                if (!lString.isEmpty())
                    return (Integer.parseInt(cursor.getString(0)) + 1);
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableCategory::getNextCategoryId", e.getMessage());
        }
        return(1);
    }

    public void addCategory(RecordCategory rc)
    {
        try
        {
            rc.CategoryId = getNextCategoryId();

            MyLog.WriteLogMessage("Adding category." +
              "CategoryId: " + rc.CategoryId.toString() + ", " +
                    "CategoryNamee: " + rc.CategoryName);

            String lSql =
                    "INSERT INTO tblCategory " +
                    "(CategoryId, CategoryName) " +
                    "VALUES ("+
                    Integer.toString(rc.CategoryId) + "," +
                    "'" + rc.CategoryName + "') ";

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableCategory::addCategory", e.getMessage());
        }
    }
    
    public ArrayList<RecordCategory> getCategoryList()
    {
        int cnt;
        ArrayList<RecordCategory> list;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query
                (false,
                    "tblCategory",
                    new String[]{"CategoryId", "CategoryName"},
                    null,
                    null,
                    null,
                    null, "CategoryName", null, null);
            cnt = cursor.getCount();
            list = new ArrayList<RecordCategory>();
            cnt = 0;
            if (cursor != null)
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
                    cnt++;
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            list = new ArrayList<RecordCategory>();
            ErrorDialog.Show("Error in TableCategory.getCategoryList", e.getMessage());
        }
        return list;
    }

    

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion==1 && newVersion==2)
        {
            MyLog.WriteLogMessage("Creating table tblCategory");

            onCreate(db);
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
    
}
