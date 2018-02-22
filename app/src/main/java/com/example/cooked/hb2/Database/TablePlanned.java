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
                "UPDATE tblTransaction " +
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
    
    public ArrayList<RecordPlanned> getPlannedList(String sortCode, String accountNum)
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
                    "SortCode=? AND AccountNo=?",
                    new String[]{sortCode, accountNum}, null, null, null, null);
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
