package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                        "   AcStartingBalance REAL, " +
                        "   OrderSeqNo INTEGER DEFAULT 0 " +
                        "   Hidden INTEGER DEFAULT 0, " +
                        "   UseCategory INTEGER DEFAULT 1 " +
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

        Integer lUseCategory=0;
        if(ra.AcUseCategory)
            lUseCategory=1;
        String lSql =
                "INSERT INTO tblAccount " +
                        "(AcSeqNo, AcSortCode, " +
                        "AcAccountNumber, AcDescription, AcStartingBalance, OrderSeqNo, Hidden, UseCategory) " +
                        "VALUES (" +
                        ra.AcSeqNo + "," +
                        "'" + ra.AcSortCode + "'," +
                        "'" + ra.AcAccountNumber + "'," +
                        "'" + ra.AcDescription + "'," +
                        ra.AcStartingBalance.toString() + ", " +
                        ra.AcOrderSeqNo.toString() + ", " +
                        ra.AcHidden.toString() + ", " +
                        lUseCategory.toString() + " " +
                        ") ";

        executeSQL(lSql, "TableAccount::addAccount", null);
    }

    void updateAccount(RecordAccount ra)
    {
        Integer lUseCategory=0;
        if(ra.AcUseCategory)
            lUseCategory=1;

        String lSql =
                    "UPDATE tblAccount " +
                            "SET AcDescription = '" + ra.AcDescription + "', " +
                            " AcStartingBalance = " + ra.AcStartingBalance.toString() + ", " +
                            " OrderSeqNo = " + ra.AcOrderSeqNo.toString() + ", " +
                            " Hidden = " + ra.AcHidden.toString() + ", " +
                            " UseCategory = " + lUseCategory.toString() + " " +
                            "WHERE AcSeqNo = " + ra.AcSeqNo;

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
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSeqNo, AcSortCode, AcAccountNumber, AcDescription, " +
                        "AcStartingBalance, OrderSeqno, Hidden, UseCategory " +
                        "FROM tblAccount " +
                        "WHERE Hidden = 0 " +
                        "ORDER BY OrderSeqNo, AcSortCode, AcAccountNumber";
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
                                                        parseFloat(cursor.getString(4)),
                                                        Integer.parseInt(cursor.getString(5)),
                                                        Integer.parseInt(cursor.getString(6)),
                                                        Integer.parseInt(cursor.getString(7))==1
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

        return list;
    }

    public void unhideAllAccounts()
    {
        String lSql = "UPDATE tblAccount SET Hidden = 0 ";

        executeSQL(lSql, "TableAccount::updateAccount", null);
    }


    RecordAccount getSingleAccount(Integer pAcSeqNo)
    {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSeqNo, AcSortCode, AcAccountNumber, AcDescription, " +
                        "AcStartingBalance, OrderSeqNo, Hidden, UseCategory " +
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
                                                parseFloat(cursor.getString(4)),
                                                Integer.parseInt(cursor.getString(5)),
                                                Integer.parseInt(cursor.getString(6)),
                                                Integer.parseInt(cursor.getString(7))==1
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

        return (null);
    }

    RecordAccount getAccountItemByAccountNum(String pAcSortCode, String pAcAccountNumber)
    {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "select AcSeqNo, AcSortCode, AcAccountNumber, AcDescription, " +
                        "AcStartingBalance, OrderSeqNo, Hidden, UseCategory " +
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
                                                    parseFloat(cursor.getString(4)),
                                                    Integer.parseInt(cursor.getString(5)),
                                                    Integer.parseInt(cursor.getString(6)),
                                                    Integer.parseInt(cursor.getString(7))==1
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

        return (null);
    }

    public boolean accountExists(String pSortCode, String pAccountNumber)
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

        return(false);
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 15 && newVersion == 16)
        {
            onCreate(db);
        }
        if (oldVersion == 16 && newVersion == 17)
        {
            String lSql =
                    "ALTER TABLE tblAccount " +
                            " ADD COLUMN OrderSeqNo INTEGER DEFAULT 0 ";

            executeSQL(lSql, "TableAccount::onUpgrade", db);

            lSql =
                    "ALTER TABLE tblAccount " +
                            " ADD COLUMN Hidden INTEGER DEFAULT 0 ";

            executeSQL(lSql, "TableAccount::onUpgrade", db);
        }
        if (oldVersion == 24 && newVersion == 25)
        {
            String lSql =
                    "ALTER TABLE tblAccount " +
                    " ADD COLUMN UseCategory INTEGER DEFAULT 1 ";

            executeSQL(lSql, "TableAccount::onUpgrade", db);

            lSql =
                    "UPDATE tblAccount " +
                    " SET UseCategory = 1 ";

            executeSQL(lSql, "TableAccount::onUpgrade", db);
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion) );
    }

    public void accountResequence(ArrayList<RecordAccount> raa)
    {
        int lSeqNo=1;
        for (int i = 0; i < raa.size(); i++)
        {
            RecordAccount ra = raa.get(i);
            ra.AcOrderSeqNo=lSeqNo;
            updateAccount(ra);
            lSeqNo++;
        }
    }

}
