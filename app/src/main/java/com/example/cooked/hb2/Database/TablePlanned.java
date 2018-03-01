package com.example.cooked.hb2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.ArrayList;
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

    public class SortByNextDueDateComparator implements Comparator<RecordBudget>
    {
        public int compare(RecordBudget left, RecordBudget right) {
            return left.NextDueDate.compareTo(right.NextDueDate);
        }
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
                "   MatchingTxAmount FLOAT " +
                ") ";
        
        executeSQL(lSql, "TablePlanned::onCreate", db);
    }
    
    public int getNextPlannedId()
    {
        String lSql = "SELECT MAX(PlannedId) FROM tblPlanned ";
        
        return (getMaxPlus1(lSql, "TablePlanned::getNextPlannedId"));
    }
    
    public void addPlanned(RecordPlanned rp)
    {
        rp.mPlannedId = getNextPlannedId();
        
        String lSql =
            "INSERT INTO tblPlanned " +
                "(PlannedId, PlannedType, PlannedName, SubCategoryId, SortCode, AccountNo, PlannedDate, PlannedMonth, PlannedDay, Monday, " +
                "Tuesday, Wednesday, Thursday, Friday, Saturday, " +
                "Sunday, StartDate, EndDate, MatchingTxType, MatchingTxDescription, MatchingTxAmount) " +
                "VALUES (" +
                Integer.toString(rp.mPlannedId) + "," +
                Integer.toString(rp.mPlannedType) + "," +
                "'" + rp.mPlannedName + "'," +
                Integer.toString(rp.mSubCategoryId) + "," +
                    "'" + rp.mSortCode + "'," +
                    "'" + rp.mAccountNo + "'," +
                Long.toString(rp.mPlannedDate.getTime()) + "," +
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
                "'" + rp.mMatchingTxType + "'," +
                "'" + rp.mMatchingTxDescription + "'," +
                Float.toString(rp.mMatchingTxAmount) + " " +
        ") ";
        
        executeSQL(lSql, "TablePlanned::addPlanned", null);
    }
    
    public void updatePlanned(RecordPlanned rp) {
        String lSql =
                "UPDATE tblPlanned " +
                        " SET PlannedType = " + Integer.toString(rp.mPlannedType) + "," +
                        " PlannedName = '" + rp.mPlannedName + "'," +
                        " SubCategoryId = " + Integer.toString(rp.mSubCategoryId) + "," +
                        " SortCode = '" + rp.mSortCode + "'," +
                        " AccountNo = '" + rp.mAccountNo + "'," +
                        " PlannedDate = " + Long.toString(rp.mPlannedDate.getTime()) + "," +
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
                        " MatchingTxType = '" + rp.mMatchingTxType + "'," +
                        " MatchingTxDescription = '" + rp.mMatchingTxDescription + "'," +
                        " MatchingTxAmount = " + Float.toString(rp.mMatchingTxAmount) + " " +
                        "WHERE PlannedId = " + Integer.toString(rp.mPlannedId);

        executeSQL(lSql, "TablePlanned::updatePlanned", null);
    }
    
    public void deletePlanned(RecordPlanned rec)
    {
        String lSql =
            "DELETE FROM tblPlanned " +
                "WHERE PlannedId = " + rec.mPlannedId.toString();
        
        executeSQL(lSql, "TablePlanned::deletePlanned", null);
    }
    
    public ArrayList<RecordPlanned> getPlannedList()
    {
        int cnt;
        ArrayList<RecordPlanned> list;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try
            {
                Cursor cursor = db.query("tblPlanned", new String[]{"PlannedId", "PlannedType",
                        "PlannedName", "SubCategoryId", "SortCode", "AccountNo", "PlannedDate",
                        "PlannedMonth", "PlannedDay", "Monday", "Tuesday",
                        "Wednesday","Thursday", "Friday", "Saturday", "Sunday", "StartDate",
                        "EndDate", "MatchingTxType", "MatchingTxDescription", "MatchingTxAmount"},
                    null,
                    null, null, null, "PlannedName", null);
                list = new ArrayList<RecordPlanned>();
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
                                RecordPlanned lrec =
                                    new RecordPlanned
                                        (
                                            Integer.parseInt(cursor.getString(0)),
                                            Integer.parseInt(cursor.getString(1)),
                                            cursor.getString(2),
                                            Integer.parseInt(cursor.getString(3)),
                                            cursor.getString(4),
                                            cursor.getString(5),
                                            new Date(Long.parseLong(cursor.getString(6))),
                                            Integer.parseInt(cursor.getString(7)),
                                            Integer.parseInt(cursor.getString(8)),
                                            Integer.parseInt(cursor.getString(9))==1?TRUE:FALSE,
                                            Integer.parseInt(cursor.getString(10))==1?TRUE:FALSE,
                                            Integer.parseInt(cursor.getString(11))==1?TRUE:FALSE,
                                            Integer.parseInt(cursor.getString(12))==1?TRUE:FALSE,
                                            Integer.parseInt(cursor.getString(13))==1?TRUE:FALSE,
                                            Integer.parseInt(cursor.getString(14))==1?TRUE:FALSE,
                                            Integer.parseInt(cursor.getString(15))==1?TRUE:FALSE,
                                            new Date(Long.parseLong(cursor.getString(16))),
                                            new Date(Long.parseLong(cursor.getString(17))),
                                            cursor.getString(18),
                                            cursor.getString(19),
                                            Float.parseFloat(cursor.getString(20))
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
            list = new ArrayList<RecordPlanned>();
            ErrorDialog.Show("Error in TablePlanned.getPlannedList", e.getMessage());
        }

        return list;
    }

    public ArrayList<RecordTransaction> getOutstandingList(String pSortCode, String pAccountNum, int pMonth, int pYear)
    {
        int cnt;
        ArrayList<RecordBudget> budgetSpent = new ArrayList<RecordBudget>();
        ArrayList<RecordBudget> budgetFull;
        ArrayList<RecordTransaction> list = new ArrayList<RecordTransaction>();
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try
            {
                String l_SQL = "SELECT CategoryId, SUM(TxAmount) FROM tblTransaction " +
                        "WHERE TxSortCode = '" + pSortCode + "' AND TxAccountNumber = '" +
                        pAccountNum + "' AND BudgetYear = " + pYear + " AND BudgetMonth = " +
                        pMonth + " GROUP BY CategoryId ";

                Cursor cursor = db.rawQuery(l_SQL, null);

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
                                RecordBudget lrec =
                                        new RecordBudget(
                                                Integer.parseInt(cursor.getString(0)),
                                                0,
                                                Float.parseFloat(cursor.getString(1)),
                                                "",
                                                "",
                                                false,
                                                null
                                        );
                                budgetSpent.add(lrec);
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
                if(lFound==false)
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

        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TablePlanned.getPlannedList", e.getMessage());
        }
        Collections.sort(list, new Comparator<RecordTransaction>() {
            public int compare(RecordTransaction o1, RecordTransaction o2) {
                return o1.TxDate.compareTo(o2.TxDate);
            }
        });
        return list;
    }

    public ArrayList<RecordBudget> getBudgetSpent(int pMonth, int pYear)
    {
        ArrayList<RecordBudget> budgetSpent = new ArrayList<RecordBudget>();
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try
            {
                String l_SQL = "SELECT CategoryId, SUM(TxAmount) FROM tblTransaction " +
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
                                                Integer.parseInt(cursor.getString(0)),
                                                0,
                                                Float.parseFloat(cursor.getString(1)),
                                                "",
                                                "",
                                                false,
                                                null
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
            finally
            {
                db.close();
            }

            return(budgetSpent);

        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TablePlanned.getBudgetSpent", e.getMessage());
        }
        return (null);
    }

    public Boolean isDue(RecordPlanned prp, Date pDate)
    {
        if(prp.mPlannedType==mPTOneOff)
        {
            if(prp.mPlannedDate==pDate)
                return(true);
            return(false);
        }
        if(prp.mPlannedType==mPTYearly) {
            MyInt lDay = new MyInt();
            MyInt lMonth = new MyInt();
            DateUtils.dateUtils().GetMonth(pDate, lMonth);
            DateUtils.dateUtils().GetDay(pDate, lDay);
            if(lMonth.Value==prp.mPlannedMonth && lDay.Value == prp.mPlannedDay)
                return(true);
            return(false);
        }
        if(prp.mPlannedType==mPTMonthly) {
            MyInt lDay = new MyInt();
            DateUtils.dateUtils().GetDay(pDate, lDay);
            if(lDay.Value == prp.mPlannedDay)
                return(true);
            return(false);
        }
        if(prp.mPlannedType==mPTWeekly) {
            MyString lMyString = new MyString();
            DateUtils.dateUtils().GetDayOfWeek(pDate, lMyString);
            if(lMyString.Value.compareTo("Mon")==0 && prp.mMonday)
                return(true);
            if(lMyString.Value.compareTo("Tue")==0 && prp.mTuesday)
                return(true);
            if(lMyString.Value.compareTo("Wed")==0 && prp.mWednesday)
                return(true);
            if(lMyString.Value.compareTo("Thu")==0 && prp.mThursday)
                return(true);
            if(lMyString.Value.compareTo("Fri")==0 && prp.mFriday)
                return(true);
            if(lMyString.Value.compareTo("Sat")==0 && prp.mSaturday)
                return(true);
            if(lMyString.Value.compareTo("Sun")==0 && prp.mSunday)
                return(true);
        }

        return(false);
    }

    public ArrayList<RecordBudget> getBudgetList(int pMonth, int pYear)
    {
        ArrayList<RecordPlanned> plannedList = new ArrayList<RecordPlanned>();
        ArrayList<RecordBudget> budgetFull = new ArrayList<RecordBudget>();
        int pCurrentDate;

        Date lBudgetStart = DateUtils.dateUtils().BudgetStart(pMonth, pYear);
        Date lBudgetEnd = DateUtils.dateUtils().BudgetEnd(pMonth, pYear);
        Date lCurrentDate;

        plannedList = getPlannedList();
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
                            lCurrentDate.getTime() <= rp.mEndDate.getTime()) {
                        if (isDue(rp, lCurrentDate)) {
                            if(rp.mNextDueDate==null)
                                rp.mNextDueDate = lCurrentDate;
                            lAtleastOne = true;
                            lAmount += rp.mMatchingTxAmount;
                        }
                    }

                    Date lNewDate = new Date();
                    DateUtils.dateUtils().AddDays(lCurrentDate, 1, lNewDate);
                    lCurrentDate = lNewDate;
                } while (lCurrentDate.getTime() <= lBudgetEnd.getTime());
                if (lAtleastOne) {
                    boolean lFound = false;
                    for (int j = 0; j < budgetFull.size(); j++) {
                        if (budgetFull.get(j).SubCategoryId == rp.mSubCategoryId) {
                            budgetFull.get(j).Amount += lAmount;
                            budgetFull.get(j).NextDueDate = rp.mNextDueDate;
                            lFound = true;
                        }
                    }
                    if (!lFound) {
                        Boolean lMonthlyBudget=false;
                        if(rp.mPlannedType==RecordPlanned.mPTMonthly ||
                                rp.mPlannedType==RecordPlanned.mPTWeekly)
                        {
                            lMonthlyBudget=true;
                        }
                        budgetFull.add(
                                new RecordBudget(
                                        rp.mSubCategoryId,
                                        0,
                                        lAmount,
                                        rp.mSubCategoryName,
                                        "",
                                        lMonthlyBudget,
                                        rp.mNextDueDate));
                    }
                }
            }
        }

        return(budgetFull);
    }

    public RecordPlanned getSinglePlanned(Integer pPlannedId)
    {
        int cnt;
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            try
            {
                Cursor cursor = db.query("tblPlanned", new String[]{"PlannedId", "PlannedType",
                        "PlannedName", "SubCategoryId", "SortCode", "AccountNo", "PlannedDate",
                        "PlannedMonth", "PlannedDay", "Monday", "Tuesday",
                        "Wednesday","Thursday", "Friday", "Saturday", "Sunday", "StartDate",
                        "EndDate", "MatchingTxType", "MatchingTxDescription", "MatchingTxAmount"},
                    "PlannedId=?",
                    new String[]{pPlannedId.toString()}, null, null, null, null);
                cnt = 0;
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();

                            RecordPlanned lrec =
                                new RecordPlanned
                                    (
                                        Integer.parseInt(cursor.getString(0)),
                                        Integer.parseInt(cursor.getString(1)),
                                        cursor.getString(2),
                                        Integer.parseInt(cursor.getString(3)),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        new Date(Long.parseLong(cursor.getString(6))),
                                        Integer.parseInt(cursor.getString(7)),
                                        Integer.parseInt(cursor.getString(8)),
                                        Integer.parseInt(cursor.getString(9)) == 1 ? TRUE : FALSE,
                                        Integer.parseInt(cursor.getString(10)) == 1 ? TRUE : FALSE,
                                        Integer.parseInt(cursor.getString(11)) == 1 ? TRUE : FALSE,
                                        Integer.parseInt(cursor.getString(12)) == 1 ? TRUE : FALSE,
                                        Integer.parseInt(cursor.getString(13)) == 1 ? TRUE : FALSE,
                                        Integer.parseInt(cursor.getString(14)) == 1 ? TRUE : FALSE,
                                        Integer.parseInt(cursor.getString(15)) == 1 ? TRUE : FALSE,
                                        new Date(Long.parseLong(cursor.getString(16))),
                                        new Date(Long.parseLong(cursor.getString(17))),
                                        cursor.getString(18),
                                        cursor.getString(19),
                                        Float.parseFloat(cursor.getString(20))
                                    );
                            return(lrec);
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
            ErrorDialog.Show("Error in TablePlanned.getSinglePlanned", e.getMessage());
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
    }
    
    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
