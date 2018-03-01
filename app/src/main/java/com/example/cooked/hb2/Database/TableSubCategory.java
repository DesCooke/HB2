package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;

public class TableSubCategory extends TableBase {
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
                        "   SubCategoryType INTEGER " +
                        ") ";

        executeSQL(lSql, "TableSubCategory::onCreate", db);
    }

    public int getNextSubCategoryId() {
        String lSql = "SELECT MAX(SubCategoryId) FROM tblSubCategory ";

        return (getMaxPlus1(lSql, "TableSubCategory::getNextSubCategoryId"));
    }

    public void addSubCategory(RecordSubCategory rc) {
        rc.SubCategoryId = getNextSubCategoryId();

        String lSql =
                "INSERT INTO tblSubCategory " +
                        "(SubCategoryId, CategoryId, SubCategoryName, SubCategoryType) " +
                        "VALUES (" +
                        Integer.toString(rc.SubCategoryId) + "," +
                        Integer.toString(rc.CategoryId) + "," +
                        "'" + rc.SubCategoryName + "', " +
                        Integer.toString(rc.SubCategoryType)  + ") ";

        executeSQL(lSql, "TableSubCategory::addSubCategory", null);
    }

    public void updateSubCategory(RecordSubCategory rc) {
        String lSql =
                "UPDATE tblSubCategory " +
                        "  SET SubCategoryName = '" + rc.SubCategoryName + "', " +
                        "  SubCategoryType = " + Integer.toString(rc.SubCategoryType) + " " +
                        "WHERE SubCategoryId = " + rc.SubCategoryId.toString();

        executeSQL(lSql, "TableSubCategory::updateSubCategory", null);
    }

    public void deleteSubCategory(RecordSubCategory rc) {
        String lSql =
                "DELETE FROM tblSubCategory " +
                        "WHERE SubCategoryId = " + rc.SubCategoryId.toString();

        executeSQL(lSql, "TableSubCategory::deleteSubCategory", null);
    }

    public ArrayList<RecordSubCategory> getSubCategoryList(Integer pCategoryId) {
        int cnt;
        ArrayList<RecordSubCategory> list;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor;
                String l_SQL;
                if (pCategoryId != 0) {
                    l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName, a.SubCategoryType " +
                            "FROM tblSubCategory a, tblCategory b " +
                            "WHERE a.CategoryId = " + pCategoryId.toString() + " " +
                            "AND a.CategoryId = b.CategoryId " +
                            "ORDER BY b.CategoryName, a.SubCategoryName ";
                } else {
                    l_SQL = "SELECT a.SubCategoryId, a.CategoryId, a.SubCategoryName, b.CategoryName, a.SubCategoryType " +
                            "FROM tblSubCategory a, tblCategory b " +
                            "WHERE a.CategoryId = b.CategoryId " +
                            "ORDER BY b.CategoryName, a.SubCategoryName ";
                }
                cursor = db.rawQuery(l_SQL, null);
                try {
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
                                                            cursor.getString(2),
                                                            Integer.parseInt(cursor.getString(4))
                                                    )
                                    );
                            cnt++;
                        } while (cursor.moveToNext());
                    }
                } finally {
                    cursor.close();
                }
            } finally {
                db.close();
            }

        } catch (Exception e) {
            list = new ArrayList<RecordSubCategory>();
            ErrorDialog.Show("Error in TableSubCategory.getSubCategoryList", e.getMessage());
        }
        return list;
    }

    public RecordSubCategory getSubCategory(Integer pSubCategoryId) {
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor;
                String l_SQL;
                l_SQL = "SELECT a.SubCategoryId, a.CategoryId, SubCategoryName, b.CategoryName, a.SubCategoryType " +
                        "FROM tblSubCategory a, tblCategory b " +
                        "WHERE a.SubCategoryId = " + pSubCategoryId.toString() + " " +
                        "AND a.CategoryId = b.CategoryId " +
                        "ORDER BY b.CategoryName, a.SubCategoryName ";
                cursor = db.rawQuery(l_SQL, null);
                if (cursor != null) {
                    try {

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
                                                            cursor.getString(2),
                                                            Integer.parseInt(cursor.getString(4))
                                                    )
                                    );
                        } while (cursor.moveToNext());
                    } finally {
                        cursor.close();
                    }
                }
            } finally {
                db.close();
            }

        } catch (Exception e) {
            ErrorDialog.Show("Error in TableSubCategory.getSubCategoryList", e.getMessage());
        }
        return
                (
                        new RecordSubCategory
                                (
                                        0,
                                        "Unknown Category",
                                        pSubCategoryId,
                                        "Unknown SubCategory",
                                        0
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
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
