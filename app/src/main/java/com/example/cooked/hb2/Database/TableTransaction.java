package com.example.cooked.hb2.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;

/**
 * Created by cooked on 14/06/2017.
 */

public class TableTransaction extends TableBase
{
    public TableTransaction()
    {
    }
    
    public void dropTableIfExists(SQLiteDatabase db)
    {
        try
        {
            String lSql =
                "DROP TABLE IF EXISTS Transaction";
    
            db.execSQL(lSql);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction::dropTableIfExists", e.getMessage());
        }
    }
 
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            dropTableIfExists(db);
    
            String lSql =
                "CREATE TABLE Transaction " +
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
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
