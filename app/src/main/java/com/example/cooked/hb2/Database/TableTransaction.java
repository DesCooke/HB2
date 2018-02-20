package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class TableTransaction extends TableBase
{
    TableTransaction(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblTransaction";
        executeSQL(lSql, "TableTransaction::dropTableIfExists", db);
    }
    
    void onCreate(SQLiteDatabase db)
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
                "   CategoryId INTEGER, " +
                "   Comments TEXT, " +
                "   BudgetYear INTEGER, " +
                "   BudgetMonth INTEGER " +
                ") ";
        
        executeSQL(lSql, "TableTransaction::onCreate", db);
    }
    
    public int getNextTxSeqNo()
    {
        String lSql = "SELECT MAX(TxSeqNo) FROM tblTransaction ";
        
        return (getMaxPlus1(lSql, "TableTransaction::getNextTxSeqNo"));
    }
    
    public int getNextTxLineNo(Date pDate)
    {
        String lSql = "SELECT MIN(TxLineNo) FROM tblTransaction " +
            "WHERE TxDate = " + Long.toString(pDate.getTime()) + " " +
            "AND TxSortCode = 'Cash'";
        
        return (getMinMinus1(lSql, "TableTransaction::getNextTxLineNo"));
    }
    
    public boolean txExists(RecordTransaction rt)
    {
        String lSql = "SELECT TxSeqNo FROM tblTransaction " +
            "WHERE TxDate = " + Long.toString(rt.TxDate.getTime()) + " " +
            "AND TxType = '" + rt.TxType + "' " +
            "AND TxDescription = '" + rt.TxDescription + "' " +
            "AND TxAmount = " + Float.toString(rt.TxAmount) + " ";
        return (recordExists(lSql, "TableTransaction::getNextTxLineNo"));
    }
    
    public void addTransaction(RecordTransaction rt)
    {
        rt.TxSeqNo = getNextTxSeqNo();
        
        String lSql =
            "INSERT INTO tblTransaction " +
                "(TxSeqNo, TxAdded, TxFilename, TxLineNo, TxDate, TxType, TxSortCode, " +
                "TxAccountNumber, TxDescription, TxAmount, TxBalance, CategoryId, " +
                "Comments, BudgetYear, BudgetMonth) " +
                "VALUES (" +
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
                rt.TxBalance.toString() + ", " +
                Integer.toString(rt.CategoryId) + ", " +
                "'" + rt.Comments + "', " +
                Integer.toString(rt.BudgetYear) + ", " +
                Integer.toString(rt.BudgetMonth) + " " +
        ") ";
        
        executeSQL(lSql, "TableTransaction::addTransaction", null);
    }
    
    public void updateTransaction(RecordTransaction rt)
    {
        if (rt.TxSortCode.compareTo("Cash") == 0)
        {
            String lSql =
                "UPDATE tblTransaction " +
                    "SET TxDate = " + Long.toString(rt.TxDate.getTime()) + ", " +
                    "TxDescription = '" + rt.TxDescription + "'," +
                    "TxAmount = " + rt.TxAmount.toString() + ", " +
                    "CategoryId = " + Integer.toString(rt.CategoryId) + ", " +
                    "Comments = '" + rt.Comments + "'," +
                    "BudgetYear = " + Integer.toString(rt.BudgetYear) + ", " +
                    "BudgetMonth = " + Integer.toString(rt.BudgetMonth) + " " +
                    "WHERE TxSeqNo = " + Integer.toString(rt.TxSeqNo);
            
            executeSQL(lSql, "TableTransaction::updateTransaction", null);
            
        } else
        {
            String lSql =
                "UPDATE tblTransaction " +
                    "SET CategoryId = " + Integer.toString(rt.CategoryId) + ", " +
                    "Comments = '" + rt.Comments + "'," +
                    "BudgetYear = " + Integer.toString(rt.BudgetYear) + ", " +
                    "BudgetMonth = " + Integer.toString(rt.BudgetMonth) + " " +
                    "WHERE TxSeqNo = " + Integer.toString(rt.TxSeqNo);
            
            executeSQL(lSql, "TableTransaction::updateTransaction", null);
            
            MyLog.WriteLogMessage("Amending transaction, SubCategoryName is ("+rt.SubCategoryName+")");
            
            if (rt.SubCategoryName.compareTo("Cash Transfer") == 0)
            {
                MyLog.WriteLogMessage("It is a Cash Transfer - so do we need to create a record in Cash Account?");
                rt.TxSortCode = "Cash";
                rt.TxAccountNumber = "Cash";
                rt.TxAmount = rt.TxAmount * -1;
                if (txExists(rt) == FALSE)
                {
                    MyLog.WriteLogMessage("--yes");
                    rt.TxLineNo = getNextTxLineNo(rt.TxDate);
                    addTransaction(rt);
                }
                else
                {
                    MyLog.WriteLogMessage("--no");
                }
            }
        }
    }
    
    public void updateFilenameLineNo(RecordTransaction dbRec, RecordTransaction fileRec)
    {
        String lSql =
            "UPDATE tblTransaction " +
                " SET TxFilename = '" + fileRec.TxFilename + "', " +
                " TxLineNo = " + fileRec.TxLineNo + " " +
                "WHERE TxSeqNo = " + dbRec.TxSeqNo.toString();
        
        executeSQL(lSql, "TableTransaction::updateFilenameLineNo", null);
    }
    
    public void deleteTransaction(RecordTransaction rec)
    {
        String lSql =
            "DELETE FROM tblTransaction " +
                "WHERE TxSeqNo = " + rec.TxSeqNo.toString();
        
        executeSQL(lSql, "TableTransaction::deleteTransaction", null);
    }
    
    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum)
    {
        int cnt;
        ArrayList<RecordTransaction> list;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                        "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                        "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                        "CategoryId","Comments", "BudgetYear", "BudgetMonth"},
                    "TxSortCode=? AND TxAccountNumber=?",
                    new String[]{sortCode, accountNum}, null, null, "TxDate desc, TxLineNo", null);
                list = new ArrayList<RecordTransaction>();
                cnt = 0;
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                RecordTransaction lrec =
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
                                            Integer.parseInt(cursor.getString(11)),
                                            cursor.getString(12),
                                            Integer.parseInt(cursor.getString(13)),
                                            Integer.parseInt(cursor.getString(14))
                                        );
                                list.add(lrec);
                                cnt++;
                            } while (cursor.moveToNext());
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
        catch (Exception e)
        {
            list = new ArrayList<RecordTransaction>();
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        if (sortCode.compareTo("Cash") == 0)
        {
            Float lBal = new Float(0.00);
            for (int i = list.size() - 1; i >= 0; i--)
            {
                lBal = lBal + list.get(i).TxAmount;
                list.get(i).TxBalance = lBal;
            }
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
            try
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                        "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                        "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                        "CategoryId", "Comments", "BudgetYear", "BudgetMonth"},
                    "TxDate>=? AND TxDate<=? AND TxSortCode=? AND TxAccountNumber=?",
                    new String[]{Long.toString(lFrom.getTime()), Long.toString(lTo.getTime()),
                        lSortCode, lAccountNumber},
                    null, null, "TxDate, TxLineNo", null);
                list = new ArrayList<RecordTransaction>();
                cnt = 0;
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
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
                                                Integer.parseInt(cursor.getString(11)),
                                                cursor.getString(12),
                                                Integer.parseInt(cursor.getString(13)),
                                                Integer.parseInt(cursor.getString(14))
                                            )
                                    );
                                cnt++;
                            } while (cursor.moveToNext());
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
        catch (Exception e)
        {
            list = new ArrayList<RecordTransaction>();
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        return list;
    }
    
    public RecordTransaction getSingleTransaction(Integer pTxSeqNo)
    {
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                        "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                        "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                        "CategoryId", "Comments", "BudgetYear", "BudgetMonth"},
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
                                        Integer.parseInt(cursor.getString(11)),
                                        cursor.getString(12),
                                        Integer.parseInt(cursor.getString(13)),
                                        Integer.parseInt(cursor.getString(14))
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
            finally
            {
                db.close();
            }
            
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        return (null);
    }
    
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 1 && newVersion == 2)
        {
            MyLog.WriteLogMessage("Altering tblTransaction - adding column CategoryId");
            db.execSQL("ALTER TABLE tblTransaction ADD COLUMN CategoryId INTEGER DEFAULT 0");
        }
        if (oldVersion == 5 && newVersion == 6)
        {
            MyLog.WriteLogMessage("Altering tblTransaction - adding columns Comments, BudgetYear, BudgetMonth");
            db.execSQL("ALTER TABLE tblTransaction ADD COLUMN Comments TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE tblTransaction ADD COLUMN BudgetYear INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE tblTransaction ADD COLUMN BudgetMonth INTEGER DEFAULT 0");
        }
    }
    
    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
