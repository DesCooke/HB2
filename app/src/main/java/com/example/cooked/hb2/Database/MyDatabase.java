package com.example.cooked.hb2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
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
    private static final int DATABASE_VERSION = 9;
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
        try
        {
            ArrayList<RecordTransaction> rta = tableTransaction.getTransactionList(sortCode, accountNum);
    
            if (sortCode.compareTo("11-03-95") == 0)
            {
                int lBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
                int lBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
                ArrayList<RecordTransaction> rba = tablePlanned.getOutstandingList(sortCode, accountNum, lBudgetMonth, lBudgetYear);
                if (rba != null)
                {
                    Float lBal = 0.00f;
                    if (rta.size() > 0)
                    {
                        lBal = rta.get(0).TxBalance;
                    }
                    for (int i = 0; i < rba.size(); i++)
                    {
                        lBal = lBal + rba.get(i).TxAmount;
                        rba.get(i).TxBalance = lBal;
                        rta.add(0, rba.get(i));
                    }
            
                }
            }
    
            if (rta != null)
            {
                for (int i = 0; i < rta.size(); i++)
                {
                    RecordSubCategory sc = tableSubCategory.getSubCategory(rta.get(i).CategoryId);
                    if (sc != null)
                        rta.get(i).SubCategoryName = sc.SubCategoryName;
                }
            }
            return (rta);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getTransactionList", e.getMessage());
        }
        return(null);
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
        try
        {
            RecordTransaction rt = tableTransaction.getSingleTransaction(pTxSeqNo);
            if (rt != null)
            {
                RecordSubCategory sc = tableSubCategory.getSubCategory(rt.CategoryId);
                if (sc != null)
                    rt.SubCategoryName = sc.SubCategoryName;
            }
            return (rt);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getSingleTransaction", e.getMessage());
        }
        return(null);
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

    public RecordSubCategory getSubCategory(Integer pSubCategoryId)
    {
        return(tableSubCategory.getSubCategory(pSubCategoryId));
    }
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
        try
        {
            RecordTransaction rt = tableTransaction.getSingleTransaction(pTxSeqNo);
            if (rt != null)
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
                rp.mEndDate = dateUtils().StrToDate(MainActivity.context.getString(R.string.end_of_time));
        
                rp.mMatchingTxType = rt.TxType;
                rp.mMatchingTxDescription = rt.TxDescription;
                rp.mMatchingTxAmount = rt.TxAmount;
        
                rp.mPlanned = "";
                rp.mSubCategoryName = rt.SubCategoryName;
        
                tablePlanned.addPlanned(rp);
        
                return (rp.mPlannedId);
        
            }
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.createPlanned", e.getMessage());
        }
        
        return(0);
        
    }

    public void updatePlanned(RecordPlanned rp)
    {
        tablePlanned.updatePlanned(rp);
    }

    public ArrayList<RecordPlanned> getPlannedList()
    {
        try
        {
            ArrayList<RecordPlanned> rpa = tablePlanned.getPlannedList();
            if (rpa != null)
            {
                for (int i = 0; i < rpa.size(); i++)
                {
                    RecordSubCategory sc = tableSubCategory.getSubCategory(rpa.get(i).mSubCategoryId);
                    if (sc != null)
                        rpa.get(i).mSubCategoryName = sc.SubCategoryName;
                }
            }
            return (rpa);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getPlannedList", e.getMessage());
        }
        return(null);
    }

    public RecordPlanned getSinglePlanned(Integer pPlannedId)
    {
        try
        {
            RecordPlanned rp = tablePlanned.getSinglePlanned(pPlannedId);
            if (rp != null)
            {
                RecordSubCategory sc = tableSubCategory.getSubCategory(rp.mSubCategoryId);
                if (sc != null)
                    rp.mSubCategoryName = sc.SubCategoryName;
            }
            return (rp);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getSinglePlanned", e.getMessage());
        }
        return(null);
    }
    
    private void ProcessGroup(ArrayList<RecordCategory> cl, ArrayList<RecordBudget> rb,
                              ArrayList<RecordBudget> rbspent, RecordBudgetGroup mainGroup,
                              ArrayList<RecordBudgetGroup> lList, Integer pCategoryType)
    {
        try
        {
            RecordBudgetGroup rbg;
    
            for (int i = 0; i < cl.size(); i++)
            {
                rbg = new RecordBudgetGroup();
                rbg.budgetGroupName = cl.get(i).CategoryName;
                rbg.CategoryId = cl.get(i).CategoryId;
                rbg.RecCount = 0;
        
                ArrayList<RecordSubCategory> scl = tableSubCategory.getSubCategoryList(rbg.CategoryId);
                RecordBudgetItem rbi;
                RecordBudget rb2;
                for (int j = 0; j < scl.size(); j++)
                {
                    if (scl.get(j).SubCategoryType.intValue() != pCategoryType.intValue())
                        continue;
    
                /*
                Is there a planned transaction for this sub category?
                 */
                    rb2 = null;
                    for (int k = 0; k < rb.size(); k++)
                    {
                        if (rb.get(k).SubCategoryId == scl.get(j).SubCategoryId)
                        {
                            rb2 = rb.get(k);
                            break;
                        }
                    }
                    if (rb2 != null)
                    {
                    /*
                    Yes there is - have we spent anything of it?
                     */
                        rbi = new RecordBudgetItem();
                
                        for (int l = 0; l < rbspent.size(); l++)
                        {
                            if (rbspent.get(l).SubCategoryId == rb2.SubCategoryId)
                            {
                            /* yes - update spent totals */
                                rbi.spent = rbspent.get(l).Amount;
                                rbg.spent += rbi.spent;
                                mainGroup.spent += rbi.spent;
                                break;
                            }
                        }
                
                        rbi.budgetItemName = scl.get(j).SubCategoryName;
                        rbi.SubCategoryId = scl.get(j).SubCategoryId;
                        rbi.total = rb2.Amount;
                        rbg.total += rbi.total;
                        mainGroup.total += rbi.total;
                        rbi.RecCount = 0;
                        rbg.budgetItems.add(rbi);
                    } else
                    {
                    /*
                    Nope - unplanned expense
                     */
                        for (int l = 0; l < rbspent.size(); l++)
                        {
                            if (rbspent.get(l).SubCategoryId == scl.get(j).SubCategoryId)
                            {
                                rb2 = rbspent.get(l);
                                break;
                            }
                        }
                        if (rb2 != null)
                        {
                    
                            rbi = new RecordBudgetItem();
                    
                            rbi.budgetItemName = scl.get(j).SubCategoryName;
                            rbi.SubCategoryId = scl.get(j).SubCategoryId;
                            rbi.total = 0.00f;
                            rbi.spent = rb2.Amount;
                            rbi.outstanding = rbi.total - rb2.Amount;
                            rbg.spent += rb2.Amount;
                            rbg.outstanding += rbi.outstanding;
                            mainGroup.spent += rbi.spent;
                            mainGroup.outstanding += rbi.outstanding;
                            rbi.RecCount = 0;
                            rbg.budgetItems.add(rbi);
                        }
                    }
            
                }
                if (rbg.budgetItems.size() > 0)
                    lList.add(rbg);
            }
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.processGroup", e.getMessage());
        }
        
    }
    public ArrayList<RecordBudgetGroup> getBudget(Integer pMonth, Integer pYear)
    {
        try
        {
            ArrayList<RecordBudgetGroup> lList = new ArrayList<>();
    
            RecordBudgetGroup mrbg;
    
            ArrayList<RecordBudget> rb = tablePlanned.getBudgetList(pMonth, pYear);
            ArrayList<RecordBudget> rbspent = tablePlanned.getBudgetSpent(pMonth, pYear);
            ArrayList<RecordCategory> cl = tableCategory.getCategoryList();
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_monthly_expenses);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTMonthlyExpense);
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_monthly_income);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTMonthlyIncome);
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_extra_expenses);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTExtraExpense);
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_extra_income);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTExtraIncome);
    
            return (lList);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getbudget", e.getMessage());
        }
        
        return(null);
    }
    //endregion
  
    
 }

 