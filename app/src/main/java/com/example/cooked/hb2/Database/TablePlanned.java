package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;
import com.example.cooked.hb2.Records.RecordTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.example.cooked.hb2.Database.RecordPlanned.mPTMonthly;
import static com.example.cooked.hb2.Database.RecordPlanned.mPTOneOff;
import static com.example.cooked.hb2.Database.RecordPlanned.mPTWeekly;
import static com.example.cooked.hb2.Database.RecordPlanned.mPTYearly;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class TablePlanned extends TableBase
{
    TablePlanned(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }
    
    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblPlanned";
        executeSQL(lSql, "TablePlanned::dropTableIfExists", db);
    }
    
    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);
        
        String lSql =
            "CREATE TABLE tblPlanned " +
                " (" +
                "   PlannedId INTEGER PRIMARY KEY, " +
                "   PlannedType INTEGER, " +
                "   PlannedName TEXT, " +
                "   SubCategoryId INTEGER, " +
                "   SortCode TEXT, " +
                "   AccountNo TEXT, " +
                "   PlannedDate INTEGER, " +
                "   PlannedMonth INTEGER, " +
                "   PlannedDay INTEGER, " +
                "   Monday INTEGER, " +
                "   Tuesday INTEGER, " +
                "   Wednesday INTEGER, " +
                "   Thursday INTEGER, " +
                "   Friday INTEGER, " +
                "   Saturday INTEGER, " +
                "   Sunday INTEGER, " +
                "   StartDate INTEGER, " +
                "   EndDate INTEGER, " +
                "   MatchingTxType TEXT, " +
                "   MatchingTxDescription TEXT, " +
                "   MatchingTxAmount FLOAT, " +
                "   AutoMatchTransaction INTEGER, " +
                "   PaidInParts INTEGER, " +
                "   FrequencyMultiplier INTEGER " +
                ") ";
        
        executeSQL(lSql, "TablePlanned::onCreate", db);
    }
    
    private int getNextPlannedId()
    {
        String lSql = "SELECT MAX(PlannedId) FROM tblPlanned ";
        
        return (getMaxPlus1(lSql, "TablePlanned::getNextPlannedId"));
    }
    
    void addPlanned(RecordPlanned rp)
    {
        rp.mPlannedId = getNextPlannedId();
        
        String lSql =
            "INSERT INTO tblPlanned " +
                "(PlannedId, PlannedType, PlannedName, SubCategoryId, SortCode, AccountNo, PlannedDate, PlannedMonth, PlannedDay, Monday, " +
                "Tuesday, Wednesday, Thursday, Friday, Saturday, " +
                "Sunday, StartDate, EndDate, MatchingTxType, MatchingTxDescription, " +
                "MatchingTxAmount, AutoMatchTransaction, PaidInParts, FrequencyMultiplier ) " +
                "VALUES (" +
                Integer.toString(rp.mPlannedId) + "," +
                Integer.toString(rp.mPlannedType) + "," +
                "'" + rp.mPlannedName.trim() + "'," +
                Integer.toString(rp.mSubCategoryId) + "," +
                    "'" + rp.mSortCode.trim() + "'," +
                    "'" + rp.mAccountNo.trim() + "'," +
                Long.toString(DateUtils.dateUtils().StripTimeElement(rp.mPlannedDate)) + "," +
                Integer.toString(rp.mPlannedMonth) + "," +
                Integer.toString(rp.mPlannedDay) + "," +
                Integer.toString(rp.mMonday?1:0) + "," +
                Integer.toString(rp.mTuesday?1:0) + "," +
                Integer.toString(rp.mWednesday?1:0) + "," +
                Integer.toString(rp.mThursday?1:0) + "," +
                Integer.toString(rp.mFriday?1:0) + "," +
                Integer.toString(rp.mSaturday?1:0) + "," +
                Integer.toString(rp.mSunday?1:0) + "," +
                Long.toString(rp.mStartDate.getTime()) + "," +
                Long.toString(rp.mEndDate.getTime()) + "," +
                "'" + rp.mMatchingTxType.trim() + "'," +
                "'" + rp.mMatchingTxDescription.trim() + "'," +
                Float.toString(rp.mMatchingTxAmount) + ", " +
                Integer.toString(rp.mAutoMatchTransaction?1:0) + ", " +
                Integer.toString(rp.mPaidInParts?1:0) + ", " +
                Integer.toString(rp.mFrequencyMultiplier) + " " +
        ") ";
        
        executeSQL(lSql, "TablePlanned::addPlanned", null);
    }
    
    void updatePlanned(RecordPlanned rp) {
        String lSql =
                "UPDATE tblPlanned " +
                        " SET PlannedType = " + Integer.toString(rp.mPlannedType) + "," +
                        " PlannedName = '" + rp.mPlannedName.trim() + "'," +
                        " SubCategoryId = " + Integer.toString(rp.mSubCategoryId) + "," +
                        " SortCode = '" + rp.mSortCode.trim() + "'," +
                        " AccountNo = '" + rp.mAccountNo.trim() + "'," +
                        " PlannedDate = " + Long.toString(DateUtils.dateUtils().StripTimeElement(rp.mPlannedDate)) + "," +
                        " PlannedMonth = " + Integer.toString(rp.mPlannedMonth) + "," +
                        " PlannedDay = " + Integer.toString(rp.mPlannedDay) + "," +
                        " Monday = " + Integer.toString(rp.mMonday ? 1 : 0) + "," +
                        " Tuesday = " + Integer.toString(rp.mTuesday ? 1 : 0) + "," +
                        " Wednesday = " + Integer.toString(rp.mWednesday ? 1 : 0) + "," +
                        " Thursday = " + Integer.toString(rp.mThursday ? 1 : 0) + "," +
                        " Friday = " + Integer.toString(rp.mFriday ? 1 : 0) + "," +
                        " Saturday = " + Integer.toString(rp.mSaturday ? 1 : 0) + "," +
                        " Sunday = " + Integer.toString(rp.mSunday ? 1 : 0) + "," +
                        " StartDate = " + Long.toString(rp.mStartDate.getTime()) + "," +
                        " EndDate = " + Long.toString(rp.mEndDate.getTime()) + "," +
                        " MatchingTxType = '" + rp.mMatchingTxType.trim() + "'," +
                        " MatchingTxDescription = '" + rp.mMatchingTxDescription.trim() + "'," +
                        " MatchingTxAmount = " + Float.toString(rp.mMatchingTxAmount) + ", " +
                        " AutoMatchTransaction = " + Integer.toString(rp.mAutoMatchTransaction ? 1 : 0) + ", " +
                        " PaidInParts = " + Integer.toString(rp.mPaidInParts ? 1 : 0) + ", " +
                        " FrequencyMultiplier = " + Integer.toString(rp.mFrequencyMultiplier) + " " +
                        "WHERE PlannedId = " + Integer.toString(rp.mPlannedId);

        executeSQL(lSql, "TablePlanned::updatePlanned", null);
    }
    
    void deletePlanned(RecordPlanned rec)
    {
        String lSql =
            "DELETE FROM tblPlanned " +
                "WHERE PlannedId = " + rec.mPlannedId.toString();
        
        executeSQL(lSql, "TablePlanned::deletePlanned", null);
    }
    
    ArrayList<RecordPlanned> getPlannedList(boolean activeOnly) {
        ArrayList<RecordPlanned> list;
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            String l_SQL = "SELECT PlannedId, PlannedType, PlannedName, SubCategoryId, " +
                    "SortCode, AccountNo, PlannedDate, PlannedMonth,PlannedDay,Monday, " +
                    "Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, StartDate, " +
                    "EndDate, MatchingTxType, MatchingTxDescription, MatchingTxAmount, " +
                    "AutoMatchTransaction, PaidInParts, FrequencyMultiplier " +
                    " FROM tblPlanned ";
            if (activeOnly) {
                Integer lCurrBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
                Integer lCurrBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
                Date lBudgetStart = DateUtils.dateUtils().BudgetStart(lCurrBudgetMonth, lCurrBudgetYear);
                Date lBudgetEnd = DateUtils.dateUtils().BudgetEnd(lCurrBudgetMonth, lCurrBudgetYear);

                l_SQL = l_SQL + "WHERE StartDate < " + Long.toString(lBudgetEnd.getTime()) +
                        " AND EndDate > " + Long.toString(lBudgetStart.getTime()) + " ";
            }
            l_SQL = l_SQL + "ORDER BY PlannedName";

            Cursor cursor = db.rawQuery(l_SQL, null);
            try {
                list = new ArrayList<>();
                if (cursor != null) {
                    try {
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                Long lOrigDate = Long.parseLong(cursor.getString(6).trim());
                                Date lDate = new Date(lOrigDate);
                                Long lNewDate = DateUtils.dateUtils().StripTimeElement(lDate);
                                lDate = new Date(lNewDate);
                                String lstring=cursor.getString(23);
                                RecordPlanned lrec =
                                        new RecordPlanned
                                                (
                                                        Integer.parseInt(cursor.getString(0).trim()),
                                                        Integer.parseInt(cursor.getString(1).trim()),
                                                        cursor.getString(2).trim(),
                                                        Integer.parseInt(cursor.getString(3).trim()),
                                                        cursor.getString(4).trim(),
                                                        cursor.getString(5).trim(),
                                                        lDate,
                                                        Integer.parseInt(cursor.getString(7).trim()),
                                                        Integer.parseInt(cursor.getString(8).trim()),
                                                        Integer.parseInt(cursor.getString(9).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(10).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(11).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(12).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(13).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(14).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(15).trim()) == 1 ? TRUE : FALSE,
                                                        new Date(Long.parseLong(cursor.getString(16).trim())),
                                                        new Date(Long.parseLong(cursor.getString(17).trim())),
                                                        cursor.getString(18).trim(),
                                                        cursor.getString(19).trim(),
                                                        Float.parseFloat(cursor.getString(20).trim()),
                                                        Integer.parseInt(cursor.getString(21).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(22).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(23).trim())
                                                );
                                list.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    } finally {
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                list = new ArrayList<>();
                ErrorDialog.Show("Error in TablePlanned.getPlannedList", e.getMessage());
            }
        } catch (Exception e) {
            list = new ArrayList<>();
            ErrorDialog.Show("Error in TablePlanned.getPlannedList", e.getMessage());
        }

        for(int i=0;i<list.size();i++)
        {
            list.get(i).variations = MyDatabase.MyDB().getVariationList(list.get(i).mPlannedId);
        }
        return list;
    }

    public ArrayList<RecordPlanned> getPlannedListForSubCategory(int pSubCategoryId) {
        ArrayList<RecordPlanned> list;
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            try (Cursor cursor = db.query("tblPlanned", new String[]{"PlannedId", "PlannedType",
                            "PlannedName", "SubCategoryId", "SortCode", "AccountNo", "PlannedDate",
                            "PlannedMonth", "PlannedDay", "Monday", "Tuesday",
                            "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "StartDate",
                            "EndDate", "MatchingTxType", "MatchingTxDescription", "MatchingTxAmount",
                            "AutoMatchTransaction", "PaidInParts", "FrequencyMultiplier"},
                    "SubCategoryId=?",
                    new String[]{Integer.toString(pSubCategoryId)},
                    null, null, "PlannedName", null)) {
                list = new ArrayList<>();
                if (cursor != null) {
                    try {
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                RecordPlanned lrec =
                                        new RecordPlanned
                                                (
                                                        Integer.parseInt(cursor.getString(0).trim()),
                                                        Integer.parseInt(cursor.getString(1).trim()),
                                                        cursor.getString(2).trim(),
                                                        Integer.parseInt(cursor.getString(3).trim()),
                                                        cursor.getString(4).trim(),
                                                        cursor.getString(5).trim(),
                                                        new Date(Long.parseLong(cursor.getString(6).trim())),
                                                        Integer.parseInt(cursor.getString(7).trim()),
                                                        Integer.parseInt(cursor.getString(8).trim()),
                                                        Integer.parseInt(cursor.getString(9).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(10).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(11).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(12).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(13).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(14).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(15).trim()) == 1 ? TRUE : FALSE,
                                                        new Date(Long.parseLong(cursor.getString(16).trim())),
                                                        new Date(Long.parseLong(cursor.getString(17).trim())),
                                                        cursor.getString(18).trim(),
                                                        cursor.getString(19).trim(),
                                                        Float.parseFloat(cursor.getString(20).trim()),
                                                        Integer.parseInt(cursor.getString(21).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(22).trim()) == 1 ? TRUE : FALSE,
                                                        Integer.parseInt(cursor.getString(23).trim())
                                                );
                                Long lDate = DateUtils.dateUtils().StripTimeElement(lrec.mPlannedDate);
                                lrec.mPlannedDate = new Date(lDate);
                                list.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    } finally {
                        cursor.close();
                    }
                }
            }
        }

        for(int i=0;i<list.size();i++)
        {
            list.get(i).variations = MyDatabase.MyDB().getVariationList(list.get(i).mPlannedId);
        }

        return list;
    }

    ArrayList<RecordTransaction> getOutstandingList(String pSortCode, String pAccountNum, int pMonth, int pYear)
    {
        ArrayList<RecordBudget> budgetSpent;
        budgetSpent = new ArrayList<>();
        ArrayList<RecordBudget> budgetFull;
        ArrayList<RecordTransaction> list = new ArrayList<>();
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String l_SQL = "SELECT CategoryId, SUM(TxAmount) FROM tblTransaction " +
                    "WHERE TxSortCode = '" + pSortCode + "' AND TxAccountNumber = '" +
                    pAccountNum + "' AND BudgetYear = " + pYear + " AND BudgetMonth = " +
                    pMonth + " GROUP BY CategoryId ";
        
                Cursor cursor = db.rawQuery(l_SQL, null);
    
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                RecordBudget lrec =
                                    new RecordBudget(
                                        Integer.parseInt(cursor.getString(0).trim()),
                                        Float.parseFloat(cursor.getString(1).trim()),
                                       null,
                                       false,
                                            -1
                                    );
                                budgetSpent.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }

            MyLog.WriteLogMessage("Budget Spent for " + pMonth + "/" + pYear);

            for(int i=0;i<budgetSpent.size();i++) {
                RecordBudget currBudget = budgetSpent.get(i);

                RecordSubCategory lSubCategory = MyDatabase.MyDB().getSubCategory(currBudget.SubCategoryId);
                MyLog.WriteLogMessage("   " + lSubCategory.SubCategoryName + "(" +
                        currBudget.SubCategoryId + "), " + currBudget.Amount);
            }

            budgetFull = getBudgetList(pMonth, pYear);
            MyLog.WriteLogMessage("Full Budget for " + pMonth + "/" + pYear);

            for(int i=0;i<budgetFull.size();i++)
            {
                RecordBudget currBudget=budgetFull.get(i);

                RecordSubCategory lSubCategory = MyDatabase.MyDB().getSubCategory(currBudget.SubCategoryId);
                MyLog.WriteLogMessage("   " + lSubCategory.SubCategoryName + "(" +
                  currBudget.SubCategoryId + "), " + currBudget.Amount);

                boolean lFound=false;
                Float lDiff = 0.00f;
                for(int j=0;j<budgetSpent.size();j++)
                {
                    RecordBudget currSpend = budgetSpent.get(j);
                    if(currSpend.SubCategoryId == currBudget.SubCategoryId)
                    {
                        lFound=true;
                        lDiff = currBudget.Amount - currSpend.Amount;
                        break;
                    }
                }
                Float lAmount = 0.00f;
                boolean lCreateTx = false;
                if(!lFound)
                {
                    lAmount = currBudget.Amount;
                    lCreateTx = true;
                }
                else
                {
                    if( lDiff > 0.001 || lDiff < -0.001)
                    {
                        lAmount = lDiff;
                        lCreateTx = true;
                    }
                }
                if(lCreateTx)
                {
                    if (currBudget.NextDueDate==null)
                        currBudget.NextDueDate = new Date();
                    RecordTransaction lrec=new RecordTransaction();
                    lrec.TxSeqNo = 0;
                    lrec.TxAdded = new Date();
                    lrec.TxFilename = "planned";
                    lrec.TxLineNo = 1;
                    lrec.TxDate = currBudget.NextDueDate;
                    lrec.TxType = "PP";
                    lrec.TxSortCode = pSortCode;
                    lrec.TxAccountNumber = pAccountNum;
                    lrec.TxDescription = "Planned";
                    lrec.TxAmount = lAmount;
                    lrec.TxBalance = 0.00f;
                    lrec.TxStatus = RecordTransaction.Status.NEW;
                    lrec.CategoryId = currBudget.SubCategoryId;
                    RecordSubCategory rs = MyDatabase.MyDB().getSubCategory(currBudget.SubCategoryId);
                    lrec.SubCategoryName = rs.SubCategoryName;
                    lrec.Comments = "Outstanding";
                    lrec.BudgetYear = pYear;
                    lrec.BudgetMonth = pMonth;
                    list.add(lrec);
                }
            }

        Collections.sort(list, new Comparator<RecordTransaction>() {
            public int compare(RecordTransaction o1, RecordTransaction o2) {
                return o1.TxDate.compareTo(o2.TxDate);
            }
        });
        return list;
    }

    ArrayList<RecordTransaction> getBothOutstandingList(int pMonth, int pYear, int pSubCategoryId)
    {
        ArrayList<RecordBudget> budgetSpent;
        budgetSpent = new ArrayList<>();
        ArrayList<RecordBudget> budgetFull;
        ArrayList<RecordTransaction> list = new ArrayList<>();
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String l_SQL = "SELECT TxAmount FROM tblTransaction " +
                    "WHERE TxAccountNumber IN ('Cash','00038840') " +
                    "AND BudgetYear = " + pYear + " " +
                    "AND BudgetMonth = " + pMonth + " " +
                    "AND CategoryId = " + pSubCategoryId;
        
                Cursor cursor = db.rawQuery(l_SQL, null);
    
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                RecordBudget lrec =
                                    new RecordBudget(
                                        Integer.parseInt(cursor.getString(0).trim()),
                                        Float.parseFloat(cursor.getString(1).trim()),
                                        null,
                                            false,
                                            -1
                                    );
                                budgetSpent.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }

            MyLog.WriteLogMessage("Budget Spent for " + pMonth + "/" + pYear);

            for(int i=0;i<budgetSpent.size();i++) {
                RecordBudget currBudget = budgetSpent.get(i);

                RecordSubCategory lSubCategory = MyDatabase.MyDB().getSubCategory(currBudget.SubCategoryId);
                MyLog.WriteLogMessage("   " + lSubCategory.SubCategoryName + "(" +
                        currBudget.SubCategoryId + "), " + currBudget.Amount);
            }

            budgetFull = getBudgetList(pMonth, pYear);
            MyLog.WriteLogMessage("Full Budget for " + pMonth + "/" + pYear);

            for(int i=0;i<budgetFull.size();i++)
            {
                RecordBudget currBudget=budgetFull.get(i);

                RecordSubCategory lSubCategory = MyDatabase.MyDB().getSubCategory(currBudget.SubCategoryId);
                MyLog.WriteLogMessage("   " + lSubCategory.SubCategoryName + "(" +
                  currBudget.SubCategoryId + "), " + currBudget.Amount);

                boolean lFound=false;
                Float lDiff = 0.00f;
                for(int j=0;j<budgetSpent.size();j++)
                {
                    RecordBudget currSpend = budgetSpent.get(j);
                    if(currSpend.SubCategoryId == currBudget.SubCategoryId)
                    {
                        lFound=true;
                        lDiff = currBudget.Amount - currSpend.Amount;
                        break;
                    }
                }
                Float lAmount = 0.00f;
                boolean lCreateTx = false;
                if(!lFound)
                {
                    lAmount = currBudget.Amount;
                    lCreateTx = true;
                }
                else
                {
                    if( lDiff > 0.001 || lDiff < -0.001)
                    {
                        lAmount = lDiff;
                        lCreateTx = true;
                    }
                }
                if(lCreateTx)
                {
                    if (currBudget.NextDueDate==null)
                        currBudget.NextDueDate = new Date();
                    RecordTransaction lrec=new RecordTransaction();
                    lrec.TxSeqNo = 0;
                    lrec.TxAdded = new Date();
                    lrec.TxFilename = "planned";
                    lrec.TxLineNo = 1;
                    lrec.TxDate = currBudget.NextDueDate;
                    lrec.TxType = "PP";
                    lrec.TxSortCode = "Planned";
                    lrec.TxAccountNumber = "Planned";
                    lrec.TxAmount = lAmount;
                    lrec.TxBalance = 0.00f;
                    lrec.TxStatus = RecordTransaction.Status.NEW;
                    lrec.CategoryId = currBudget.SubCategoryId;
                    RecordSubCategory rs = MyDatabase.MyDB().getSubCategory(currBudget.SubCategoryId);
                    lrec.SubCategoryName = rs.SubCategoryName;
                    lrec.Comments = "Outstanding";
                    lrec.BudgetYear = pYear;
                    lrec.BudgetMonth = pMonth;
                    list.add(lrec);
                }
            }

        Collections.sort(list, new Comparator<RecordTransaction>() {
            public int compare(RecordTransaction o1, RecordTransaction o2) {
                return o1.TxDate.compareTo(o2.TxDate);
            }
        });
        return list;
    }
    
    ArrayList<RecordBudget> getBudgetSpent(int pMonth, int pYear)
    {
        ArrayList<RecordBudget> budgetSpent = new ArrayList<>();
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                String l_SQL =
                    "SELECT CategoryId, SUM(TxAmount) " +
                    "FROM tblTransaction " +
                    "WHERE BudgetYear = " + pYear + " AND BudgetMonth = " +
                    pMonth + " GROUP BY CategoryId ";
        
                Cursor cursor = db.rawQuery(l_SQL, null);
        
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                            do
                            {
                                RecordBudget lrec =
                                    new RecordBudget(
                                        Integer.parseInt(cursor.getString(0).trim()),
                                        Float.parseFloat(cursor.getString(1).trim()),
                                        null,
                                            false,
                                            -1
                                    );
                                budgetSpent.add(lrec);
                            } while (cursor.moveToNext());
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }

            return(budgetSpent);
    }

    private Boolean isDue(RecordPlanned prp, Date pDate)
    {
        if(prp.mPlannedType==mPTOneOff)
        {
            return prp.mPlannedDate.getTime() == pDate.getTime();
        }
        if(prp.mPlannedType==mPTYearly)
        {
            MyInt lDay = new MyInt();
            MyInt lMonth = new MyInt();
            DateUtils.dateUtils().GetMonth(pDate, lMonth);
            DateUtils.dateUtils().GetDay(pDate, lDay);
            if(lMonth.Value == prp.mPlannedMonth && lDay.Value == prp.mPlannedDay)
            {
                MyInt lYearToCheck = new MyInt();
                DateUtils.dateUtils().GetYear(pDate, lYearToCheck);
                MyInt lPlannedStartDateYear = new MyInt();
                DateUtils.dateUtils().GetYear(prp.mStartDate, lPlannedStartDateYear);
                int lDiff = lYearToCheck.Value - lPlannedStartDateYear.Value;
                int lMod = lDiff % prp.mFrequencyMultiplier;
                if (lMod == 0)
                    return (true);
                return (false);
            }
            else
            {
                return(false);
            }
        }
        if(prp.mPlannedType==mPTMonthly)
        {
            MyInt lDay = new MyInt();
            DateUtils.dateUtils().GetDay(pDate, lDay);
            if(lDay.Value == prp.mPlannedDay)
            {
                MyInt lYearToCheck = new MyInt();
                DateUtils.dateUtils().GetYear(pDate, lYearToCheck);
                MyInt lMonthToCheck = new MyInt();
                DateUtils.dateUtils().GetMonth(pDate, lMonthToCheck);

                MyInt lPlannedYearToCheck = new MyInt();
                DateUtils.dateUtils().GetYear(prp.mStartDate, lPlannedYearToCheck);
                MyInt lPlannedMonthToCheck = new MyInt();
                DateUtils.dateUtils().GetMonth(prp.mStartDate, lPlannedMonthToCheck);

                int lMonths=((lYearToCheck.Value*12)+lMonthToCheck.Value)-((lPlannedYearToCheck.Value*12)+lPlannedMonthToCheck.Value);

                int lMod = lMonths % prp.mFrequencyMultiplier;
                if (lMod == 0)
                    return (true);
                return (false);
            }
            else
            {
                return(false);
            }
        }
        if(prp.mPlannedType==mPTWeekly)
        {
            MyString lMyString = new MyString();
            DateUtils.dateUtils().GetDayOfWeek(pDate, lMyString);
            if(lMyString.Value.compareTo("Mon")==0 && !prp.mMonday)
                return(false);
            if(lMyString.Value.compareTo("Tue")==0 && !prp.mTuesday)
                return(false);
            if(lMyString.Value.compareTo("Wed")==0 && !prp.mWednesday)
                return(false);
            if(lMyString.Value.compareTo("Thu")==0 && !prp.mThursday)
                return(false);
            if(lMyString.Value.compareTo("Fri")==0 && !prp.mFriday)
                return(false);
            if(lMyString.Value.compareTo("Sat")==0 && !prp.mSaturday)
                return(false);
            if(lMyString.Value.compareTo("Sun")==0 && !prp.mSunday)
                return(false);

            //
            // ok - so we know the date we are checking is the correct day
            // eg. Saturday.  Now we need to work out how many days since the start
            // but we don't want the start of the planned item - that might not be the
            // correct date.  We take that date, check to see if it is the correct day.
            // If it isn't - add 1 day until we get the right day.
            // So if the planned item started on Sun 26th June, and the
            // planned item is only on saturdays, and the current date we are checking
            // is a saturday - we need to count the number of days since the first saturday after
            // the planned start date
            //
            MyString lCurrentDay=new MyString();
            Date lLoopDate = new Date(prp.mStartDate.getTime());
            DateUtils.dateUtils().GetDayOfWeek(lLoopDate, lCurrentDay);
            while(lCurrentDay.Value.compareTo(lMyString.Value)!=0)
            {
                Calendar c = Calendar.getInstance();
                c.setTime(lLoopDate);
                c.add(Calendar.DATE, 1);
                lLoopDate.setTime(c.getTime().getTime());
                DateUtils.dateUtils().GetDayOfWeek(lLoopDate, lCurrentDay);
            }
            long lDiff=pDate.getTime() - lLoopDate.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = lDiff / daysInMilli;
            if(elapsedDays % (prp.mFrequencyMultiplier * 7) == 0)
                return(true);
            return (false);
        }

        return(false);
    }

    ArrayList<RecordBudget> getBudgetList(int pMonth, int pYear)
    {
        ArrayList<RecordPlanned> plannedList;
        ArrayList<RecordBudget> budgetFull = new ArrayList<>();
    
        Date lBudgetStart = DateUtils.dateUtils().BudgetStart(pMonth, pYear);
        Date lBudgetEnd = DateUtils.dateUtils().BudgetEnd(pMonth, pYear);
        Date lCurrentDate;

        plannedList = getPlannedList(false);
        for(int i=0;i<plannedList.size();i++)
        {
            RecordPlanned rp = plannedList.get(i);

            if(rp.mStartDate.getTime() <= lBudgetEnd.getTime() &&
                    rp.mEndDate.getTime() >= lBudgetStart.getTime())
            {
                lCurrentDate = lBudgetStart;
                Float lAmount = 0.00f;
                Boolean lAtleastOne = false;
                do {
                    if(lCurrentDate.getTime() >= rp.mStartDate.getTime() &&
                            lCurrentDate.getTime() <= rp.mEndDate.getTime()) {
                        if (isDue(rp, lCurrentDate)) {
                            if(rp.mNextDueDate==null)
                                rp.mNextDueDate = lCurrentDate;
                            lAtleastOne = true;
                            lAmount += rp.GetAmountAt(lCurrentDate);
                        }
                    }

                    Date lNewDate = new Date();
                    DateUtils.dateUtils().AddDays(lCurrentDate, 1, lNewDate);
                    lCurrentDate = lNewDate;
                } while (lCurrentDate.getTime() <= lBudgetEnd.getTime());
                if (lAtleastOne) {
                    boolean lFound = false;
                    for (int j = 0; j < budgetFull.size(); j++)
                    {
                        RecordBudget rb = budgetFull.get(j);
                        if (rb.SubCategoryId == rp.mSubCategoryId)
                        {
                            rb.Amount += lAmount;
                            rb.NextDueDate = rp.mNextDueDate;
                            lFound = true;
                        }
                    }
                    if (!lFound)
                    {
                        budgetFull.add(
                            new RecordBudget
                                    (
                                        rp.mSubCategoryId,
                                        lAmount,
                                        rp.mNextDueDate,
                                        rp.mAutoMatchTransaction,
                                        rp.mPlannedId
                                    )
                        );
                    }
                }
            }
        }

        return(budgetFull);
    }

    ArrayList<RecordTransaction> getPlannedTransForSubCategoryId(int pMonth, int pYear, int pSubCategoryId)
    {
        ArrayList<RecordPlanned> plannedList;
        ArrayList<RecordTransaction> budgetTrans = new ArrayList<>();
    
        Date lBudgetStart = DateUtils.dateUtils().BudgetStart(pMonth, pYear);
        Date lBudgetEnd = DateUtils.dateUtils().BudgetEnd(pMonth, pYear);
        Date lCurrentDate;

        plannedList = getPlannedListForSubCategory(pSubCategoryId);
        for(int i=0;i<plannedList.size();i++)
        {
            RecordPlanned rp = plannedList.get(i);

            if(rp.mStartDate.getTime() <= lBudgetEnd.getTime() &&
                    rp.mEndDate.getTime() >= lBudgetStart.getTime()) {
                lCurrentDate = lBudgetStart;
                Float lAmount = 0.00f;
                Boolean lAtleastOne = false;
                do {
                    if(lCurrentDate.getTime() >= rp.mStartDate.getTime() &&
                            lCurrentDate.getTime() <= rp.mEndDate.getTime())
                    {
                        if (isDue(rp, lCurrentDate))
                        {
                            RecordTransaction rt = new RecordTransaction();
                            rt.TxSeqNo = rp.mPlannedId;
                            rt.TxAdded = new Date();
                            rt.TxFilename = "Planned";
                            rt.TxLineNo = 0;
                            rt.TxDate = lCurrentDate;
                            rt.TxType = "Planned";
                            rt.TxSortCode = rp.mSortCode;
                            rt.TxAccountNumber = rp.mAccountNo;
                            rt.TxDescription = rp.mPlannedName;
                            rt.TxAmount = rp.GetAmountAt(lCurrentDate);
                            rt.TxBalance = 0.00f;
                            rt.CategoryId = pSubCategoryId;
                            rt.SubCategoryName = MyDatabase.MyDB().getSubCategory(pSubCategoryId).SubCategoryName;
                            rt.Comments = "planned";
                            rt.BudgetYear = pYear;
                            rt.BudgetMonth = pMonth;
                            rt.HideBalance = true;
                            budgetTrans.add(rt);
                        }
                    }

                    Date lNewDate = new Date();
                    DateUtils.dateUtils().AddDays(lCurrentDate, 1, lNewDate);
                    lCurrentDate = lNewDate;
                } while (lCurrentDate.getTime() <= lBudgetEnd.getTime());
            }
        }

        return(budgetTrans);
    }

    RecordPlanned getSinglePlanned(Integer pPlannedId)
    {
            try (SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query("tblPlanned", new String[]{"PlannedId", "PlannedType",
                        "PlannedName", "SubCategoryId", "SortCode", "AccountNo", "PlannedDate",
                        "PlannedMonth", "PlannedDay", "Monday", "Tuesday",
                        "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "StartDate",
                        "EndDate", "MatchingTxType", "MatchingTxDescription", "MatchingTxAmount",
                        "AutoMatchTransaction", "PaidInParts", "FrequencyMultiplier"},
                    "PlannedId=?",
                    new String[]{pPlannedId.toString()}, null, null, null, null);
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
    
                            RecordPlanned rp = new RecordPlanned
                                (
                                    Integer.parseInt(cursor.getString(0).trim()),
                                    Integer.parseInt(cursor.getString(1).trim()),
                                    cursor.getString(2).trim(),
                                    Integer.parseInt(cursor.getString(3).trim()),
                                    cursor.getString(4).trim(),
                                    cursor.getString(5).trim(),
                                    new Date(Long.parseLong(cursor.getString(6).trim())),
                                    Integer.parseInt(cursor.getString(7).trim()),
                                    Integer.parseInt(cursor.getString(8).trim()),
                                    Integer.parseInt(cursor.getString(9).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(10).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(11).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(12).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(13).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(14).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(15).trim()) == 1 ? TRUE : FALSE,
                                    new Date(Long.parseLong(cursor.getString(16).trim())),
                                    new Date(Long.parseLong(cursor.getString(17).trim())),
                                    cursor.getString(18).trim(),
                                    cursor.getString(19).trim(),
                                    Float.parseFloat(cursor.getString(20).trim()),
                                    Integer.parseInt(cursor.getString(21).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(22).trim()) == 1 ? TRUE : FALSE,
                                    Integer.parseInt(cursor.getString(23).trim())
                                );
                            rp.variations = MyDatabase.MyDB().getVariationList(rp.mPlannedId);
                            Long lDate = DateUtils.dateUtils().StripTimeElement(rp.mPlannedDate);
                            rp.mPlannedDate = new Date(lDate);
                            return(rp);
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }
        return (new RecordPlanned());
    }
    
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 7 && newVersion == 8)
        {
            MyLog.WriteLogMessage("Creating tblPlanned");
            onCreate(db);
        }
        if (oldVersion == 13 && newVersion == 14)
        {
            MyLog.WriteLogMessage("Altering tblPlanned - adding column AutoMatchTransaction");
            db.execSQL("ALTER TABLE tblPlanned ADD COLUMN AutoMatchTransaction INTEGER DEFAULT 0");
        }
        if (oldVersion == 22 && newVersion == 23)
        {
            MyLog.WriteLogMessage("Altering tblPlanned - adding column FrequencyMultiplier");
            db.execSQL("ALTER TABLE tblPlanned ADD COLUMN FrequencyMultiplier INTEGER DEFAULT 1");
            db.execSQL("UPDATE tblPlanned set FrequencyMultiplier=1");
        }
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("DB Version " + Integer.toString(db.getVersion()) + ". " +
                "Downgrading from " + Integer.toString(oldVersion) +
                " down to " + Integer.toString(newVersion) );
    }

}
