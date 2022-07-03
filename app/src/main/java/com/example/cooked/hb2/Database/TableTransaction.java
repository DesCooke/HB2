package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.GlobalUtils.MyString;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;
import com.example.cooked.hb2.Records.RecordTransaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.cooked.hb2.Database.RecordPlanned.mPTYearly;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
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
        MyDatabase.MyDB().Dirty=true;
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

            MyLog.WriteLogMessage("Amending transaction, SubCategoryName is (" + rt.SubCategoryName + ")");

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
                } else
                {
                    MyLog.WriteLogMessage("--no");
                }
            }
        }
        MyDatabase.MyDB().Dirty=true;
    }

    void updateFilenameLineNo(RecordTransaction dbRec, RecordTransaction fileRec)
    {
        String lSql =
                "UPDATE tblTransaction " +
                        " SET TxFilename = '" + fileRec.TxFilename + "', " +
                        " TxLineNo = " + fileRec.TxLineNo + " " +
                        "WHERE TxSeqNo = " + dbRec.TxSeqNo.toString();

        executeSQL(lSql, "TableTransaction::updateFilenameLineNo", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void deleteTransaction(RecordTransaction rec)
    {
        String lSql =
                "DELETE FROM tblTransaction " +
                        "WHERE TxSeqNo = " + rec.TxSeqNo.toString();

        executeSQL(lSql, "TableTransaction::deleteTransaction", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void dumpTransactionTable()
    {
        if (MyResources.R() == null)
            return;

        fixUnassignedBudget();

        String homeDirectory = MyResources.R().getString(R.string.home_directory);
        String dumpFilename = MyResources.R().getString(R.string.table_dump_tblTransaction);

        // create a File object from it
        File file = new File(homeDirectory + '/' + dumpFilename);
        if (file.exists())
            file.delete();

        File dir = new File(homeDirectory);
        if (!dir.exists())
            dir.mkdir();

        try
        {
            if (!file.createNewFile())
                throw new Exception("file.CreateNewFile() returned false");

            String timeStamp = DateFormat.getDateTimeInstance().format(new Date());

            FileWriter fw = new FileWriter(file, /*append*/ TRUE);


            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                try
                {
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write("TxSortCode,TxAccountNumber,TxDate,TxLineNo," +
                            "CategoryId,Comments,BudgetYear,BudgetMonth," +
                            "TxDescription,TxAmount,TxBalance\n");

                    String lSql = "select TxSortCode,TxAccountNumber,TxDate,TxLineNo, " +
                            "CategoryId,Comments,BudgetYear,BudgetMonth," +
                            "TxDescription,TxAmount,TxBalance " +
                            "FROM tblTransaction " +
                            "ORDER BY TxSortCode, TxAccountNumber,TxDate,TxLineNo ";
                    Cursor cursor = db.rawQuery(lSql, null);
                    if (cursor != null)
                    {
                        try
                        {
                            cursor.moveToFirst();
                            do
                            {
                                Date lTxDate = new Date(Long.parseLong(cursor.getString(2)));
                                MyString lDateStr = new MyString();
                                dateUtils().DateToStr(lTxDate, lDateStr);

                                bw.write("\"" + cursor.getString(0) + "\"," +
                                        "\"" + cursor.getString(1) + "\"," +
                                        "\"" + lDateStr.Value + "\"," +
                                        cursor.getString(3) + "," +
                                        cursor.getString(4) + "," +
                                        "\"" + cursor.getString(5) + "\"," +
                                        cursor.getString(6) + "," +
                                        cursor.getString(7) + "," +
                                        cursor.getString(8) + "," +
                                        cursor.getString(9) + "," +
                                        cursor.getString(10) + "\n"
                                );
                            }
                            while (cursor.moveToNext());
                        } finally
                        {
                            cursor.close();
                        }
                    }
                    bw.flush();
                    bw.close();
                } finally
                {
                    db.close();
                }
            }
        } catch (Exception e)
        {

        }
    }

    ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum)
    {
        ArrayList<RecordTransaction> list;
        RecordAccount ra = MyDatabase.MyDB().getAccountItemByAccountNumber(sortCode, accountNum);

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
                                                    false,
                                                    ra.AcUseCategory
                                            );
                            list.add(lrec);
                        } while (cursor.moveToNext());
                    }
                } finally
                {
                    cursor.close();
                }
            }
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
                                return (new Date(Long.parseLong(cursor.getString(0))));
                        }
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
        return (new Date(0));
    }

    Float getBalanceAtStartOf(String sortCode, String accountNum,
                              Integer pBudgetMonth, Integer pBudgetYear)
    {
        RecordAccount ra = MyDatabase.MyDB().getAccountItemByAccountNumber(sortCode, accountNum);
        float lBalance = ra.AcStartingBalance;
        MyLog.WriteLogMessage("getBalanceAt: ");

        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            try
            {
                String lSql = "SELECT SUM(TxAmount) FROM tblTransaction " +
                        "WHERE TxSortCode = '" + sortCode + "' " +
                        "AND TxAccountNumber = '" + accountNum + "' " +
                        "AND " +
                        " ( " +
                        "   ( BudgetYear < " + pBudgetYear + ") OR " +
                        "   ( BudgetYear = " + pBudgetYear + " AND BudgetMonth < " + pBudgetMonth + ") " +
                        " ) ";
                Cursor cursor = db.rawQuery(lSql, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            lBalance = lBalance + cursor.getFloat(0);
                        }
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
        return (lBalance);
    }

    Float getBalanceAt(String sortCode, String accountNum,
                       Date pDate)
    {
        RecordAccount ra = MyDatabase.MyDB().getAccountItemByAccountNumber(sortCode, accountNum);
        float lBalance = ra.AcStartingBalance;

        //float lBalance=324.27f + 27.86f;
        MyLog.WriteLogMessage("getBalanceAt: ");

        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            try
            {
                String lSql = "SELECT SUM(TxAmount) FROM tblTransaction " +
                        "WHERE TxSortCode = '" + sortCode + "' " +
                        "AND TxAccountNumber = '" + accountNum + "' " +
                        "AND TxDate < " + pDate.getTime() + " ";
                Cursor cursor = db.rawQuery(lSql, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            lBalance = lBalance + cursor.getFloat(0);
                        }
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
        return (lBalance);
    }

    Date getLatestTxDate(String sortCode, String accountNum,
                         Integer budgetMonth, Integer budgetYear)
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
                                return (new Date(Long.parseLong(cursor.getString(0))));
                        }
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
        return (new Date(0));
    }

    ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum,
                                                    Integer budgetMonth, Integer budgetYear,
                                                    Boolean includeThisBudgetOnly)
    {
        ArrayList<RecordTransaction> list;
        Float lBalance = 0.00f;
        Float lCurrBalance = 0.00f;
        Float lStartBalance = 0.00f;
        Date mFromDate = getEarliestTxDate(sortCode, accountNum, budgetMonth, budgetYear);
        Date mToDate = getLatestTxDate(sortCode, accountNum, budgetMonth, budgetYear);
        if (mFromDate == new Date(0))
            return (new ArrayList<>());
        if (mToDate == new Date(0))
            return (new ArrayList<>());

        RecordAccount ra = MyDatabase.MyDB().getAccountItemByAccountNumber(sortCode, accountNum);

        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String lSql =
                    "SELECT " +
                            "  a.TxSeqNo, a.TxAdded, a.TxFilename, a.TxLineNo, a.TxDate, a.TxType, a.TxSortCode, " +
                            "  a.TxAccountNumber, a.TxDescription, a.TxAmount, a.TxBalance, " +
                            "  a.CategoryId, a.Comments, a.BudgetYear, a.BudgetMonth, " +
                            "  b.SubCategoryName, c.CategoryName " +
                            "FROM tblTransaction a " +
                            "  LEFT OUTER JOIN tblSubCategory b " +
                            "    ON a.CategoryId = b.SubCategoryId " +
                            "  LEFT OUTER JOIN tblCategory c " +
                            "    ON b.CategoryId = c.CategoryId " +
                            "WHERE a.TxSortCode = '" + sortCode + "' " +
                            "AND a.TxAccountNumber = '" + accountNum + "' " +
                            "AND a.TxDate BETWEEN " + Long.toString(mFromDate.getTime()) + " " +
                            "  AND " + Long.toString(mToDate.getTime()) + " " +
                            "ORDER BY TxDate desc, TxLineNo";
            Cursor cursor = db.rawQuery(lSql, null);
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
                            int lBudgetYear = Integer.parseInt(cursor.getString(13));
                            int lBudgetMonth = Integer.parseInt(cursor.getString(14));
                            if (includeThisBudgetOnly == false ||
                                    (includeThisBudgetOnly && lBudgetYear == budgetYear && lBudgetMonth == budgetMonth))
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
                                                        0.00f,
                                                        Integer.parseInt(cursor.getString(11)),
                                                        cursor.getString(12),
                                                        Integer.parseInt(cursor.getString(13)),
                                                        Integer.parseInt(cursor.getString(14)),
                                                        false,
                                                        ra.AcUseCategory
                                                );
                                if (cursor.getString(15) == null)
                                {
                                    lrec.SubCategoryName = MyResources.R().getString(R.string.not_set);
                                } else
                                {
                                    lrec.SubCategoryName = cursor.getString(15);
                                }
                                if (cursor.getString(16) == null)
                                {
                                    lrec.CategoryName = MyResources.R().getString(R.string.not_set);
                                } else
                                {
                                    lrec.CategoryName = cursor.getString(16);
                                }
                                list.add(lrec);
                            }
                        } while (cursor.moveToNext());

                    }
                    else
                    {
                        lStartBalance=0.00f;
                    }
                    if (includeThisBudgetOnly)
                    {
                        lStartBalance = getBalanceAtStartOf(sortCode, accountNum, budgetMonth, budgetYear);
                    } else
                    {
                        Date lFirstDate = list.get(list.size() - 1).TxDate;
                        lStartBalance = getBalanceAt(sortCode, accountNum, lFirstDate);
                    }
                    lCurrBalance = lStartBalance;
                    for (int i = list.size() - 1; i >= 0; i--)
                    {
                        RecordTransaction rt = list.get(i);
                        lCurrBalance = lCurrBalance + rt.TxAmount;
                        rt.TxBalance = lCurrBalance;
                    }
                    RecordTransaction r1 = new RecordTransaction();
                    r1.MarkerEndingBalance = lStartBalance;
                    if(list.size()>0)
                        r1.MarkerEndingBalance = list.get(0).TxBalance;
                    list.add(0, r1);

                    RecordTransaction r2 = new RecordTransaction();
                    r2.MarkerStartingBalance = lStartBalance;
                    list.add(r2);
                } finally
                {
                    cursor.close();
                }
            }
        }
        return list;
    }

    List<RecordSubCategoryByMonth> getSubCategoryTotalByMonth(Integer pCategoryId)
    {
        List<RecordSubCategoryByMonth> list;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            Cursor cursor;
            String l_SQL;
            l_SQL = "SELECT BudgetYear, BudgetMonth, Sum(TxAmount), Count(*) " +
                "FROM tblTransaction " +
                "WHERE CategoryId = " + pCategoryId.toString() + " " +
                "GROUP BY BudgetYear, BudgetMonth " +
                "ORDER BY BudgetYear DESC, BudgetMonth DESC ";

            cursor = db.rawQuery(l_SQL, null);
            try
            {
                list = new ArrayList<RecordSubCategoryByMonth>();
                if (cursor != null)
                {
                    cursor.moveToFirst();
                    do
                    {
                        if (cursor.getCount() == 0)
                            break;
                        RecordSubCategoryByMonth rec = new RecordSubCategoryByMonth();
                        rec.CategoryId=0;
                        rec.SubCategoryId = pCategoryId;
                        rec.BudgetYear = Integer.parseInt(cursor.getString(0));
                        rec.BudgetMonth = Integer.parseInt(cursor.getString(1));
                        rec.Total = Float.parseFloat(cursor.getString(2));
                        rec.TransactionCount = Integer.parseInt(cursor.getString(3));
                        list.add(rec);
                    } while (cursor.moveToNext());
                }
            }
            finally
            {
                assert cursor != null;
                cursor.close();
            }
        }
        return list;
    }

    List<RecordSubCategoryByMonth> getCategoryTotalByMonth(Integer pCategoryId)
    {
        List<RecordSubCategoryByMonth> list;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            Cursor cursor;
            String l_SQL;
            l_SQL = "SELECT b.BudgetYear, b.BudgetMonth, Sum(b.TxAmount), Count(*) " +
                "FROM tblSubCategory a, tblTransaction b " +
                "WHERE a.CategoryId = " + pCategoryId + " " +
                "AND a.SubCategoryId = b.CategoryId " +
                "GROUP BY BudgetYear, BudgetMonth " +
                "ORDER BY BudgetYear DESC, BudgetMonth DESC ";

            cursor = db.rawQuery(l_SQL, null);
            try
            {
                list = new ArrayList<RecordSubCategoryByMonth>();
                if (cursor != null)
                {
                    cursor.moveToFirst();
                    do
                    {
                        if (cursor.getCount() == 0)
                            break;
                        RecordSubCategoryByMonth rec = new RecordSubCategoryByMonth();
                        rec.CategoryId=pCategoryId;
                        rec.SubCategoryId = pCategoryId;
                        rec.BudgetYear = Integer.parseInt(cursor.getString(0));
                        rec.BudgetMonth = Integer.parseInt(cursor.getString(1));
                        rec.Total = Float.parseFloat(cursor.getString(2));
                        rec.TransactionCount = Integer.parseInt(cursor.getString(3));
                        list.add(rec);
                    } while (cursor.moveToNext());
                }
            }
            finally
            {
                assert cursor != null;
                cursor.close();
            }
        }
        return list;
    }

    ArrayList<RecordTransaction> GetAnnualBills(int budgetYear)
    {
        float lMLeftOver=0.00f;
        float lMTotal=0.00f;
        float lMSpent = 0.00f;
        float lTotal=0.00f;
        float lSpent = 0.00f;
        ArrayList<RecordTransaction> list= new ArrayList<>();;

        ArrayList<RecordPlanned> pta = MyDatabase.MyDB().GetAnnualPlannedList(true);

        for(int i=0;i< pta.size();i++)
        {
            lTotal = pta.get(i).GetAmountAt(Calendar.getInstance().getTime());
            lSpent = 0.00f;

            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String lSql = "SELECT sum(a.TxAmount) " +
                    "FROM tblTransaction a, tblSubCategory b, tblCategory c " +
                    "WHERE a.BudgetYear = " + budgetYear + " " +
                    "AND a.CategoryId = " + pta.get(i).mSubCategoryId + " " +
                    "AND a.CategoryId = b.SubCategoryId " +
                    "AND b.CategoryId = c.CategoryId " +
                    "AND c.CategoryName ='Annual Bills' " +
                    "AND a.TxAmount < 0.00 ";
                Cursor cursor = db.rawQuery(lSql, null);
                if (cursor != null)
                {
                    try
                    {
                        int c = cursor.getCount();
                        if (c > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                lSpent = cursor.getFloat(0);
                            } while (cursor.moveToNext());

                        }
                    } finally
                    {
                        cursor.close();
                    }


                }

                float lLeftOver = lTotal - lSpent;
                lMTotal += lTotal;
                lMSpent += lSpent;
                lMLeftOver += lLeftOver;
                RecordTransaction lrec =
                    new RecordTransaction
                        (
                            0,
                            new Date(),
                            "",
                            0,
                            new Date(),
                            "",
                            "",
                            "",
                            "",
                            0.00f,
                            0.00f,
                            0,
                            "",
                            0,
                            0,
                            true,
                            false
                        );

                lrec.Comments = "Total " + Tools.moneyFormat(lTotal * -1) +
                    ", Spent " + Tools.moneyFormat(lSpent * -1) +
                    ", LeftOver " + Tools.moneyFormat(lLeftOver * -1);

                lrec.CategoryName = pta.get(i).mPlannedName;
                lrec.TxFilename = pta.get(i).mPlannedDay + DateUtils.DaySuffix(pta.get(i).mPlannedDay) + " " + DateUtils.MonthNames[pta.get(i).mPlannedMonth-1];

                list.add(lrec);

            }
        }

        RecordTransaction r2 = new RecordTransaction();
        r2.MarkerTotal = lMTotal*-1;
        r2.MarkerTotalSpent = lMSpent*-1;
        r2.MarkerTotalOutstanding = lMLeftOver*-1;
        list.add(r2);

        return list;
    }

    ArrayList<RecordTransaction> getBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth, Integer pSubCategoryId)
    {
        ArrayList<RecordTransaction> list;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String lBudgetString="";
            if(pBudgetYear!=0)
            {
                lBudgetString=
                        " a.BudgetYear = " + pBudgetYear.toString() + " AND " +
                        " a.BudgetMonth = " + pBudgetMonth.toString() + " AND ";
            }
            String lSql =
                    "SELECT " +
                            "  a.TxSeqNo, a.TxAdded, a.TxFilename, a.TxLineNo, a.TxDate, a.TxType, a.TxSortCode, " +
                            "  a.TxAccountNumber, a.TxDescription, a.TxAmount, a.TxBalance, " +
                            "  a.CategoryId, a.Comments, a.BudgetYear, a.BudgetMonth, " +
                            "  b.SubCategoryName, c.UseCategory " +
                            "FROM tblTransaction a, tblAccount c " +
                            "  LEFT OUTER JOIN tblSubCategory b " +
                            "    ON a.CategoryId = b.SubCategoryId " +
                            "WHERE " + lBudgetString + " a.TxSortCode = c.AcSortCode " +
                            "AND a.TxAccountNumber = c.AcAccountNumber " +
                            "AND a.CategoryId = " + pSubCategoryId.toString() + " " +
                            "ORDER BY TxDate desc, TxLineNo";
            Cursor cursor = db.rawQuery(lSql, null);

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
                                                    true,
                                                    Integer.parseInt(cursor.getString(16))==1
                                            );
                            lrec.SubCategoryName = cursor.getString(15);
                            list.add(lrec);
                        } while (cursor.moveToNext());
                    }
                } finally
                {
                    cursor.close();
                }
            }
        }
        return list;
    }

    ArrayList<RecordTransaction> getCategoryBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        ArrayList<RecordTransaction> list;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String lBudgetString="";
            if(pBudgetYear!=0)
            {
                lBudgetString=
                    " a.BudgetYear = " + pBudgetYear.toString() + " AND " +
                        " a.BudgetMonth = " + pBudgetMonth.toString() + " AND ";
            }
            String lSql =
                "SELECT " +
                    "  a.TxSeqNo, a.TxAdded, a.TxFilename, a.TxLineNo, a.TxDate, a.TxType, a.TxSortCode, " +
                    "  a.TxAccountNumber, a.TxDescription, a.TxAmount, a.TxBalance, " +
                    "  a.CategoryId, a.Comments, a.BudgetYear, a.BudgetMonth, " +
                    "  b.SubCategoryName, c.UseCategory " +
                    "FROM tblTransaction a, tblSubCategory b, tblAccount c " +
                    "WHERE b.CategoryId = " + pCategoryId + " " +
                    "AND b.SubCategoryId = a.CategoryId " +
                    "AND " + lBudgetString + " a.TxSortCode = c.AcSortCode " +
                    "AND a.TxAccountNumber = c.AcAccountNumber " +
                    "ORDER BY TxDate desc, TxLineNo";
            Cursor cursor = db.rawQuery(lSql, null);

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
                                        true,
                                        Integer.parseInt(cursor.getString(16))==1
                                    );
                            lrec.SubCategoryName = cursor.getString(15);
                            list.add(lrec);
                        } while (cursor.moveToNext());
                    }
                } finally
                {
                    cursor.close();
                }
            }
        }
        return list;
    }

    void fixUnassignedBudget()
    {
        ArrayList<RecordTransactionDate> list;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String lString =
                    "SELECT TxSeqNo, TxDate " +
                            "FROM tblTransaction  " +
                            "WHERE BudgetMonth = 0 OR BudgetYear = 0";
            Cursor cursor = db.rawQuery(lString, null);

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
                            Integer lTxSeqNo = Integer.parseInt(cursor.getString(0));
                            Date lTxDate = new Date(Long.parseLong(cursor.getString(1)));
                            RecordTransactionDate rtd = new RecordTransactionDate(lTxSeqNo, lTxDate);
                            list.add(rtd);
                        } while (cursor.moveToNext());
                    }
                } finally
                {
                    cursor.close();
                }

                for (int i = 0; i < list.size(); i++)
                {
                    RecordTransactionDate rtd = list.get(i);
                    int lBudgetYear = DateUtils.dateUtils().GetBudgetYear(rtd.TxDate);
                    int lBudgetMonth = DateUtils.dateUtils().GetBudgetMonth(rtd.TxDate);
                    String lSQL = "UPDATE tblTransaction SET BudgetMonth = " + lBudgetMonth + ", " +
                            "BudgetYear = " + lBudgetYear + " " +
                            "WHERE TxSeqNo = " + rtd.TxSeqNo;
                    db.execSQL(lSQL);
                }
            }
        }
        MyDatabase.MyDB().Dirty=true;
    }

    ArrayList<RecordTransaction> getTxDateRange(Date lFrom, Date lTo, String lSortCode,
                                                String lAccountNumber)
    {
        RecordAccount ra = MyDatabase.MyDB().getAccountItemByAccountNumber(lSortCode, lAccountNumber);

        ArrayList<RecordTransaction> list;
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
                        Boolean useCategory=false;
                        if(ra!=null)
                            if(ra.AcUseCategory!=null)
                                useCategory=ra.AcUseCategory;
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
                                                            false,
                                                            useCategory
                                                    )
                                    );
                        } while (cursor.moveToNext());
                    }
                } finally
                {
                    cursor.close();
                }
            }
        }
        return list;
    }

    RecordTransaction getSingleTransaction(Integer pTxSeqNo)
    {
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String lSql =
                    "SELECT " +
                            "  a.TxSeqNo, a.TxAdded, a.TxFilename, a.TxLineNo, a.TxDate, a.TxType, a.TxSortCode, " +
                            "  a.TxAccountNumber, a.TxDescription, a.TxAmount, a.TxBalance, " +
                            "  a.CategoryId, a.Comments, a.BudgetYear, a.BudgetMonth, " +
                            "  b.SubCategoryName, c.UseCategory " +
                            "FROM tblTransaction a, tblAccount c " +
                            "  LEFT OUTER JOIN tblSubCategory b " +
                            "    ON a.CategoryId = b.SubCategoryId " +
                            "WHERE TxSeqNo = " + Integer.toString(pTxSeqNo) + " " +
                            "AND a.TxSortCode = c.AcSortCode " +
                            "AND a.TxAccountNumber = c.AcAccountNumber ";
            Cursor cursor = db.rawQuery(lSql, null);
            if (cursor != null)
            {
                try
                {
                    if (cursor.getCount() > 0)
                    {
                        cursor.moveToFirst();
                        RecordTransaction rt = new RecordTransaction
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
                                        false,
                                        Integer.parseInt(cursor.getString(16))==1
                                );
                        if (cursor.getString(15) == null)
                        {
                            rt.SubCategoryName = MyResources.R().getString(R.string.not_set);
                        } else
                        {
                            rt.SubCategoryName = cursor.getString(15);
                        }
                        return (rt);
                    }
                } finally
                {
                    cursor.close();
                }
            }
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
                " down to " + Integer.toString(newVersion));
    }

    Float getCategoryBudgetSameMonthLastYear(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        Float lTotal = 0.00f;

        pBudgetYear = pBudgetYear - 1;
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
                    lTotal = Float.parseFloat(cursor.getString(0));
                } finally
                {
                    cursor.close();
                }
            }
        }
        return lTotal;
    }

    Float getCategoryBudgetLastMonth(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        Float lTotal = 0.00f;

        pBudgetMonth = pBudgetMonth - 1;
        if (pBudgetMonth < 1)
        {
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
                    lTotal = Float.parseFloat(cursor.getString(0));
                } finally
                {
                    cursor.close();
                }
            }
        }
        return lTotal;
    }

    float getCategoryBudgetAverage(Integer pCategoryId)
    {
        float lTotal = 0.00f;
        float lAverage = 0.00f;
        float lCount = 0.0f;

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
                    do
                    {
                        lTotal = lTotal + Float.parseFloat(cursor.getString(2));
                        lCount = lCount + 1.00f;
                    } while (cursor.moveToNext());
                    if (lCount > 0)
                        if (lTotal > 0.001 || lTotal < -0.001)
                            lAverage = lTotal / lCount;
                } finally
                {
                    cursor.close();
                }
            }
        }
        return lAverage;
    }

}
