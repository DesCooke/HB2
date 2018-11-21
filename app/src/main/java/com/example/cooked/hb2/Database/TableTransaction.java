package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Float.parseFloat;

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
    
    private int getNextTxSeqNo()
    {
        String lSql = "SELECT MAX(TxSeqNo) FROM tblTransaction ";
        
        return (getMaxPlus1(lSql, "TableTransaction::getNextTxSeqNo"));
    }
    
    int getNextTxLineNo(Date pDate)
    {
        String lSql = "SELECT MIN(TxLineNo) FROM tblTransaction " +
            "WHERE TxDate = " + Long.toString(pDate.getTime()) + " " +
            "AND TxSortCode = 'Cash'";
        
        return (getMinMinus1(lSql, "TableTransaction::getNextTxLineNo"));
    }
    
    private boolean txExists(RecordTransaction rt)
    {
        String lSql = "SELECT TxSeqNo FROM tblTransaction " +
            "WHERE TxDate = " + Long.toString(rt.TxDate.getTime()) + " " +
            "AND TxType = '" + rt.TxType + "' " +
            "AND TxDescription = '" + rt.TxDescription + "' " +
            "AND TxAmount = " + Float.toString(rt.TxAmount) + " ";
        return (recordExists(lSql, "TableTransaction::getNextTxLineNo"));
    }
    
    void addTransaction(RecordTransaction rt)
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
    
    void updateTransaction(RecordTransaction rt)
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
    
    void updateFilenameLineNo(RecordTransaction dbRec, RecordTransaction fileRec)
    {
        String lSql =
            "UPDATE tblTransaction " +
                " SET TxFilename = '" + fileRec.TxFilename + "', " +
                " TxLineNo = " + fileRec.TxLineNo + " " +
                "WHERE TxSeqNo = " + dbRec.TxSeqNo.toString();
        
        executeSQL(lSql, "TableTransaction::updateFilenameLineNo", null);
    }
    
    void deleteTransaction(RecordTransaction rec)
    {
        String lSql =
            "DELETE FROM tblTransaction " +
                "WHERE TxSeqNo = " + rec.TxSeqNo.toString();
        
        executeSQL(lSql, "TableTransaction::deleteTransaction", null);
    }
    
    ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum)
    {
        ArrayList<RecordTransaction> list;
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                        "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                        "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                        "CategoryId", "Comments", "BudgetYear", "BudgetMonth"},
                    "TxSortCode=? AND TxAccountNumber=?",
                    new String[]{sortCode, accountNum}, null, null, "TxDate desc, TxLineNo", null);
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
                                            parseFloat(cursor.getString(9)),
                                            parseFloat(cursor.getString(10)),
                                            Integer.parseInt(cursor.getString(11)),
                                            cursor.getString(12),
                                            Integer.parseInt(cursor.getString(13)),
                                            Integer.parseInt(cursor.getString(14)),
                                            false
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
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        if (sortCode.compareTo("Cash") == 0)
        {
            Float lBal = 0.00f;
            for (int i = list.size() - 1; i >= 0; i--)
            {
                lBal = lBal + list.get(i).TxAmount;
                list.get(i).TxBalance = lBal;
            }
        }
        return list;
    }

    Date getEarliestTxDate(String sortCode, String accountNum,
                              Integer budgetMonth, Integer budgetYear)
    {
        try
        {
            MyLog.WriteLogMessage("getEarliestTxDate: ");

            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    String lSql = "select IFNULL(MIN(TxDate),0) FROM tblTransaction " +
                            "WHERE TxSortCode = '" + sortCode + "' " +
                            "AND TxAccountNumber = '" + accountNum + "' " +
                            "AND BudgetYear in (" + budgetYear.toString() + ") " +
                            "AND BudgetMonth in (" + budgetMonth.toString() + ") ";
                    Cursor cursor = db.rawQuery(lSql, null);
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            if (cursor.getCount() > 0)
                            {
                                String lString = cursor.getString(0);
                                if (lString != null)
                                    return(new Date(Long.parseLong(cursor.getString(0))));
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
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in getEarliestTxDate", e.getMessage());
        }
        return (new Date(0));
    }

    Date getLatestTxDate(String sortCode, String accountNum,
                              Integer budgetMonth, Integer budgetYear)
    {
        try
        {
            MyLog.WriteLogMessage("getLatestTxDate: ");

            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    String lSql = "select IFNULL(MAX(TxDate),0) FROM tblTransaction " +
                            "WHERE TxSortCode = '" + sortCode + "' " +
                            "AND TxAccountNumber = '" + accountNum + "' " +
                            "AND BudgetYear in (" + budgetYear.toString() + ") " +
                            "AND BudgetMonth in (" + budgetMonth.toString() + ") ";
                    Cursor cursor = db.rawQuery(lSql, null);
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            if (cursor.getCount() > 0)
                            {
                                String lString = cursor.getString(0);
                                if (lString != null)
                                    return(new Date(Long.parseLong(cursor.getString(0))));
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
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in getLatestTxDate", e.getMessage());
        }
        return (new Date(0));
    }


    ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum,
                                                    Integer budgetMonth, Integer budgetYear)
    {
        ArrayList<RecordTransaction> list;
        Float lBalance=0.00f;
        try
        {
            Date mFromDate = getEarliestTxDate(sortCode, accountNum, budgetMonth, budgetYear);
            Date mToDate = getLatestTxDate(sortCode, accountNum,budgetMonth, budgetYear);
            if(mFromDate == new Date(0))
                return(new ArrayList<>());
            if(mToDate==new Date(0))
                return(new ArrayList<>());

            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                                "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                                "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                                "CategoryId", "Comments", "BudgetYear", "BudgetMonth"},
                        "TxSortCode=? AND TxAccountNumber=? AND TxDate BETWEEN ? AND ?",
                        new String[]{sortCode, accountNum, Long.toString(mFromDate.getTime()),
                                Long.toString(mToDate.getTime())}, null, null, "TxDate desc, TxLineNo", null);
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
                                                        parseFloat(cursor.getString(9)),
                                                        parseFloat(cursor.getString(10)),
                                                        Integer.parseInt(cursor.getString(11)),
                                                        cursor.getString(12),
                                                        Integer.parseInt(cursor.getString(13)),
                                                        Integer.parseInt(cursor.getString(14)),
                                                        false
                                                );
                                list.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                    String lSql = "select IFNULL(SUM(TxAmount),0) FROM tblTransaction " +
                            "WHERE TxSortCode = '" + sortCode + "' " +
                            "AND TxAccountNumber = '" + accountNum + "' " +
                            "AND TxDate <= " + Long.toString(mToDate.getTime()).toString() + " ";
                    Cursor cursor2 = db.rawQuery(lSql, null);
                    if (cursor2 != null)
                    {
                        try
                        {
                            cursor2.moveToFirst();
                            if (cursor2.getCount() > 0)
                            {
                                String lString = cursor2.getString(0);
                                if (lString != null)
                                    lBalance = parseFloat(cursor2.getString(0));
                            }
                        }
                        finally
                        {
                            cursor2.close();
                        }
                    }
                    
                }
            }

        }
        catch (Exception e)
        {
            list = new ArrayList<>();
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        if (sortCode.compareTo("Cash") == 0)
        {
            Float lBal = lBalance;
            for (int i = 0; i<list.size(); i++)
            {
                list.get(i).TxBalance = lBal;
                lBal = lBal - list.get(i).TxAmount;
            }
        }
        return list;
    }

    ArrayList<RecordTransaction> getBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth, Integer pSubCategoryId)
    {
        ArrayList<RecordTransaction> list;
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                        "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                        "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                        "CategoryId", "Comments", "BudgetYear", "BudgetMonth"},
                    "BudgetYear=? AND BudgetMonth=? AND CategoryId=?",
                    new String[]{pBudgetYear.toString(), pBudgetMonth.toString(), pSubCategoryId.toString()},
                    null, null, "TxDate desc, TxLineNo", null);
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
                                            parseFloat(cursor.getString(9)),
                                            parseFloat(cursor.getString(10)),
                                            Integer.parseInt(cursor.getString(11)),
                                            cursor.getString(12),
                                            Integer.parseInt(cursor.getString(13)),
                                            Integer.parseInt(cursor.getString(14)),
                                            true
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
            ErrorDialog.Show("Error in TableTransaction.getBudgetList", e.getMessage());
        }
        return list;
    }

    ArrayList<RecordTransaction> getTxDateRange(Date lFrom, Date lTo, String lSortCode,
                                                String lAccountNumber)
    {
        ArrayList<RecordTransaction> list;
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblTransaction", new String[]{"TxSeqNo", "TxAdded",
                        "TxFilename", "TxLineNo", "TxDate", "TxType", "TxSortCode",
                        "TxAccountNumber", "TxDescription", "TxAmount", "TxBalance",
                        "CategoryId", "Comments", "BudgetYear", "BudgetMonth"},
                    "TxDate>=? AND TxDate<=? AND TxSortCode=? AND TxAccountNumber=?",
                    new String[]{Long.toString(lFrom.getTime()), Long.toString(lTo.getTime()),
                        lSortCode, lAccountNumber},
                    null, null, "TxDate, TxLineNo", null);
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
                                                parseFloat(cursor.getString(9)),
                                                parseFloat(cursor.getString(10)),
                                                Integer.parseInt(cursor.getString(11)),
                                                cursor.getString(12),
                                                Integer.parseInt(cursor.getString(13)),
                                                Integer.parseInt(cursor.getString(14)),
                                                false
                                            )
                                    );
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
            ErrorDialog.Show("Error in TableTransaction.getTransactionList", e.getMessage());
        }
        return list;
    }
    
    RecordTransaction getSingleTransaction(Integer pTxSeqNo)
    {
        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
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
                                        parseFloat(cursor.getString(9)),
                                        parseFloat(cursor.getString(10)),
                                        Integer.parseInt(cursor.getString(11)),
                                        cursor.getString(12),
                                        Integer.parseInt(cursor.getString(13)),
                                        Integer.parseInt(cursor.getString(14)),
                                        false
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
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion) );
    }

    Float getCategoryBudgetSameMonthLastYear(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        Float lTotal = 0.00f;

        try
        {
            pBudgetYear=pBudgetYear-1;
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lString =
                        "SELECT SUM(TxAmount) " +
                        "FROM tblTransaction a, tblSubCategory b " +
                        "WHERE a.BudgetMonth = " + pBudgetMonth.toString() + " " +
                        "AND a.BudgetYear = " + pBudgetYear.toString() + " " +
                        "AND a.CategoryId = b.SubCategoryId " +
                        "AND b.CategoryId = " + pCategoryId.toString();
                Cursor cursor = db.rawQuery(lString, null);
                if (cursor != null)
                {
                    try
                    {
                        cursor.moveToFirst();
                        lTotal=Float.parseFloat(cursor.getString(0));
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
            ErrorDialog.Show("Error in TableTransaction.getBudgetList", e.getMessage());
        }
        return lTotal;
    }

    Float getCategoryBudgetLastMonth(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        Float lTotal = 0.00f;

        try
        {
            pBudgetMonth=pBudgetMonth-1;
            if(pBudgetMonth<1) {
                pBudgetMonth = 12;
                pBudgetYear--;
            }
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lString =
                        "SELECT SUM(TxAmount) " +
                                "FROM tblTransaction a, tblSubCategory b " +
                                "WHERE a.BudgetMonth = " + pBudgetMonth.toString() + " " +
                                "AND a.BudgetYear = " + pBudgetYear.toString() + " " +
                                "AND a.CategoryId = b.SubCategoryId " +
                                "AND b.CategoryId = " + pCategoryId.toString();
                Cursor cursor = db.rawQuery(lString, null);
                if (cursor != null)
                {
                    try
                    {
                        cursor.moveToFirst();
                        lTotal=Float.parseFloat(cursor.getString(0));
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
            ErrorDialog.Show("Error in TableTransaction.getBudgetList", e.getMessage());
        }
        return lTotal;
    }

    float getCategoryBudgetAverage(Integer pCategoryId)
    {
        float lTotal = 0.00f;
        float lAverage = 0.00f;
        float lCount=0.0f;

        try
        {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lString =
                        "SELECT BudgetYear, BudgetMonth, SUM(TxAmount) " +
                        "FROM tblTransaction a, tblSubCategory b " +
                        "WHERE a.CategoryId = b.SubCategoryId " +
                        "AND b.CategoryId = " + pCategoryId.toString() + " " +
                        "GROUP BY BudgetYear, BudgetMonth ";
                Cursor cursor = db.rawQuery(lString, null);
                if (cursor != null)
                {
                    try
                    {
                        cursor.moveToFirst();
                        do {
                            lTotal=lTotal + Float.parseFloat(cursor.getString(2));
                            lCount=lCount+1.00f;
                        } while(cursor.moveToNext());
                        if(lCount>0)
                            if(lTotal>0.001 || lTotal<-0.001)
                              lAverage=lTotal / lCount;
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
            ErrorDialog.Show("Error in TableTransaction.getBudgetList", e.getMessage());
        }
        return lAverage;
    }

}
