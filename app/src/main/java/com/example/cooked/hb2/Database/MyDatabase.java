package com.example.cooked.hb2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

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
    private static final int DATABASE_VERSION = 14;
    private static MyDatabase myDB;
    private TableTransaction tableTransaction;
    private TableCategory tableCategory;
    private TableCategoryBudget tableCategoryBudget;
    private TableSubCategory tableSubCategory;
    private TablePlanned tablePlanned;
    private TableCommon tableCommon;
    public TextView txtNotes;
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
            tableCategoryBudget = new TableCategoryBudget(this);
            tableSubCategory = new TableSubCategory(this);
            tablePlanned = new TablePlanned(this);
            tableCommon = new TableCommon(this);
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
            tableCategoryBudget.onCreate(db);
            tableSubCategory.onCreate(db);
            tablePlanned.onCreate(db);
            tableCommon.onCreate(db);
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
            tableCategoryBudget.onUpgrade(db, oldVersion, newVersion);
            tableSubCategory.onUpgrade(db, oldVersion, newVersion);
            tablePlanned.onUpgrade(db, oldVersion, newVersion);
            tableCommon.onUpgrade(db, oldVersion, newVersion);
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
            tableCategoryBudget.onDowngrade(db, oldVersion, newVersion);
            tableSubCategory.onDowngrade(db, oldVersion, newVersion);
            tablePlanned.onDowngrade(db, oldVersion, newVersion);
            tableCommon.onDowngrade(db, oldVersion, newVersion);
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

    public Float getCategoryBudgetSameMonthLastYear(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        return(tableTransaction.getCategoryBudgetSameMonthLastYear(pBudgetYear, pBudgetMonth, pCategoryId));
    }

    public Float getCategoryBudgetLastMonth(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        return(tableTransaction.getCategoryBudgetLastMonth(pBudgetYear, pBudgetMonth, pCategoryId));
    }

    public Float getCategoryBudgetAverage(Integer pCategoryId)
    {
        return(tableTransaction.getCategoryBudgetAverage(pCategoryId));
    }

    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum, boolean showPlanned)
    {
        try
        {
            ArrayList<RecordTransaction> rta = tableTransaction.getTransactionList(sortCode, accountNum);
    
            if (sortCode.compareTo("11-03-95") == 0)
            {
                if(showPlanned)
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
            }
    
            if (rta != null)
            {
                Float lCurrentBalance=0.00f;
                for (int i = 0; i < rta.size(); i++)
                {
                    rta.get(i).BalanceCorrect = true;
                    if(i>0)
                    {
                        Float lDiff = lCurrentBalance - rta.get(i).TxBalance;
                        if(lDiff < -0.005 || lDiff > 0.005)
                        {
                            rta.get(i).BalanceCorrect = false;
                            rta.get(i).TxBalanceShouldBe = lCurrentBalance;
                        }
                        lCurrentBalance = lCurrentBalance - rta.get(i).TxAmount;
                    }
                    else
                    {
                        lCurrentBalance = rta.get(i).TxBalance - rta.get(i).TxAmount;
                    }
                    
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

    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum,
                                                           boolean showPlanned, Integer budgetMonth,
                                                           Integer budgetYear, Boolean includeThisBudgetOnly)
    {
        try
        {
            ArrayList<RecordTransaction> rta = tableTransaction.getTransactionList(sortCode,
                    accountNum, budgetMonth, budgetYear, includeThisBudgetOnly);

            if (sortCode.compareTo("11-03-95") == 0)
            {
                if(showPlanned)
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
            }

            if (rta != null)
            {
                Float lCurrentBalance=0.00f;
                for (int i = 0; i < rta.size(); i++)
                {
                    rta.get(i).BalanceCorrect = true;
                    if(i>0)
                    {
                        Float lDiff = lCurrentBalance - rta.get(i).TxBalance;
                        if(lDiff < -0.005 || lDiff > 0.005)
                        {
                            rta.get(i).BalanceCorrect = false;
                            rta.get(i).TxBalanceShouldBe = lCurrentBalance;
                        }
                        lCurrentBalance = lCurrentBalance - rta.get(i).TxAmount;
                    }
                    else
                    {
                        lCurrentBalance = rta.get(i).TxBalance - rta.get(i).TxAmount;
                    }

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

    public ArrayList<RecordTransaction> getBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth, Integer pSubCatgegoryId)
    {
        try
        {
            ArrayList<RecordTransaction> rta = tableTransaction.getBudgetTrans(pBudgetYear, pBudgetMonth, pSubCatgegoryId);
            Float lTotal = 0.00f;
            int lRecTotal = 0;
            if (rta != null)
            {
                for (int i = 0; i < rta.size(); i++)
                {
                    RecordSubCategory sc = tableSubCategory.getSubCategory(rta.get(i).CategoryId);
                    if (sc != null)
                        rta.get(i).SubCategoryName = sc.SubCategoryName;
                    lTotal += rta.get(i).TxAmount;
                }
            }
            
            Float lCount = 0.00f;
            ArrayList<RecordTransaction> rpl = tablePlanned.getPlannedTransForSubCategoryId(pBudgetMonth, pBudgetYear, pSubCatgegoryId);
            for(int i=0;i<rpl.size();i++)
            {
                if(rta.size()==0)
                {
                    rta.add(rpl.get(i));
                }
                else
                {
                    lCount += rpl.get(i).TxAmount;
                    if (lTotal > 0.00f)
                    {
                        if (lCount > (lTotal + 0.00005f))
                            rta.add(rpl.get(i));
                    }
                    if (lTotal < 0.00f)
                    {
                        if (lCount < (lTotal - 0.00005f))
                            rta.add(rpl.get(i));
                    }
                }
            }
            Collections.sort(rta, new Comparator<RecordTransaction>()
            {
                public int compare(RecordTransaction o1, RecordTransaction o2)
                {
                    return o2.TxDate.compareTo(o1.TxDate);
                }
            });
            return (rta);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getBudgetTrans", e.getMessage());
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
            tableCategoryBudget.deleteAllForCategory(rc.CategoryId);

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

    public RecordCategory getCategory(Integer pCategoryId)
    {
        return tableCategory.getCategory(pCategoryId);
    }

    //endregion

    //region CategoryBudget functions
    public void addCategoryBudget(RecordCategoryBudget rcb) { tableCategoryBudget.addCategoryBudget(rcb);}

    public void deleteAllCategoryBudgets()
    {
        tableCategoryBudget.deleteAll();
    }
    public void updateCategoryBudget(RecordCategoryBudget rcb) { tableCategoryBudget.updateCategoryBudget(rcb);}

    public void deleteCategoryBudget(RecordCategoryBudget rcb)
    {
        tableCategoryBudget.deleteCategoryBudget(rcb);
    }

    public RecordCategoryBudget getCategoryBudget(Integer pCategoryId, Integer pMonthId,
                                                  Integer pYearId)
    {
        return tableCategoryBudget.getCategoryBudget(pCategoryId, pMonthId, pYearId);
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

    public ArrayList<RecordPlanned> getPlannedList(boolean activeOnly)
    {
        try
        {
            ArrayList<RecordPlanned> rpa = tablePlanned.getPlannedList(activeOnly);
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

    public ArrayList<RecordPlanned> getPlannedListForSubCategory(int pSubCategoryId)
    {
        try
        {
            ArrayList<RecordPlanned> rpa = tablePlanned.getPlannedListForSubCategory(pSubCategoryId);
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
            ErrorDialog.Show("Error in MyDatabase.getPlannedListForSubCategory", e.getMessage());
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
    
    private void ProcessGroup(Integer pMonth, Integer pYear,
                              ArrayList<RecordCategory> cl, ArrayList<RecordBudget> rb,
                              ArrayList<RecordBudget> rbspent, RecordBudgetGroup mainGroup,
                              ArrayList<RecordBudgetGroup> lList, Integer pCategoryType)
    {
        try
        {
            RecordBudgetGroup rbg;

            ArrayList<RecordBudgetGroup> localList = new ArrayList<RecordBudgetGroup>();

            for (int i = 0; i < cl.size(); i++)
            {
                rbg = new RecordBudgetGroup();
                rbg.BudgetMonth = pMonth;
                rbg.BudgetYear = pYear;
                rbg.budgetGroupName = cl.get(i).CategoryName;
                rbg.CategoryId = cl.get(i).CategoryId;
                rbg.RecCount = 0;
                rbg.groupedBudget = cl.get(i).GroupedBudget;
                rbg.DefaultBudgetType = cl.get(i).DefaultBudgetType;
        
                ArrayList<RecordSubCategory> scl = tableSubCategory.getSubCategoryList(rbg.CategoryId);
                RecordBudgetItem rbi;
                RecordBudget rb2;
                for (int j = 0; j < scl.size(); j++)
                {
                    // ignore if not for the selected category type
                    if (scl.get(j).SubCategoryType.intValue() != pCategoryType.intValue())
                        continue;
    
                    
                    //Is there a planned transaction for this sub category?
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
                        // Yes there is - have we spent anything of it?
                        rbi = new RecordBudgetItem();
                        rbi.groupedBudget = rbg.groupedBudget;
                        rbi.spent = 0.00f;
                
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
                        if(rb2.AutoMatchTransaction)
                            if(rbi.spent < -0.0001 || rbi.spent > -0.0001)
                                rbi.total = rbi.spent;

                        if(!rbi.groupedBudget &&
                            (rbi.spent.floatValue() < 0.00f && rbi.total.floatValue() > rbi.spent.floatValue()) ||
                            (rbi.spent.floatValue() > 0.00f && rbi.total.floatValue() < rbi.spent.floatValue()) )
                        {
                            if(txtNotes!=null)
                            {
                                String lLine=
                                        "Gone over budget on " + rbi.budgetItemName + ", " +
                                        "Orig " + String.format(Locale.ENGLISH, "£%.2f", rbi.total) +
                                        ", New " + String.format(Locale.ENGLISH, "£%.2f", rbi.spent);
                                String lCurrText = txtNotes.getText().toString();

                                if(!lCurrText.contains(lLine))
                                  txtNotes.setText(txtNotes.getText() + "\n" + lLine);
                            }
                            rbi.total = rbi.spent;
                        }
                        rbi.outstanding = rbi.total - rbi.spent;

                        rbi.RecCount = 0;
                        rbg.budgetItems.add(rbi);
                    }
                    else
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

                            rbi.groupedBudget = rbg.groupedBudget;
                            rbi.budgetItemName = scl.get(j).SubCategoryName;
                            rbi.SubCategoryId = scl.get(j).SubCategoryId;
                            rbi.total = rb2.Amount;
                            rbi.spent = rb2.Amount;
                            rbi.outstanding = rbi.total - rb2.Amount;

                            rbi.RecCount = 0;
                            rbg.budgetItems.add(rbi);
                        }
                    }
            
                }
                if (rbg.budgetItems.size() > 0)
                    localList.add(rbg);
            }
            mainGroup.total=0.00f;
            mainGroup.spent=0.00f;
            mainGroup.outstanding=0.00f;
            for(int i=0;i<localList.size();i++)
            {
                rbg=localList.get(i);
                if(rbg.groupedBudget==false)
                {
                    rbg.total=0.00f;
                    rbg.spent=0.00f;
                    rbg.outstanding=0.00f;
                    for (int j = 0; j < rbg.budgetItems.size(); j++)
                    {
                        RecordBudgetItem rbi = rbg.budgetItems.get(j);
                        rbg.total += rbi.total;
                        rbg.spent += rbi.spent;
                        rbg.outstanding += rbi.outstanding;
                    }
                }
                else
                {
                    rbg.total=0.00f;
                    rbg.spent=0.00f;
                    rbg.outstanding=0.00f;
                    for (int j = 0; j < rbg.budgetItems.size(); j++)
                    {
                        RecordBudgetItem rbi = rbg.budgetItems.get(j);
                        rbg.spent += rbi.spent;
                    }
                    RecordCategoryBudget rcb = MyDatabase.MyDB().tableCategoryBudget.getCategoryBudget(
                            rbg.CategoryId, pMonth, pYear);
                    rbg.total = rcb.BudgetAmount;
                    if(     (rbg.spent < 0.00f && rbg.total > rbg.spent) ||
                            (rbg.spent > 0.00f && rbg.total < rbg.spent) )
                    {
                        if(txtNotes!=null)
                        {
                            String lLine=
                                    "Gone over budget on " + rbg.budgetGroupName + ", " +
                                            "Orig " + String.format(Locale.ENGLISH, "£%.2f", rbg.total) +
                                            ", New " + String.format(Locale.ENGLISH, "£%.2f", rbg.spent);
                            String lCurrText = txtNotes.getText().toString();

                            if(!lCurrText.contains(lLine))
                                txtNotes.setText(txtNotes.getText() + "\n" + lLine);
                        }
                        rbg.total = rbg.spent;
                    }
                    rbg.outstanding = rbg.total - rbg.spent;
                }
                mainGroup.total+=rbg.total;
                mainGroup.spent+=rbg.spent;
                mainGroup.outstanding+=rbg.outstanding;
            }
            for(int i=0;i<localList.size();i++)
            {
                rbg = localList.get(i);
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

            for(int i=0;i<rbspent.size();i++)
            {
                for(int j=0; j<rb.size();j++)
                {
                    if(rbspent.get(i).SubCategoryId==rb.get(j).SubCategoryId)
                    {
                        rbspent.get(i).AutoMatchTransaction=rb.get(j).AutoMatchTransaction;
                    }
                }
            }

            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_monthly_expenses);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(pMonth, pYear, cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTMonthlyExpense);
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_monthly_income);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(pMonth, pYear, cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTMonthlyIncome);
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_extra_expenses);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(pMonth, pYear, cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTExtraExpense);
    
            mrbg = new RecordBudgetGroup();
            mrbg.budgetGroupName = MainActivity.context.getString(R.string.budget_header_extra_income);
            mrbg.divider = true;
            lList.add(mrbg);
            ProcessGroup(pMonth, pYear, cl, rb, rbspent, mrbg, lList, RecordSubCategory.mSCTExtraIncome);
    
            return (lList);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getbudget", e.getMessage());
        }
        
        return(null);
    }
    //endregion
 
     //region TableCommon Functions
    public void addCommonTransaction(RecordCommon rt)
    {
        tableCommon.addTransaction(rt);
    }
    public void deleteCommonTransaction(RecordCommon rt)
    {
        tableCommon.deleteTransaction(rt);
    }

    public void updateCommonTransaction(RecordCommon rt)
    {
        tableCommon.updateTransaction(rt);
    }

    public ArrayList<RecordCommon> getCommonTransactionList()
    {
        try
        {
            ArrayList<RecordCommon> rta = tableCommon.getTransactionList();
            for (int i = 0; i < rta.size(); i++)
            {
                RecordSubCategory sc = tableSubCategory.getSubCategory(rta.get(i).CategoryId);
                if (sc != null)
                    rta.get(i).SubCategoryName = sc.SubCategoryName;
            }
            return (rta);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase.getCommonTransactionList", e.getMessage());
        }
        return(null);
    }

    public RecordCommon getSingleCommonTransaction(Integer pTxSeqNo)
    {
        try
        {
            RecordCommon rt = tableCommon.getSingleCommonTransaction(pTxSeqNo);
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
            ErrorDialog.Show("Error in MyDatabase.getSingleCommonTransaction", e.getMessage());
        }
        return(null);
    }

    public RecordCommon getSingleCommonTransaction(String pTxDescription)
    {
        try
        {
            RecordCommon rt = tableCommon.getSingleCommonTransaction(pTxDescription);
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
            ErrorDialog.Show("Error in MyDatabase.getSingleCommonTransaction", e.getMessage());
        }
        return(null);
    }
    //endregion
    
   
    
 }

 