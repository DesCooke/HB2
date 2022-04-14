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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.example.cooked.hb2.Database.RecordPlanned.mPTMonthly;
import static com.example.cooked.hb2.Database.RecordPlanned.mPTOneOff;
import static com.example.cooked.hb2.Database.RecordPlanned.mPTWeekly;
import static com.example.cooked.hb2.Database.RecordPlanned.mPTYearly;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class TablePlannedVariation extends TableBase
{
    TablePlannedVariation(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }

    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblPlannedVariation";
        executeSQL(lSql, "TablePlannedVariation::dropTableIfExists", db);
    }

    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE tblPlannedVariation " +
                        " (" +
                        "   VariationId INTEGER PRIMARY KEY, " +
                        "   PlannedId INTEGER, " +
                        "   VariationName TEXT, " +
                        "   EffDate INTEGER, " +
                        "   Amount FLOAT" +
                        ") ";

        executeSQL(lSql, "TablePlannedVariation::onCreate", db);
    }

    private int getNextVariationId()
    {
        String lSql = "SELECT MAX(VariationId) FROM tblPlannedVariation ";

        return (getMaxPlus1(lSql, "TablePlannedVariation::getNextVariationId"));
    }

    void addVariation(RecordPlannedVariation rpv)
    {
        rpv.mVariationId = getNextVariationId();

        String lSql =
                "INSERT INTO tblPlannedVariation " +
                        "(VariationId, PlannedId, VariationName, EffDate, Amount) " +
                        "VALUES (" +
                        Integer.toString(rpv.mVariationId) + "," +
                        Integer.toString(rpv.mPlannedId) + "," +
                        "'" + rpv.mVariationName + "'," +
                        Long.toString(DateUtils.dateUtils().StripTimeElement(rpv.mEffDate)) + "," +
                        Float.toString(rpv.mAmount) + " " +
                        ") ";

        executeSQL(lSql, "TablePlannedVariation::addVariation", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void updateVariation(RecordPlannedVariation rpv) {
        String lSql =
                "UPDATE tblPlannedVariation " +
                        " SET VariationName = '" + rpv.mVariationName + "'," +
                        " EffDate = " + Long.toString(DateUtils.dateUtils().StripTimeElement(rpv.mEffDate)) + "," +
                        " Amount = " + Float.toString(rpv.mAmount) + " " +
                        "WHERE VariationId = " + Integer.toString(rpv.mVariationId);

        executeSQL(lSql, "TablePlannedVariation::updateVariation", null);
        MyDatabase.MyDB().Dirty=true;
    }

    void deleteVariation(RecordPlannedVariation rpv)
    {
        String lSql =
                "DELETE FROM tblPlannedVariation " +
                        "WHERE VariationId = " + Integer.toString(rpv.mVariationId);

        executeSQL(lSql, "TablePlannedVariation::deleteVariation", null);
        MyDatabase.MyDB().Dirty=true;
    }


    void deleteVariationForPlannedId(int pPlannedId)
    {
        String lSql =
                "DELETE FROM tblPlannedVariation " +
                        "WHERE PlannedId = " + Integer.toString(pPlannedId);

        executeSQL(lSql, "TablePlannedVariation::deleteVariationForPlannedId", null);
        MyDatabase.MyDB().Dirty=true;
    }

    ArrayList<RecordPlannedVariation> getVariationList(int pPlannedId)
    {
        ArrayList<RecordPlannedVariation> list;
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String l_SQL = "SELECT VariationId, PlannedId, VariationName, EffDate, Amount " +
                    " FROM tblPlannedVariation " +
                    " WHERE PlannedId = " + pPlannedId + " " +
                    " ORDER BY EffDate DESC ";

            Cursor cursor = db.rawQuery(l_SQL, null);
            try
            {
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
                                Long lOrigDate=Long.parseLong(cursor.getString(3));
                                Long lNewDate=DateUtils.dateUtils().StripTimeElement(new Date(lOrigDate));
                                Date lDate=new Date(lNewDate);
                                RecordPlannedVariation lrec =
                                        new RecordPlannedVariation
                                                (
                                                        Integer.parseInt(cursor.getString(0)),
                                                        Integer.parseInt(cursor.getString(1)),
                                                        cursor.getString(2),
                                                        lDate,
                                                        Float.parseFloat(cursor.getString(4))
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
            catch (Exception e)
            {
                list = new ArrayList<>();
                ErrorDialog.Show("Error in TablePlannedVariation.getVariationList", e.getMessage());
            }
        }
        catch (Exception e)
        {
            list = new ArrayList<>();
            ErrorDialog.Show("Error in TablePlannedVariation.getVariationList", e.getMessage());
        }

        return list;
    }

    RecordPlannedVariation getSingleVariation(Integer pVariationId)
    {
        try (SQLiteDatabase db = helper.getReadableDatabase())
        {
            String l_SQL = "SELECT VariationId, PlannedId, VariationName, EffDate, Amount " +
                    " FROM tblPlannedVariation " +
                    " WHERE VariationId = " + pVariationId;

            Cursor cursor = db.rawQuery(l_SQL, null);
            try
            {
                if (cursor != null)
                {
                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();
                                Long lOrigDate=Long.parseLong(cursor.getString(3));
                                Long lNewDate=DateUtils.dateUtils().StripTimeElement(new Date(lOrigDate));
                                Date lDate=new Date(lNewDate);
                                RecordPlannedVariation lrec =
                                        new RecordPlannedVariation
                                                (
                                                        Integer.parseInt(cursor.getString(0)),
                                                        Integer.parseInt(cursor.getString(1)),
                                                        cursor.getString(2),
                                                        lDate,
                                                        Float.parseFloat(cursor.getString(4))
                                                );
                                return  lrec;
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }
            catch (Exception e)
            {
                ErrorDialog.Show("Error in TablePlannedVariation.getSingleVariation", e.getMessage());
            }
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TablePlannedVariation.getSingleVariation", e.getMessage());
        }

        return null;
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 18 && newVersion == 19)
        {
            MyLog.WriteLogMessage("Creating tblPlannedVariation");
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
