package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.Date;

public class TableSubCategory extends TableBase
{
    private SQLiteOpenHelper helper;

    TableSubCategory(SQLiteOpenHelper argHelper)
    {
        helper = argHelper;
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        try
        {
            String lSql =
                "DROP TABLE IF EXISTS tblSubCategory";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TablesubCategory::dropTableIfExists", e.getMessage());
        }
    }
 
    void onCreate(SQLiteDatabase db)
    {
        try
        {
            dropTableIfExists(db);
    
            String lSql =
                "CREATE TABLE tblSubCategory " +
                    " (" +
                    "   SubCategoryId INTEGER PRIMARY KEY, " +
                    "   CategoryId INTEGER, " +
                    "   SubCategoryName TEXT " +
                    ") ";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableSubCategory::onCreate", e.getMessage());
        }
    }

    public int getNextSubCategoryId()
    {
        try {
            String lString;
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query("tblSubCategory", new String[]{"MAX(SubCategoryId)"},
                        null, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    lString = cursor.getString(0);
                    if (lString != null)
                        return (Integer.parseInt(cursor.getString(0)) + 1);
                }
            }
            finally
            {
                db.close();
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableSubCategory::getNextSubCategoryId", e.getMessage());
        }
        return(1);
    }

    public void addSubCategory(RecordSubCategory rc)
    {
        try
        {
            rc.SubCategoryId = getNextSubCategoryId();

            MyLog.WriteLogMessage("Adding subcategory." +
              "SubCategoryId: " + rc.SubCategoryId.toString() + ", " +
                    "SubCategoryName: " + rc.SubCategoryName);

            String lSql =
                    "INSERT INTO tblSubCategory " +
                    "(SubCategoryId, CategoryId, SubCategoryName) " +
                    "VALUES ("+
                    Integer.toString(rc.SubCategoryId) + "," +
                    Integer.toString(rc.CategoryId) + "," +
                    "'" + rc.SubCategoryName + "') ";

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(lSql);
            db.close();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableSubCategory::addSubCategory", e.getMessage());
        }
    }
    
    public void updateSubCategory(RecordSubCategory rc)
    {
        try
        {
            MyLog.WriteLogMessage("Updating subcategory." +
              "SubCategoryId: " + rc.SubCategoryId.toString() + ", " +
                    "SubCategoryNamee: " + rc.SubCategoryName);

            String lSql =
                    "UPDATE tblSubCategory " +
                    "  SET SubCategoryName = '" + rc.SubCategoryName + "' " +
                    "WHERE SubCategoryId = " + rc.SubCategoryId.toString();

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(lSql);
            db.close();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableSubCategory::updateSubCategory", e.getMessage());
        }
    }
    
    public void deleteSubCategory(RecordSubCategory rc)
    {
        try
        {
            MyLog.WriteLogMessage("Deleting subcategory." +
              "SubCategoryId: " + rc.SubCategoryId.toString() + ", " +
                    "SubCategoryNamee: " + rc.SubCategoryName);

            String lSql =
                    "DELETE FROM tblSubCategory " +
                    "WHERE SubCategoryId = " + rc.SubCategoryId.toString();

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(lSql);
            db.close();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableSubCategory::deleteSubCategory", e.getMessage());
        }
    }

    public ArrayList<RecordSubCategory> getSubCategoryList(Integer pCategoryId)
    {
        int cnt;
        ArrayList<RecordSubCategory> list;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor;
                String l_SQL;
                if (pCategoryId != 0) {
                    l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName " +
                            "FROM tblSubCategory a, tblCategory b " +
                            "WHERE a.CategoryId = " + pCategoryId.toString() + " " +
                            "AND a.CategoryId = b.CategoryId " +
                            "ORDER BY b.CategoryName, a.SubCategoryName ";
                } else {
                    l_SQL = "SELECT a.SubCategoryId, a.CategoryId, a.SubCategoryName, b.CategoryName " +
                            "FROM tblSubCategory a, tblCategory b " +
                            "WHERE a.CategoryId = b.CategoryId " +
                            "ORDER BY b.CategoryName, a.SubCategoryName ";
                }
                cursor = db.rawQuery(l_SQL, null);
                cnt = cursor.getCount();
                list = new ArrayList<RecordSubCategory>();
                cnt = 0;
                if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        if (cursor.getCount() == 0)
                            break;
                        list.add
                                (
                                        new RecordSubCategory
                                                (
                                                        Integer.parseInt(cursor.getString(1)),
                                                        cursor.getString(3),
                                                        Integer.parseInt(cursor.getString(0)),
                                                        cursor.getString(2)
                                                )
                                );
                        cnt++;
                    } while (cursor.moveToNext());
                }
            }
            finally
            {
                db.close();
            }

        }
        catch (Exception e)
        {
            list = new ArrayList<RecordSubCategory>();
            ErrorDialog.Show("Error in TableSubCategory.getSubCategoryList", e.getMessage());
        }
        return list;
    }

    public RecordSubCategory getSubCategory(Integer pSubCategoryId)
    {
        int cnt;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor;
                String l_SQL;
                l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName " +
                        "FROM tblSubCategory a, tblCategory b " +
                        "WHERE a.SubCategoryId = " + pSubCategoryId.toString() + " " +
                        "AND a.CategoryId = b.CategoryId " +
                        "ORDER BY b.CategoryName, a.SubCategoryName ";
                cursor = db.rawQuery(l_SQL, null);
                cnt = cursor.getCount();
                cnt = 0;
                if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        if (cursor.getCount() == 0)
                            break;
                        return
                                (
                                        new RecordSubCategory
                                                (
                                                        Integer.parseInt(cursor.getString(1)),
                                                        cursor.getString(3),
                                                        Integer.parseInt(cursor.getString(0)),
                                                        cursor.getString(2)
                                                )
                                );
                    } while (cursor.moveToNext());
                }
            }
            finally
            {
                db.close();
            }

        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TableSubCategory.getSubCategoryList", e.getMessage());
        }
        return(null);
    }


    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion==4 && newVersion==5)
        {
            MyLog.WriteLogMessage("Creating table tblSubCategory");

            onCreate(db);
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
    
}
