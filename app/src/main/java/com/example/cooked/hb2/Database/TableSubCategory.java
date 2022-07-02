package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;

import java.util.ArrayList;
import java.util.List;

class TableSubCategory extends TableBase {
    TableSubCategory(SQLiteOpenHelper argHelper) {
        super(argHelper);
    }

    private void dropTableIfExists(SQLiteDatabase db) {
        String lSql = "DROP TABLE IF EXISTS tblSubCategory";
        executeSQL(lSql, "TableSubCategory::dropTableIfExists", db);
    }

    void onCreate(SQLiteDatabase db) {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE tblSubCategory " +
                        " (" +
                        "   SubCategoryId INTEGER PRIMARY KEY, " +
                        "   CategoryId INTEGER, " +
                        "   SubCategoryName TEXT, " +
                        "   SubCategoryType INTEGER, " +
                        "   Monitor TEXT, " +
                        "   Old TEXT  " +
                        ") ";

        executeSQL(lSql, "TableSubCategory::onCreate", db);
    }

    private int getNextSubCategoryId() {
        String lSql = "SELECT MAX(SubCategoryId) FROM tblSubCategory ";

        return (getMaxPlus1(lSql, "TableSubCategory::getNextSubCategoryId"));
    }

    void addSubCategory(RecordSubCategory rc) {
        rc.SubCategoryId = getNextSubCategoryId();

        String lMonitor="N";
        if(rc.Monitor)
                lMonitor="Y";

        String lSql =
                "INSERT INTO tblSubCategory " +
                        "(SubCategoryId, CategoryId, SubCategoryName, SubCategoryType, Monitor) " +
                        "VALUES (" +
                        Integer.toString(rc.SubCategoryId) + "," +
                        Integer.toString(rc.CategoryId) + "," +
                        "'" + rc.SubCategoryName + "', " +
                        Integer.toString(rc.SubCategoryType)  + ", " +
                        "'" + lMonitor + "' ) ";

        executeSQL(lSql, "TableSubCategory::addSubCategory", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void updateSubCategory(RecordSubCategory rc)
    {
        String lMonitor="N";
        if(rc.Monitor)
            lMonitor="Y";
        String lOld="N";
        if(rc.Old)
            lOld="Y";

        String lSql =
                "UPDATE tblSubCategory " +
                        "  SET SubCategoryName = '" + rc.SubCategoryName + "', " +
                        "  SubCategoryType = " + Integer.toString(rc.SubCategoryType) + "," +
                        "  Monitor = '" + lMonitor + "'," +
                        "  Old = '" + lOld + "'," +
                        "  CategoryId =  " + rc.CategoryId + " " +
                        "WHERE SubCategoryId = " + rc.SubCategoryId.toString();

        executeSQL(lSql, "TableSubCategory::updateSubCategory", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void deleteSubCategory(RecordSubCategory rc) {
        String lSql =
                "DELETE FROM tblSubCategory " +
                        "WHERE SubCategoryId = " + rc.SubCategoryId.toString();

        executeSQL(lSql, "TableSubCategory::deleteSubCategory", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void changeSubCategory(int oldSubCategoryId, int newSubCategoryId) {
        String lSql =
                "UPDATE tblCommon " +
                        "SET CategoryId = " + newSubCategoryId + " " +
                        "WHERE CategoryId = " + oldSubCategoryId;

        executeSQL(lSql, "TableSubCategory::changeSubCategory", null);

        lSql =
                "UPDATE tblPlanned " +
                        "SET SubCategoryId = " + newSubCategoryId + " " +
                        "WHERE SubCategoryId = " + oldSubCategoryId;

        executeSQL(lSql, "TableSubCategory::changeSubCategory", null);

        lSql =
                "UPDATE tblTransaction " +
                        "SET CategoryId = " + newSubCategoryId + " " +
                        "WHERE CategoryId = " + oldSubCategoryId;

        executeSQL(lSql, "TableSubCategory::changeSubCategory", null);
        MyDatabase.MyDB().Dirty=true;
    }

    ArrayList<RecordSubCategory> getSubCategoryList(Integer pCategoryId) {
        ArrayList<RecordSubCategory> list;
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor;
                String l_SQL;
                if (pCategoryId != 0)
                {
                    l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName, " +
                            "  a.SubCategoryType, a.Monitor, a.Old " +
                        "FROM tblSubCategory a, tblCategory b " +
                        "WHERE a.CategoryId = " + pCategoryId.toString() + " " +
                        "AND a.CategoryId = b.CategoryId " +
                        "ORDER BY b.CategoryName, a.SubCategoryName ";
                } else
                {
                    l_SQL = "SELECT a.SubCategoryId, a.CategoryId, a.SubCategoryName, b.CategoryName, " +
                            "  a.SubCategoryType, a.Monitor, a.Old " +
                        "FROM tblSubCategory a, tblCategory b " +
                        "WHERE a.CategoryId = b.CategoryId " +
                        "ORDER BY b.CategoryName, a.SubCategoryName ";
                }
                cursor = db.rawQuery(l_SQL, null);
                try
                {
                    list = new ArrayList<>();
                    if (cursor != null)
                    {
                        cursor.moveToFirst();
                        do
                        {
                            if (cursor.getCount() == 0)
                                break;
                            list.add
                                (
                                    new RecordSubCategory
                                        (
                                            Integer.parseInt(cursor.getString(1)),
                                            cursor.getString(3),
                                            Integer.parseInt(cursor.getString(0)),
                                            cursor.getString(2),
                                            Integer.parseInt(cursor.getString(4)),
                                            cursor.getString(5).compareTo("Y")==0,
                                                cursor.getString(6).compareTo("Y")==0
                                        )
                                );
                        } while (cursor.moveToNext());
                    }
                }
                finally
                {
                    assert cursor != null;
                    cursor.close();
                }
            }
        return list;
    }

    ArrayList<RecordSubCategory> getSubCategoryListWithOld(Integer pCategoryId, Boolean pOld) {
        ArrayList<RecordSubCategory> list;
        String lOld=" ";
        if(!pOld)
            lOld = " AND a.Old <> 'Y' ";

        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            Cursor cursor;
            String l_SQL;
            if (pCategoryId != 0)
            {
                l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName, " +
                        "  a.SubCategoryType, a.Monitor, a.Old " +
                        "FROM tblSubCategory a, tblCategory b " +
                        "WHERE a.CategoryId = " + pCategoryId.toString() + " " +
                        "AND a.CategoryId = b.CategoryId " +
                        lOld +
                        "ORDER BY b.CategoryName, a.SubCategoryName ";
            } else
            {
                l_SQL = "SELECT a.SubCategoryId, a.CategoryId, a.SubCategoryName, b.CategoryName, " +
                        "  a.SubCategoryType, a.Monitor, a.Old " +
                        "FROM tblSubCategory a, tblCategory b " +
                        "WHERE a.CategoryId = b.CategoryId " +
                        lOld +
                        "ORDER BY b.CategoryName, a.SubCategoryName ";
            }
            cursor = db.rawQuery(l_SQL, null);
            try
            {
                list = new ArrayList<>();
                if (cursor != null)
                {
                    cursor.moveToFirst();
                    do
                    {
                        if (cursor.getCount() == 0)
                            break;
                        list.add
                                (
                                        new RecordSubCategory
                                                (
                                                        Integer.parseInt(cursor.getString(1)),
                                                        cursor.getString(3),
                                                        Integer.parseInt(cursor.getString(0)),
                                                        cursor.getString(2),
                                                        Integer.parseInt(cursor.getString(4)),
                                                        cursor.getString(5).compareTo("Y")==0,
                                                        cursor.getString(6).compareTo("Y")==0
                                                )
                                );
                    } while (cursor.moveToNext());
                }
            }
            finally
            {
                assert cursor != null;
                cursor.close();
            }
        }
        return list;
    }

    RecordSubCategory getSubCategory(Integer pSubCategoryId) {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor;
                String l_SQL;
                l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName, " +
                        "  a.SubCategoryType, a.Monitor, a.Old " +
                    "FROM tblSubCategory a, tblCategory b " +
                    "WHERE a.SubCategoryId = " + pSubCategoryId.toString() + " " +
                    "AND a.CategoryId = b.CategoryId " +
                    "ORDER BY b.CategoryName, a.SubCategoryName ";
                cursor = db.rawQuery(l_SQL, null);
                if (cursor != null)
                {
                    try
                    {
                        cursor.moveToFirst();
                        if (cursor.getCount() > 0)
                        {
                            return
                                (
                                    new RecordSubCategory
                                        (
                                            Integer.parseInt(cursor.getString(1)),
                                            cursor.getString(3),
                                            Integer.parseInt(cursor.getString(0)),
                                            cursor.getString(2),
                                            Integer.parseInt(cursor.getString(4)),
                                                cursor.getString(5).compareTo("Y")==0,
                                                cursor.getString(6).compareTo("Y")==0
                                        )
                                );
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }
        return
                (
                        new RecordSubCategory
                                (
                                        0,
                                        "Unknown Category",
                                        pSubCategoryId,
                                        "Unknown SubCategory",
                                        0,
                                        false,
                                        false
                                )
                );
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 4 && newVersion == 5) {
            MyLog.WriteLogMessage("Creating table tblSubCategory");

            onCreate(db);
        }
        if (oldVersion == 8 && newVersion == 9) {
            MyLog.WriteLogMessage("Adding SubCategoryType to tblSubCategory");

            db.execSQL("ALTER TABLE tblSubCategory ADD COLUMN SubCategoryType INTEGER DEFAULT 0");
        }
        if (oldVersion == 19 && newVersion == 20) {
            MyLog.WriteLogMessage("Adding Monitor to tblSubCategory");

            db.execSQL("ALTER TABLE tblSubCategory ADD COLUMN Monitor TEXT DEFAULT 'N'");
        }
        if (oldVersion == 21 && newVersion == 22) {
            MyLog.WriteLogMessage("Adding Monitor to tblSubCategory");

            db.execSQL("ALTER TABLE tblSubCategory ADD COLUMN Old TEXT DEFAULT 'N'");
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion) );
    }
}
