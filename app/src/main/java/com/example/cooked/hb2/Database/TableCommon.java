package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.Date;

class TableCommon extends TableBase
{
    TableCommon(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblCommon";
        executeSQL(lSql, "TableCommon::dropTableIfExists", db);
    }
    
    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);
        
        String lSql =
            "CREATE TABLE tblCommon " +
                " (" +
                "   TxSeqNo INTEGER PRIMARY KEY, " +
                "   TxDate INTEGER, " +
                "   TxDescription TEXT, " +
                "   TxAmount REAL, " +
                "   CategoryId INTEGER, " +
                "   Comments TEXT " +
                ") ";
        
        executeSQL(lSql, "TableCommon::onCreate", db);
    }
    
    private int getNextTxSeqNo()
    {
        String lSql = "SELECT MAX(TxSeqNo) FROM tblCommon ";
        
        return (getMaxPlus1(lSql, "TableCommon::getNextTxSeqNo"));
    }
    
    void addTransaction(RecordCommon rt)
    {
        rt.TxSeqNo = getNextTxSeqNo();
        
        String lSql =
            "INSERT INTO tblCommon " +
                "(TxSeqNo, TxDate,  " +
                "TxDescription, TxAmount, CategoryId, " +
                "Comments) " +
                "VALUES (" +
                Integer.toString(rt.TxSeqNo) + "," +
                Long.toString(rt.TxDate.getTime()) + "," +
                "'" + rt.TxDescription + "'," +
                rt.TxAmount.toString() + ", " +
                Integer.toString(rt.CategoryId) + ", " +
                "'" + rt.Comments + "' "  +
        ") ";
        
        executeSQL(lSql, "TableCommon::addCommon", null);
    }
    
    void updateTransaction(RecordCommon rt)
    {
        String lSql =
            "UPDATE tblCommon " +
                    "SET TxDate = " + Long.toString(rt.TxDate.getTime()) + ", " +
                    "TxDescription = '" + rt.TxDescription + "'," +
                    "TxAmount = " + rt.TxAmount.toString() + ", " +
                    "CategoryId = " + Integer.toString(rt.CategoryId) + ", " +
                    "Comments = '" + rt.Comments + "' " +
                    "WHERE TxSeqNo = " + Integer.toString(rt.TxSeqNo);
            
        executeSQL(lSql, "TableCommon::updateCommon", null);
    }
    
    void deleteTransaction(RecordCommon rec)
    {
        String lSql =
            "DELETE FROM tblCommon " +
                "WHERE TxSeqNo = " + rec.TxSeqNo.toString();
        
        executeSQL(lSql, "TableCommon::deleteCommon", null);
    }
    
    ArrayList<RecordCommon> getTransactionList()
    {
        ArrayList<RecordCommon> list;
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblCommon", new String[]{"TxSeqNo",
                        "TxDate", "TxDescription", "TxAmount",
                        "CategoryId", "Comments"},
                    null,
                    null, null, null, "TxDescription", null);
                list = new ArrayList<>();
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                RecordCommon lrec =
                                    new RecordCommon
                                        (
                                            Integer.parseInt(cursor.getString(0)),
                                            new Date(Long.parseLong(cursor.getString(1))),
                                            cursor.getString(2),
                                            Float.parseFloat(cursor.getString(3)),
                                            Integer.parseInt(cursor.getString(4)),
                                            cursor.getString(5)
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

    RecordCommon getSingleCommonTransaction(Integer pTxSeqNo)
    {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblCommon", new String[]{"TxSeqNo",
                        "TxDate", "TxDescription", "TxAmount",
                        "CategoryId", "Comments"},
                    "TxSeqNo=?",
                    new String[]{Integer.toString(pTxSeqNo)},
                    null, null, null, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            return (
                                new RecordCommon
                                    (
                                        Integer.parseInt(cursor.getString(0)),
                                        new Date(Long.parseLong(cursor.getString(1))),
                                        cursor.getString(2),
                                        Float.parseFloat(cursor.getString(3)),
                                        Integer.parseInt(cursor.getString(4)),
                                        cursor.getString(5)
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
    
    RecordCommon getSingleCommonTransaction(String pTxDescription)
    {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblCommon", new String[]{"TxSeqNo",
                        "TxDate", "TxDescription", "TxAmount",
                        "CategoryId", "Comments"},
                    "TxDescription=?",
                    new String[]{pTxDescription},
                    null, null, null, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            return (
                                new RecordCommon
                                    (
                                        Integer.parseInt(cursor.getString(0)),
                                        new Date(Long.parseLong(cursor.getString(1))),
                                        cursor.getString(2),
                                        Float.parseFloat(cursor.getString(3)),
                                        Integer.parseInt(cursor.getString(4)),
                                        cursor.getString(5)
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

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 9 && newVersion == 10)
        {
            MyLog.WriteLogMessage("Creating tblCommon");
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
