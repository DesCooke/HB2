package com.example.cooked.hb2.Database;

import android.database.sqlite.SQLiteDatabase;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;

class TableTransaction extends TableBase
{
    TableTransaction()
    {
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
                    "   TxDebit REAL, " +
                    "   TxCredit REAL, " +
                    "   TxBalance REAL " +
                    ") ";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction::onCreate", e.getMessage());
        }
    }
    
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
