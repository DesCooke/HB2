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

class TableHelpPage extends TableBase
{
    TableHelpPage(SQLiteOpenHelper argHelper)
    {
        super(argHelper);
    }

    private void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql = "DROP TABLE IF EXISTS tblHelpPage";
        executeSQL(lSql, "TableHelpPage::dropTableIfExists", db);
    }

    void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE tblHelpPage " +
                        " (" +
                        "   HelpPageId INTEGER PRIMARY KEY, " +
                        "   HelpPageSeq INTEGER, " +
                        "   HelpPageTitle TEXT " +
                        ") ";

        executeSQL(lSql, "TableHelpPage::onCreate", db);
    }

    private int getNextHelpPageId()
    {
        String lSql = "SELECT MAX(HelpPageId) FROM tblHelpPage ";

        return (getMaxPlus1(lSql, "TableHelpPage::getNextHelpPageId"));
    }

    private int getNextHelpPageSeq()
    {
        String lSql = "SELECT MAX(HelpPageSeq) FROM tblHelpPage ";

        return (getMaxPlus1(lSql, "TableHelpPage::getNextHelpPageSeq"));
    }

    void addHelpPage(RecordHelpPage hp)
    {
        hp.HelpPageId = getNextHelpPageId();
        hp.HelpPageSeq = getNextHelpPageSeq();

        String lSql =
                "INSERT INTO tblHelpPage " +
                        "(HelpPageId, HelpPageSeq, HelpPageTitle) " +
                        "VALUES (" +
                        Integer.toString(hp.HelpPageId) + "," +
                        Integer.toString(hp.HelpPageSeq) + "," +
                        "'" + hp.HelpPageTitle.trim() + "' " +
                        ") ";

        executeSQL(lSql, "TableHelpPage::addHelpPage", null);
    }

    void updateHelpPage(RecordHelpPage hp) {
        String lSql =
                "UPDATE tblHelpPage " +
                        " SET HelpPageSeq = " + Integer.toString(hp.HelpPageSeq) + "," +
                        " HelpPageTitle = '" + hp.HelpPageTitle.trim() + "' " +
                        "WHERE HelpPageId = " + Integer.toString(hp.HelpPageId);

        executeSQL(lSql, "TableHelpPage::updateHelpPage", null);
    }

    void deleteHelpPage(RecordHelpPage hp)
    {
        String lSql =
                "DELETE FROM tblHelpPage " +
                        "WHERE HelpPageId = " + Integer.toString(hp.HelpPageId);

        executeSQL(lSql, "TableHelpPage::deleteHelpPage", null);
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 23 && newVersion == 24)
        {
            MyLog.WriteLogMessage("Creating tblHelpPage");
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
