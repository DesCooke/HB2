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
                "   CategoryName TEXT, " +
                "   GroupedBudget INTEGER, " +
                "   DefaultBudgetType INTEGER," +
                "   Monitor TEXT " +
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

        String lMonitor="N";
        if(rc.Monitor)
            lMonitor="Y";

        String lSql =
            "INSERT INTO tblCategory " +
                "(CategoryId, CategoryName, GroupedBudget, DefaultBudgetType, Monitor) " +
                "VALUES (" +
                Integer.toString(rc.CategoryId) + "," +
                "'" + rc.CategoryName + "', " +
                rc.GroupedBudget.toString() + "," +
                rc.DefaultBudgetType.toString() + ", " +
                "'" + lMonitor + "' " +
                ") ";
        
        executeSQL(lSql, "TableCategory::addCategory", null);
    }
    
    void updateCategory(RecordCategory rc)
    {
        Integer lGroupedBudget=0;
        if(rc.GroupedBudget)
            lGroupedBudget=1;

        String lMonitor="N";
        if(rc.Monitor)
            lMonitor="Y";

        String lSql =
            "UPDATE tblCategory " +
                "  SET CategoryName = '" + rc.CategoryName + "', " +
                "      GroupedBudget = " + lGroupedBudget.toString() + ", " +
                "      DefaultBudgetType = " + rc.DefaultBudgetType.toString() + "," +
                    "  Monitor = '" + lMonitor + "' " +
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
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    String lString =
                        "SELECT CategoryId, CategoryName, GroupedBudget, DefaultBudgetType, Monitor " +
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
                                Boolean lGroupedBudget=false;
                                if ( Integer.parseInt(cursor.getString(2))==1 )
                                    lGroupedBudget=true;

                                list.add
                                    (
                                        new RecordCategory
                                            (
                                                Integer.parseInt(cursor.getString(0)),
                                                cursor.getString(1),
                                                    lGroupedBudget,
                                                Integer.parseInt(cursor.getString(3)),
                                                    cursor.getString(4).compareTo("Y")==0
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
        return list;
    }

    RecordCategory getCategory(Integer pCategoryId)
    {
        RecordCategory item;
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    String lString =
                            "SELECT CategoryId, CategoryName, GroupedBudget, DefaultBudgetType, Monitor " +
                                    "FROM tblCategory " +
                                    "WHERE CategoryId = " + pCategoryId.toString();
                    Cursor cursor = db.rawQuery(lString, null);
                    item = new RecordCategory();
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            Boolean lGroupedBudget=false;
                            if ( Integer.parseInt(cursor.getString(2))==1 )
                                lGroupedBudget=true;

                                item= new RecordCategory
                                                        (
                                                                Integer.parseInt(cursor.getString(0)),
                                                                cursor.getString(1),
                                                                lGroupedBudget,
                                                                Integer.parseInt(cursor.getString(3)),
                                                                cursor.getString(4).compareTo("Y")==0
                                                        );
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
        return item;
    }


    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 1 && newVersion == 2)
        {
            MyLog.WriteLogMessage("Creating table tblCategory");
            onCreate(db);
        }
        if (oldVersion == 10 && newVersion == 11)
        {
            MyLog.WriteLogMessage("Adding Grouped Budget");
            db.execSQL("ALTER TABLE tblCategory ADD COLUMN GroupedBudget INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE tblCategory ADD COLUMN DefaultBudgetType INTEGER DEFAULT 0");
        }
        if (oldVersion == 20 && newVersion == 21)
        {
            MyLog.WriteLogMessage("Adding Monitor");
            db.execSQL("ALTER TABLE tblCategory ADD COLUMN Monitor TEXT DEFAULT 'N' ");
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion) );
    }
    
}
