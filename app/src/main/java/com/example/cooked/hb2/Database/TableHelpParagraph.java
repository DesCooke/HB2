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

class TableHelpParagraph extends TableBase
{
    TableHelpParagraph(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }

    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblHelpParagraph";
        executeSQL(lSql, "TableHelpParagraph::dropTableIfExists", db);
    }

    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE tblHelpParagraph " +
                        " (" +
                        "   HelpParagraphId INTEGER PRIMARY KEY, " +
                        "   HelpParagraphSeq INTEGER, " +
                        "   HelpParagraphType INTEGER, " +
                        "   HelpParagraphText TEXT " +
                        ") ";

        executeSQL(lSql, "TableHelpParagraph::onCreate", db);
    }

    private int getNextHelpParagraphId()
    {
        String lSql = "SELECT MAX(HelpParagraphId) FROM tblHelpParagraph ";

        return (getMaxPlus1(lSql, "TableHelpParagraph::getNextHelpParagraphId"));
    }

    private int getNextHelpParagraphSeq()
    {
        String lSql = "SELECT MAX(HelpParagraphSeq) FROM tblHelpParagraph ";

        return (getMaxPlus1(lSql, "TableHelpParagraph::getNextHelpParagraphSeq"));
    }

    void addHelpParagraph(RecordHelpParagraph hp)
    {
        hp.HelpParagraphId = getNextHelpParagraphId();
        hp.HelpParagraphSeq = getNextHelpParagraphSeq();

        String lSql =
                "INSERT INTO tblHelpParagraph " +
                        "(HelpParagraphId, HelpPageSeq, HelpParagraphType, HelpParagraphText) " +
                        "VALUES (" +
                        Integer.toString(hp.HelpParagraphId) + "," +
                        Integer.toString(hp.HelpParagraphSeq) + "," +
                        Integer.toString(hp.HelpParagraphType) + ", " +
                        "'" + hp.HelpParagraphText.trim() + "' " +
                        ") ";

        executeSQL(lSql, "TableHelpParagraph::addHelpParagraph", null);
    }

    void updateHelpParagraph(RecordHelpParagraph hp) {
        String lSql =
                "UPDATE tblHelpParagraph " +
                        " SET HelpParagraphSeq = " + Integer.toString(hp.HelpParagraphSeq) + "," +
                        " HelpParagraphText = '" + hp.HelpParagraphText.trim() + "' " +
                        "WHERE HelpParagraphId = " + Integer.toString(hp.HelpParagraphId);

        executeSQL(lSql, "TableHelpParagraph::updateHelpParagraph", null);
    }

    void deleteHelpParagraph(RecordHelpParagraph hp)
    {
        String lSql =
                "DELETE FROM tblHelpParagraph " +
                        "WHERE HelpParagraphId = " + Integer.toString(hp.HelpParagraphId);

        executeSQL(lSql, "TableHelpParagraph::deleteHelpParagraph", null);
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 23 && newVersion == 24)
        {
            MyLog.WriteLogMessage("Creating tblHelpParagraph");
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
