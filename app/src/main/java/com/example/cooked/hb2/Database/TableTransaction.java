package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.Date;

class TableTransaction extends TableBase
{
    private SQLiteOpenHelper helper;

    TableTransaction(SQLiteOpenHelper argHelper)
    {
        helper = argHelper;
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        try
        {
            String lSql =
                "DROP TABLE IF EXISTS tblTransaction";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction::dropTableIfExists", e.getMessage());
        }
    }
 
    void onCreate(SQLiteDatabase db)
    {
        try
        {
            dropTableIfExists(db);
    
            String lSql =
                "CREATE TABLE tblTransaction " +
                    " (" +
                    "   TxSeqNo INTEGER PRIMARY KEY, " +
                    "   TxAdded INTEGER, " +
                    "   TxFilename TEXT, " +
                    "   TxLineNo INTEGER, " +
                    "   TxDate INTEGER, " +
                    "   TxType TEXT, " +
                    "   TxSortCode TEXT, " +
                    "   TxAccountNumber TEXT, " +
                    "   TxDescription TEXT, " +
                    "   TxAmount REAL, " +
                    "   TxBalance REAL, " +
                    "   CategoryId INTEGER " +
                    ") ";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction::onCreate", e.getMessage());
        }
    }

    public int getNextTxSeqNo()
    {
        try {
            String lString;
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("tblTransaction", new String[]{"MAX(TxSeqNo)"},
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
            ErrorDialog.Show("Error in TableTransaction::getNextTxSeqNo", e.getMessage());
        }
        return(1);
    }

    public void addTransaction(RecordTransaction rt)
    {
        try
        {
            rt.TxSeqNo = getNextTxSeqNo();

            MyLog.WriteLogMessage("Adding transaction." +
              "TxDate: " + rt.TxDate.toString() + ", " +
                    "TxDate: " + rt.TxDate.toString() + ", " +
                    "TxType: " + rt.TxType + ", " +
                    "TxSortCode: " + rt.TxSortCode + ", " +
                    "TxAccountNumber: " + rt.TxAccountNumber + ", " +
                    "TxDescription: " + rt.TxDescription + ", " +
                    "TxAmount: " + rt.TxAmount.toString()
            );

            String lSql =
                    "INSERT INTO tblTransaction " +
                    "(TxSeqNo, TxAdded, TxFilename, TxLineNo, TxDate, TxType, TxSortCode, " +
                    "TxAccountNumber, TxDescription, TxAmount, TxBalance) " +
                    "VALUES ("+
                    Integer.toString(rt.TxSeqNo) + "," +
                    Long.toString(rt.TxAdded.getTime()) + "," +
                    "'" + rt.TxFilename + "'," +
                    Integer.toString(rt.TxLineNo) + "," +
                    Long.toString(rt.TxDate.getTime()) + "," +
                    "'" + rt.TxType + "'," +
                    "'" + rt.TxSortCode + "'," +
                    "'" + rt.TxAccountNumber + "'," +
                    "'" + rt.TxDescription + "'," +
                    rt.TxAmount.toString() + ", " +
                    rt.TxBalance.toString() + ") ";

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction::addTransaction", e.getMessage());
        }
    }

    public void updateFilenameLineNo(RecordTransaction dbRec, RecordTransaction fileRec)
    {
        try
        {
            MyLog.WriteLogMessage("Updating transaction." +
              "TxDate: " + dbRec.TxDate.toString() + ", " +
                    "TxDate: " + dbRec.TxDate.toString() + ", " +
                    "TxType: " + dbRec.TxType + ", " +
                    "TxSortCode: " + dbRec.TxSortCode + ", " +
                    "TxAccountNumber: " + dbRec.TxAccountNumber + ", " +
                    "TxDescription: " + dbRec.TxDescription + ", " +
                    "TxAmount: " + dbRec.TxAmount.toString() + ".  " +
                "TxFilename: from " + dbRec.TxFilename + " to " + fileRec.TxFilename + ", " +
                "TxLineNo: from " + dbRec.TxLineNo.toString() + " to " + fileRec.TxLineNo.toString()
            );

            String lSql =
                    "UPDATE tblTransaction " +
                    " SET TxFilename = '" + fileRec.TxFilename + "', " +
                        " TxLineNo = " + fileRec.TxLineNo + " " +
                    "WHERE TxSeqNo = " + dbRec.TxSeqNo.toString();

            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction::updateFilenameLineNo", e.getMessage());
        }
    }

    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum)
    {
        int cnt;
        ArrayList<RecordTransaction> list;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                            "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                            "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                            "CategoryId"},
                    "TxSortCode=? AND TxAccountNumber=?",
                    new String[]{sortCode, accountNum}, null, null, "TxDate desc, TxLineNo", null);
            cnt = cursor.getCount();
            list = new ArrayList<RecordTransaction>();
            cnt = 0;
            if (cursor != null && cursor.getCount()>0)
            {
                cursor.moveToFirst();
                do
                {
                    list.add
                    (
                        new RecordTransaction
                            (
                                Integer.parseInt(cursor.getString(0)),
                                new Date(Long.parseLong(cursor.getString(1))),
                                cursor.getString(2),
                                Integer.parseInt(cursor.getString(3)),
                                new Date(Long.parseLong(cursor.getString(4))),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7),
                                cursor.getString(8),
                                Float.parseFloat(cursor.getString(9)),
                                Float.parseFloat(cursor.getString(10)),
                                Integer.parseInt(cursor.getString(11))
                            )
                    );
                    cnt++;
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            list = new ArrayList<RecordTransaction>();
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        return list;
    }

    public ArrayList<RecordTransaction> getTxDateRange(Date lFrom, Date lTo, String lSortCode,
                                                       String lAccountNumber)
    {
        int cnt;
        ArrayList<RecordTransaction> list;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                            "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                            "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                            "CategoryId"},
                    "TxDate>=? AND TxDate<=? AND TxSortCode=? AND TxAccountNumber=?",
                    new String[]{Long.toString(lFrom.getTime()), Long.toString(lTo.getTime()),
                    lSortCode, lAccountNumber},
                    null, null, "TxDate, TxLineNo", null);
            cnt = cursor.getCount();
            list = new ArrayList<RecordTransaction>();
            cnt = 0;
            if (cursor != null)
            {
                cursor.moveToFirst();
                do
                {
                    list.add
                            (
                                    new RecordTransaction
                                            (
                                                    Integer.parseInt(cursor.getString(0)),
                                                    new Date(Long.parseLong(cursor.getString(1))),
                                                    cursor.getString(2),
                                                    Integer.parseInt(cursor.getString(3)),
                                                    new Date(Long.parseLong(cursor.getString(4))),
                                                    cursor.getString(5),
                                                    cursor.getString(6),
                                                    cursor.getString(7),
                                                    cursor.getString(8),
                                                    Float.parseFloat(cursor.getString(9)),
                                                    Float.parseFloat(cursor.getString(10)),
                                                    Integer.parseInt(cursor.getString(11))
                                            )
                            );
                    cnt++;
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            list = new ArrayList<RecordTransaction>();
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        return list;
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion==1 && newVersion==2)
        {
            MyLog.WriteLogMessage("Altering tblTransaction - adding column CategoryId");
            db.execSQL("ALTER TABLE tblTransaction ADD COLUMN CategoryId INTEGER DEFAULT 0");
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
