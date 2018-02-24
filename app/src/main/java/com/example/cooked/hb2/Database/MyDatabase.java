package com.example.cooked.hb2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

// derived from SQLiteOpenHelper because it give lots of features like - upgrade/downgrade
// automatically handles the creation and recreation of the database
public class MyDatabase extends SQLiteOpenHelper
{
    //region Member Variables
    // these regions help to compartmentalise the unit

    // The version - each change - increment by one
    // if the version increases onUpgrade is called - if decreases - onDowngrade is called
    // if current is 0 (does not exist) onCreate is called
    private static final int DATABASE_VERSION = 8;
    private static MyDatabase myDB;
    private TableTransaction tableTransaction;
    private TableCategory tableCategory;
    private TableSubCategory tableSubCategory;
    private TablePlanned tablePlanned;
    //endregion
    
    //region statics
    public static MyDatabase MyDB()
    {
        if(myDB==null)
          myDB = new MyDatabase(MainActivity.context);
        return(myDB);
    }
    //endregion

    //region Database functions
    private MyDatabase(Context context)
    {
        // super has to be the first command - can't put anything before it
        super(context, context.getResources().getString(R.string.database_filename), null, DATABASE_VERSION);
        try
        {
            tableTransaction = new TableTransaction(this);
            tableCategory = new TableCategory(this);
            tableSubCategory = new TableSubCategory(this);
            tablePlanned = new TablePlanned(this);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::MyDatabase", e.getMessage());
        }
    }

    // called when the current database version is 0
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            tableTransaction.onCreate(db);
            tableCategory.onCreate(db);
            tableSubCategory.onCreate(db);
            tablePlanned.onCreate(db);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::onCreate", e.getMessage());
        }
    }

    // called when the version number increases
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            MyLog.WriteLogMessage("Upgrading database from " + Integer.toString(oldVersion) + " to " +
            Integer.toString(newVersion));

            tableTransaction.onUpgrade(db, oldVersion, newVersion);
            tableCategory.onUpgrade(db, oldVersion, newVersion);
            tableSubCategory.onUpgrade(db, oldVersion, newVersion);
            tablePlanned.onUpgrade(db, oldVersion, newVersion);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::onUpgrade", e.getMessage());
        }
    }

    // called when the version number decreases
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            tableTransaction.onDowngrade(db, oldVersion, newVersion);
            tableCategory.onDowngrade(db, oldVersion, newVersion);
            tableSubCategory.onDowngrade(db, oldVersion, newVersion);
            tablePlanned.onDowngrade(db, oldVersion, newVersion);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::onDowngrade", e.getMessage());
        }
    }
    //endregion

    //region Transaction Functions
    public void addTransaction(RecordTransaction rt)
    {
        tableTransaction.addTransaction(rt);
    }
    public void deleteTransaction(RecordTransaction rt)
    {
        tableTransaction.deleteTransaction(rt);
    }

    public void updateTransaction(RecordTransaction rt)
    {
        tableTransaction.updateTransaction(rt);
    }

    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum)
    {
        ArrayList<RecordTransaction> rta = tableTransaction.getTransactionList(sortCode, accountNum);
        if(rta!=null)
        {
            for(int i=0;i<rta.size();i++) {
                RecordSubCategory sc = tableSubCategory.getSubCategory(rta.get(i).CategoryId);
                if (sc != null)
                    rta.get(i).SubCategoryName = sc.SubCategoryName;
            }
        }
        return(rta);
    }

    public ArrayList<RecordTransaction> getTxDateRange(Date lFrom, Date lTo, String lSortCode, String lAccountNumber)
    {
        return tableTransaction.getTxDateRange(lFrom, lTo, lSortCode, lAccountNumber);
    }

    public void updateFilenameLineNo(RecordTransaction dbRec, RecordTransaction fileRec)
    {
        tableTransaction.updateFilenameLineNo(dbRec, fileRec);
        
    }

    public int getNextTxLineNo(Date pDate)
    {
        return(tableTransaction.getNextTxLineNo(pDate));
    }

    public RecordTransaction getSingleTransaction(Integer pTxSeqNo)
    {
        RecordTransaction rt =tableTransaction.getSingleTransaction(pTxSeqNo);
        if(rt!=null)
        {
           RecordSubCategory sc = tableSubCategory.getSubCategory(rt.CategoryId);
           if(sc!=null)
               rt.SubCategoryName = sc.SubCategoryName;
        }
        return(rt);
    }
    //endregion
    
    //region Category functions
    public void addCategory(RecordCategory rc) { tableCategory.addCategory(rc);}

    public void updateCategory(RecordCategory rc) { tableCategory.updateCategory(rc);}

    public void deleteCategory(RecordCategory rc)
    {
        try {
            ArrayList<RecordSubCategory> rscList;
            rscList = getSubCategoryList(rc.CategoryId);
            for (int i = 0; i < rscList.size(); i++) {
                tableSubCategory.deleteSubCategory(rscList.get(i));
            }
            tableCategory.deleteCategory(rc);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.deleteCategory", e.getMessage());
        }
    }

    public ArrayList<RecordCategory> getCategoryList()
    {
        return tableCategory.getCategoryList();
    }
    //endregion
    
    
//region SubCategory functions
    public void addSubCategory(RecordSubCategory rc) { tableSubCategory.addSubCategory(rc);}

    public void updateSubCategory(RecordSubCategory rc) { tableSubCategory.updateSubCategory(rc);}

    public void deleteSubCategory(RecordSubCategory rc) { tableSubCategory.deleteSubCategory(rc);}

    public ArrayList<RecordSubCategory> getSubCategoryList(Integer pCategoryId)
    {
        return tableSubCategory.getSubCategoryList(pCategoryId);
    }
    
    //endregion
    
    //region Planned Functions
    public void addPlanned(RecordPlanned rp)
    {
        tablePlanned.addPlanned(rp);
    }
    public void deletePlanned(RecordPlanned rp)
    {
        tablePlanned.deletePlanned(rp);
    }

    public int createPlanned(int pTxSeqNo)
    {
        RecordTransaction rt = tableTransaction.getSingleTransaction(pTxSeqNo);
        if(rt!=null)
        {
            RecordPlanned rp = new RecordPlanned();
            rp.mPlannedId = 0;
            rp.mPlannedType = RecordPlanned.mPTMonthly;
            rp.mPlannedName = rt.TxDescription;
            rp.mSubCategoryId = rt.CategoryId;
            rp.mSortCode = rt.TxSortCode;
            rp.mAccountNo = rt.TxAccountNumber;

            rp.mPlannedDate = rt.TxDate;
            rp.mPlannedMonth = 0;
            Calendar c = Calendar.getInstance();
            c.setTime(rt.TxDate);
            rp.mPlannedDay = c.get(Calendar.DAY_OF_MONTH);
            rp.mMonday = false;
            rp.mTuesday = false;
            rp.mWednesday = false;
            rp.mThursday = false;
            rp.mFriday = false;
            rp.mSaturday = false;
            rp.mSunday = false;

            rp.mStartDate = rt.TxDate;
            rp.mEndDate = dateUtils().StrToDate("31/12/2099");

            rp.mMatchingTxType = rt.TxType;
            rp.mMatchingTxDescription = rt.TxDescription;
            rp.mMatchingTxAmount = rt.TxAmount;

            rp.mPlanned = "";
            rp.mSubCategoryName = rt.SubCategoryName;

            tablePlanned.addPlanned(rp);

            return(rp.mPlannedId);

        }
        return(0);
    }

    public void updatePlanned(RecordPlanned rp)
    {
        tablePlanned.updatePlanned(rp);
    }

    public ArrayList<RecordPlanned> getPlannedList()
    {
        ArrayList<RecordPlanned> rpa = tablePlanned.getPlannedList();
        if(rpa!=null)
        {
            for(int i=0;i<rpa.size();i++) {
                RecordSubCategory sc = tableSubCategory.getSubCategory(rpa.get(i).mSubCategoryId);
                if (sc != null)
                    rpa.get(i).mSubCategoryName = sc.SubCategoryName;
            }
        }
        return(rpa);
    }

    public RecordPlanned getSinglePlanned(Integer pPlannedId)
    {
        RecordPlanned rp =tablePlanned.getSinglePlanned(pPlannedId);
        if(rp!=null) {
            RecordSubCategory sc = tableSubCategory.getSubCategory(rp.mSubCategoryId);
            if (sc != null)
                rp.mSubCategoryName = sc.SubCategoryName;
        }
        return(rp);
    }
    //endregion
    
 }

 