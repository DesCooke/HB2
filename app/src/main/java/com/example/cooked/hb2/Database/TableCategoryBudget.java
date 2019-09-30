package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;

class TableCategoryBudget extends TableBase
{

    TableCategoryBudget(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }

    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblCategoryBudget";
        executeSQL(lSql, "TableCategoryBudget::dropTableIfExists", db);
    }

    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE tblCategoryBudget " +
                        " (" +
                        "   RecordId INTEGER PRIMARY KEY," +
                        "   CategoryId INTEGER, " +
                        "   BudgetMonth INTEGER, " +
                        "   BudgetYear INTEGER, " +
                        "   BudgetAmount REAL " +
                        ") ";

        executeSQL(lSql, "TableCategoryBudget::onCreate", db);
    }

    private int getNextRecordId()
    {
        String lSql = "SELECT MAX(RecordId) FROM tblCategoryBudget ";

        return (getMaxPlus1(lSql, "TableCategoryBudget::getNextRecordId"));
    }

    void addCategoryBudget(RecordCategoryBudget rcb)
    {
        int lNextRecordId = getNextRecordId();

        String lSql =
                "INSERT INTO tblCategoryBudget " +
                        "(RecordId, CategoryId, BudgetMonth, BudgetYear, BudgetAmount) " +
                        "VALUES (" +
                        Integer.toString(lNextRecordId) + ", " +
                        Integer.toString(rcb.CategoryId) + "," +
                        rcb.BudgetMonth.toString() + "," +
                        rcb.BudgetYear.toString() + "," +
                        rcb.BudgetAmount.toString() + " " +
                        ") ";

        executeSQL(lSql, "TableCategoryBudget::addCategoryBudget", null);
    }

    void updateCategoryBudget(RecordCategoryBudget rcb)
    {
        String lSql =
                "UPDATE tblCategoryBudget " +
                        "  SET BudgetAmount = " + rcb.BudgetAmount.toString() + " " +
                        "WHERE CategoryId = " + rcb.CategoryId.toString() + " " +
                        "AND BudgetMonth = " + rcb.BudgetMonth.toString() + " " +
                        "and BudgetYear = " + rcb.BudgetYear.toString() + " ";

        executeSQL(lSql, "TableCategoryBudget::updateCategoryBudget", null);
    }

    void deleteCategoryBudget(RecordCategoryBudget rcb)
    {
        String lSql =
                "DELETE FROM tblCategoryBudget " +
                        "WHERE CategoryId = " + rcb.CategoryId.toString() + " " +
                        "AND BudgetMonth = " + rcb.BudgetMonth.toString() + " " +
                        "and BudgetYear = " + rcb.BudgetYear.toString() + " ";

        executeSQL(lSql, "TableCategoryBudget::deleteCategoryBudget", null);
    }

    void deleteAllForCategory(Integer pCategoryId)
    {
        String lSql =
                "DELETE FROM tblCategoryBudget " +
                        "WHERE CategoryId = " + pCategoryId.toString();

        executeSQL(lSql, "TableCategoryBudget::deleteAllForCategory", null);
    }

    void deleteAll()
    {
        String lSql =
                "DELETE FROM tblCategoryBudget ";

        executeSQL(lSql, "TableCategoryBudget::deleteAll", null);
    }

    RecordCategoryBudget getCategoryBudget(Integer pCategoryId, Integer pBudgetMonth, Integer pBudgetYear)
    {
        RecordCategoryBudget item;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            try
            {
                String lString =
                        "SELECT BudgetAmount " +
                                "FROM tblCategoryBudget " +
                                "WHERE CategoryId = " + pCategoryId.toString() + " " +
                                "AND BudgetMonth = " + pBudgetMonth.toString() + " " +
                                "and BudgetYear = " + pBudgetYear.toString() + " ";
                Cursor cursor = db.rawQuery(lString, null);
                item = new RecordCategoryBudget();
                if (cursor != null)
                {
                    try
                    {
                        cursor.moveToFirst();
                        item = new RecordCategoryBudget
                                (
                                        pCategoryId,
                                        pBudgetMonth,
                                        pBudgetYear,
                                        Float.parseFloat(cursor.getString(0))
                                );
                    } finally
                    {
                        cursor.close();
                    }
                }
            } finally
            {
                db.close();
            }
        }
        return item;
    }


    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 11 && newVersion == 12)
        {
            MyLog.WriteLogMessage("Creating table tblCategoryBudget");
            onCreate(db);
        }
        if (oldVersion == 12 && newVersion == 13)
        {
            MyLog.WriteLogMessage("Creating table tblCategoryBudget");
            onCreate(db);
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion));
    }

}
