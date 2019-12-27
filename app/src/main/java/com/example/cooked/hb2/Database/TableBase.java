package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class TableBase
{
    SQLiteOpenHelper helper;
    
    TableBase(SQLiteOpenHelper argHelper)
    {
        helper = argHelper;
    }
    
    void executeSQL(String pSql, String pLocation, SQLiteDatabase pdb)
    {
        SQLiteDatabase ldb;
            ldb = pdb;
            if (pdb == null)
                ldb = helper.getWritableDatabase();
            try
            {
                MyLog.WriteLogMessage("executeSQL: " + pLocation + ":" + pSql);
                ldb.execSQL(pSql);
            }
            finally
            {
                if (pdb == null)
                    ldb.close();
            }
    }
    
    Integer getMaxPlus1(String pSql, String pLocation)
    {
            MyLog.WriteLogMessage("getMaxPlus1: " + pLocation + ":" + pSql);
            
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    Cursor cursor = db.rawQuery(pSql, null);
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            if (cursor.getCount() > 0)
                            {
                                String lString = cursor.getString(0);
                                if (lString != null)
                                    return (Integer.parseInt(cursor.getString(0)) + 1);
                            }
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
        return (1);
    }
    
    Integer getMinMinus1(String pSql, String pLocation)
    {
            MyLog.WriteLogMessage("getMinMinus1: " + pLocation + ":" + pSql);
            
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    Cursor cursor = db.rawQuery(pSql, null);
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            if (cursor.getCount() > 0)
                            {
                                String lString = cursor.getString(0);
                                if (lString != null)
                                    return (Integer.parseInt(cursor.getString(0)) - 1);
                            }
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
        return (-1);
    }
    
    Boolean recordExists(String pSql, String pLocation)
    {
            MyLog.WriteLogMessage("recordExists: " + pLocation + ":" + pSql);
    
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.rawQuery(pSql, null);
                if (cursor != null)
                {
                    try
                    {
                        cursor.moveToFirst();
                        if (cursor.getCount() > 0)
                            return (TRUE);
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }
            
        return (FALSE);
    }
    
    
}

 