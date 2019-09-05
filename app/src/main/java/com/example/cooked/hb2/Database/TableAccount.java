package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordAccount;

import java.util.ArrayList;

import static java.lang.Float.parseFloat;

class TableAccount extends TableBase
{
    TableAccount(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }

    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblAccount";
        executeSQL(lSql, "TableAccount::dropTableIfExists", db);
    }

    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE tblAccount " +
                        " (" +
                        "   AcSeqNo INTEGER PRIMARY KEY, " +
                        "   AcSortCode TEXT, " +
                        "   AcAccountNumber TEXT, " +
                        "   AcDescription TEXT, " +
                        "   AcStartingBalance REAL " +
                        ") ";

        executeSQL(lSql, "TableAccount::onCreate", db);
    }

    private int getNextTxSeqNo()
    {
        String lSql = "SELECT MAX(AcSeqNo) FROM tblAccount ";

        return (getMaxPlus1(lSql, "TableAccount::getNextTxSeqNo"));
    }

    private boolean acExists(RecordAccount ra)
    {
        String lSql = "SELECT AcSeqNo FROM tblAccount " +
                "WHERE AcSortCode = '" + ra.AcSortCode + "' " +
                "AND AcAccountNumber = '" + ra.AcAccountNumber + "' ";
        return (recordExists(lSql, "TableAccount::acExists"));
    }

    void addAccount(RecordAccount ra)
    {
        ra.AcSeqNo = getNextTxSeqNo();

        String lSql =
                "INSERT INTO tblAccount " +
                        "(AcSeqNo, AcSortCode, " +
                        "AcAccountNumber, AcDescription, AcStartingBalance) " +
                        "VALUES (" +
                        Integer.toString(ra.AcSeqNo) + "," +
                        "'" + ra.AcSortCode + "'," +
                        "'" + ra.AcAccountNumber + "'," +
                        "'" + ra.AcDescription + "'," +
                        ra.AcStartingBalance.toString() + " " +
                        ") ";

        executeSQL(lSql, "TableAccount::addAccount", null);
    }

    void updateAccount(RecordAccount ra)
    {
        String lSql =
                    "UPDATE tblAccount " +
                            "SET AcDescription = '" + ra.AcDescription + "', " +
                            " AcStartingBalance = " + ra.AcStartingBalance.toString() + " " +
                            "WHERE AcSeqNo = " + Integer.toString(ra.AcSeqNo);

        executeSQL(lSql, "TableAccount::updateAccount", null);

    }

    void deleteAccount(RecordAccount ra)
    {
        String lSql =
                "DELETE FROM tblAccount " +
                        "WHERE AcSeqNo = " + ra.AcSeqNo.toString();

        executeSQL(lSql, "TableAccoun t::deleteAccount", null);
    }

    ArrayList<RecordAccount> getAccountList()
    {
        ArrayList<RecordAccount> list;
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSeqNo, AcSortCode, AcAccountNumber, AcDescription, AcStartingBalance " +
                        "FROM tblAccount " +
                        "ORDER BY AcSortCode, AcAccountNumber";
                Cursor cursor = db.rawQuery(lSql, null);
                list=new ArrayList<>();
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                RecordAccount lrec =
                                        new RecordAccount
                                                (
                                                        Integer.parseInt(cursor.getString(0)),
                                                        cursor.getString(1),
                                                        cursor.getString(2),
                                                        cursor.getString(3),
                                                        parseFloat(cursor.getString(4))
                                                );
                                list.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }

        }
        catch (Exception e)
        {
            list = new ArrayList<>();
            ErrorDialog.Show("Error in TableAccount.getAccountList", e.getMessage());
        }
        return list;
    }

    RecordAccount getSingleAccount(Integer pAcSeqNo)
    {
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSeqNo, AcSortCode, AcAccountNumber, AcDescription, AcStartingBalance " +
                        "FROM tblAccount " +
                        "WHERE AcSeqNo = " + pAcSeqNo.toString();
                Cursor cursor = db.rawQuery(lSql, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            return (
                                new RecordAccount
                                        (
                                                Integer.parseInt(cursor.getString(0)),
                                                cursor.getString(1),
                                                cursor.getString(2),
                                                cursor.getString(3),
                                                parseFloat(cursor.getString(4))
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

        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TableAccount.getAcountItem", e.getMessage());
        }
        return (null);
    }

    RecordAccount getAccountItemByAccountNum(String pAcSortCode, String pAcAccountNumber)
    {
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSeqNo, AcSortCode, AcAccountNumber, AcDescription, AcStartingBalance " +
                        "FROM tblAccount " +
                        "WHERE AcSortCode = '" + pAcSortCode + "' " +
                        "AND AcAccountNumber = '" + pAcAccountNumber + "'";
                Cursor cursor = db.rawQuery(lSql, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            return (
                                    new RecordAccount
                                            (
                                                    Integer.parseInt(cursor.getString(0)),
                                                    cursor.getString(1),
                                                    cursor.getString(2),
                                                    cursor.getString(3),
                                                    parseFloat(cursor.getString(4))
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

        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TableAccount.getAcountItem", e.getMessage());
        }
        return (null);
    }

    public boolean accountExists(String pSortCode, String pAccountNumber)
    {
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSortCode " +
                        "FROM tblAccount " +
                        "WHERE AcSortCode = '" + pSortCode + "' " +
                        "AND AcAccountNumber = '" + pAccountNumber + "'";
                Cursor cursor = db.rawQuery(lSql, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            return(true);
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }

        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TableAccount.accountExists", e.getMessage());
        }
        return(false);
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 15 && newVersion == 16)
        {
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
