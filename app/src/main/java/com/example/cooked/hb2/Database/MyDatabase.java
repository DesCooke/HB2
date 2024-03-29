package com.example.cooked.hb2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.Records.RecordBudgetClass;
import com.example.cooked.hb2.Records.RecordBudgetGroup;
import com.example.cooked.hb2.Records.RecordBudgetItem;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;
import com.example.cooked.hb2.Records.RecordTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.cooked.hb2.Database.RecordPlanned.mPTYearly;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.DateFromComponents;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static java.lang.Math.abs;

// derived from SQLiteOpenHelper because it give lots of features like - upgrade/downgrade
// automatically handles the creation and recreation of the database
public class MyDatabase extends SQLiteOpenHelper
{
    //region Member Variables
    // these regions help to compartmentalise the unit

    // The version - each change - increment by one
    // if the version increases onUpgrade is called - if decreases - onDowngrade is called
    // if current is 0 (does not exist) onCreate is called
    private static final int DATABASE_VERSION = 26;
    private static MyDatabase myDB;
    private TableTransaction tableTransaction;
    private TableCategory tableCategory;
    private TableCategoryBudget tableCategoryBudget;
    private TableSubCategory tableSubCategory;
    private TablePlanned tablePlanned;
    private TableCommon tableCommon;
    private TableAccount tableAccount;
    private TablePlannedVariation tablePlannedVariation;
    private TableHelpPage tableHelpPage;
    private TableHelpParagraph tableHelpParagraph;
    public String Notes;
    public TextView txtNotes;
    public boolean Dirty = true;
    public RecordBudgetMonth rbm;
    //endregion

    //region statics
    public static MyDatabase MyDB()
    {
        if (myDB == null)
            myDB = new MyDatabase(MainActivity.context);
        return (myDB);
    }
    //endregion

    //region Database functions
    private MyDatabase(Context context)
    {
        // super has to be the first command - can't put anything before it
        super(context, context.getResources().getString(R.string.database_filename), null, DATABASE_VERSION);
        tableTransaction = new TableTransaction(this);
        tableCategory = new TableCategory(this);
        tableCategoryBudget = new TableCategoryBudget(this);
        tableSubCategory = new TableSubCategory(this);
        tablePlanned = new TablePlanned(this);
        tableCommon = new TableCommon(this);
        tableAccount = new TableAccount(this);
        tablePlannedVariation =  new TablePlannedVariation(this);
        tableHelpPage = new TableHelpPage(this);
        tableHelpParagraph = new TableHelpParagraph(this);
    }

    // called when the current database version is 0
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        tableTransaction.onCreate(db);
        tableCategory.onCreate(db);
        tableCategoryBudget.onCreate(db);
        tableSubCategory.onCreate(db);
        tablePlanned.onCreate(db);
        tableCommon.onCreate(db);
        tableAccount.onCreate(db);
        tablePlannedVariation.onCreate(db);
        tableHelpPage.onCreate(db);
        tableHelpParagraph.onCreate(db);
    }

    private void addToNotes(String comment)
    {
        if (txtNotes != null)
        {
            if (txtNotes.getText().length() == 0)
            {
                txtNotes.setText(comment);
            } else
            {
                txtNotes.setText(txtNotes.getText() + "\n" + comment);
            }
        }

        if (Notes.length() == 0)
        {
            Notes = comment;
        } else
        {
            Notes = Notes + "\n" + comment;
        }
    }

    // called when the version number increases
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        MyLog.WriteLogMessage("Upgrading database from " + Integer.toString(oldVersion) + " to " +
                Integer.toString(newVersion));

        tableTransaction.onUpgrade(db, oldVersion, newVersion);
        tableCategory.onUpgrade(db, oldVersion, newVersion);
        tableCategoryBudget.onUpgrade(db, oldVersion, newVersion);
        tableSubCategory.onUpgrade(db, oldVersion, newVersion);
        tablePlanned.onUpgrade(db, oldVersion, newVersion);
        tableCommon.onUpgrade(db, oldVersion, newVersion);
        tableAccount.onUpgrade(db, oldVersion, newVersion);
        tablePlannedVariation.onUpgrade(db, oldVersion, newVersion);
        tableHelpPage.onUpgrade(db, oldVersion, newVersion);
        tableHelpParagraph.onUpgrade(db, oldVersion, newVersion);
    }

    // called when the version number decreases
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        tableTransaction.onDowngrade(db, oldVersion, newVersion);
        tableCategory.onDowngrade(db, oldVersion, newVersion);
        tableCategoryBudget.onDowngrade(db, oldVersion, newVersion);
        tableSubCategory.onDowngrade(db, oldVersion, newVersion);
        tablePlanned.onDowngrade(db, oldVersion, newVersion);
        tableCommon.onDowngrade(db, oldVersion, newVersion);
        tableAccount.onDowngrade(db, oldVersion, newVersion);
        tablePlannedVariation.onDowngrade(db, oldVersion, newVersion);
        tableHelpPage.onDowngrade(db, oldVersion, newVersion);
        tableHelpParagraph.onDowngrade(db, oldVersion, newVersion);
    }
    //endregion

    //region Dump database
    public void dumpDatabase()
    {
        tableAccount.dump();
        tableTransaction.dumpTransactionTable();
    }

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
        return (tableTransaction.getCategoryBudgetSameMonthLastYear(pBudgetYear, pBudgetMonth, pCategoryId));
    }

    public Float getCategoryBudgetLastMonth(Integer pBudgetYear, Integer pBudgetMonth, Integer pCategoryId)
    {
        return (tableTransaction.getCategoryBudgetLastMonth(pBudgetYear, pBudgetMonth, pCategoryId));
    }

    public Float getCategoryBudgetAverage(Integer pCategoryId)
    {
        return (tableTransaction.getCategoryBudgetAverage(pCategoryId));
    }

    public RecordTransaction getLatestTransaction(String sortCode, String accountNum)
    {
        return tableTransaction.getLatestTransaction(sortCode, accountNum);
    }

    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum, boolean showPlanned)
    {
        ArrayList<RecordTransaction> rta = tableTransaction.getTransactionList(sortCode, accountNum);

        if (sortCode.compareTo("11-03-95") == 0)
        {
            if (showPlanned)
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
            Float lCurrentBalance = 0.00f;
            for (int i = 0; i < rta.size(); i++)
            {
                rta.get(i).BalanceCorrect = true;
                if (i > 0)
                {
                    Float lDiff = lCurrentBalance - rta.get(i).TxBalance;
                    if (lDiff < -0.005 || lDiff > 0.005)
                    {
                        rta.get(i).BalanceCorrect = false;
                        rta.get(i).TxBalanceShouldBe = lCurrentBalance;
                    }
                    lCurrentBalance = lCurrentBalance - rta.get(i).TxAmount;
                } else
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

    public ArrayList<RecordTransaction> getTransactionList(String sortCode, String accountNum,
                                                           boolean showPlanned, Integer budgetMonth,
                                                           Integer budgetYear, Boolean includeThisBudgetOnly)
    {
        ArrayList<RecordTransaction> rta = tableTransaction.getTransactionList(sortCode,
                accountNum, budgetMonth, budgetYear, includeThisBudgetOnly);

        if (sortCode.compareTo("11-03-95") == 0)
        {
            if (showPlanned)
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
            for (int i = 0; i < rta.size(); i++)
            {
                RecordSubCategory sc = tableSubCategory.getSubCategory(rta.get(i).CategoryId);
                if (sc != null)
                    rta.get(i).SubCategoryName = sc.SubCategoryName;
            }
        }
        return (rta);
    }


    public ArrayList<RecordTransaction> getCategoryBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth, Integer pCatgegoryId)
    {
        ArrayList<RecordTransaction> rta = tableTransaction.getCategoryBudgetTrans(pBudgetYear, pBudgetMonth, pCatgegoryId);
        Float lTotal = 0.00f;
        int lRecTotal = 0;
        if (rta != null)
        {
            for (int i = 0; i < rta.size(); i++)
            {
                lTotal += rta.get(i).TxAmount;
            }
        }

        return (rta);

    }

    public ArrayList<RecordTransaction> getBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth,
                                                       Integer pCategoryId, Integer pSubCatgegoryId)
    {
        // Called by activityCategoryItem - SubCategoryByMonth list

        // First get the list of planned transactions for this budget period
        // there could be multiple (ie. weekly planned item)
        ArrayList<RecordTransaction> rpl;
        if(pCategoryId == 0)
        {
            rpl=tablePlanned.getPlannedTransForSubCategoryId(pBudgetMonth, pBudgetYear, pSubCatgegoryId);
        }
        else
        {
            rpl=tablePlanned.getPlannedTransForCategoryId(pBudgetMonth, pBudgetYear, pCategoryId);
        }

        // Next get all the appropriate transactions - ie. what we've spent
        // normally negative - as an expense is negative
        ArrayList<RecordTransaction> rta = tableTransaction.getBudgetTrans(pBudgetYear, pBudgetMonth, pCategoryId, pSubCatgegoryId);
        Float lTotalSpent = 0.00f;
        if (rta != null)
            for (int i = 0; i < rta.size(); i++)
                lTotalSpent += rta.get(i).TxAmount;


        // Now go through each planned item - and change the TxAmount (budget amount)
        // by the amount we have already spent - so the TxAmount left is the budget not yet spent
        Float lTotalLeftToSpend = lTotalSpent;
        if(rpl != null)
        {
            for (int i = 0; i < rpl.size(); i++)
            {
                if(lTotalLeftToSpend < 0.00f)
                {
                    if(lTotalLeftToSpend < rpl.get(i).TxAmount)
                    {
                        lTotalLeftToSpend -= rpl.get(i).TxAmount;
                        rpl.get(i).TxAmount = 0.00f;
                    }
                    else
                    {
                        rpl.get(i).TxAmount -= lTotalLeftToSpend;
                        break;
                    }
                }
            }
        }

        // now - add in all the planned items which still have some budget left
        for (int i = 0; i < rpl.size(); i++)
            if(rpl.get(i).TxDescription.compareTo("Dummy")!=0)
                rta.add(rpl.get(i));

        // sort by date - so the planned items can be sorted with the transactions
        Collections.sort(rta, new Comparator<RecordTransaction>()
        {
            public int compare(RecordTransaction o1, RecordTransaction o2)
            {
                return o2.TxDate.compareTo(o1.TxDate);
            }
        });

        return (rta);

    }

    public ArrayList<RecordTransaction> getAnnualBudgetTrans(Integer pBudgetYear, Integer pBudgetMonth,
                                                       Integer pCategoryId, Integer pSubCategoryId,
                                                             TextView textView)
    {
        ArrayList<RecordTransaction> returnList = new ArrayList<>();

        Date selectedBudgetStartDate = DateUtils.BudgetStart(pBudgetMonth, pBudgetYear);
        Integer lBudgetDay = DateUtils.dateUtils().GetDayAsInt(selectedBudgetStartDate);
        Integer lBudgetMonth = DateUtils.dateUtils().GetMonthAsInt(selectedBudgetStartDate);
        Integer lBudgetYear = DateUtils.dateUtils().GetYearAsInt(selectedBudgetStartDate);

        // Get the planned transaction - usuallly just one record, this will contain
        // the date and time of the planned transaction effective for the budget year/month
        ArrayList<RecordPlanned> rpl;
        if(pCategoryId == 0)
        {
            rpl=tablePlanned.getPlannedListForSubCategory(pSubCategoryId);
        }
        else
        {
            rpl=tablePlanned.getPlannedListForCategory(pCategoryId);
        }
        if(rpl==null)
            return returnList;
        if(rpl.size()==0)
            return returnList;
        if(rpl.get(0).mPlannedType!=mPTYearly)
            return returnList;
        Integer lPlannedDay = rpl.get(0).mPlannedDay;
        Integer lPlannedMonth = rpl.get(0).mPlannedMonth;

        Integer lTxEndDay = lPlannedDay;
        Integer lTxEndMonth = lPlannedMonth;
        Integer lTxEndYear = lBudgetYear;

        if(lBudgetMonth>lPlannedMonth)
        {
            lTxEndYear++;
        }
        else
        {
            if(lBudgetMonth==lPlannedMonth)
            {
                if(lBudgetDay > lPlannedDay)
                    lTxEndYear++;
            }
        }
        Date lTxEndDate = DateFromComponents(lTxEndYear, lTxEndMonth, lTxEndDay);
        Date lTxStartDate = DateUtils.dateUtils().AddMonthsToDate(lTxEndDate, -12);
        lTxStartDate = DateUtils.dateUtils().AddDaysToDate(lTxStartDate, 1);

        textView.setText("For budget period " + DateUtils.DateToString(lTxStartDate) + " to " +
            DateUtils.DateToString(lTxEndDate));

        // Next get all the appropriate transactions - based on the annual tx start/end date
        // instead of current budget- ie. what we've spent
        // normally negative - as an expense is negative
        ArrayList<RecordTransaction> rta = tableTransaction.getAnnualBudgetTrans(lTxStartDate, lTxEndDate, pCategoryId, pSubCategoryId);
        Float lTotalSpent = 0.00f;
        if (rta != null)
            for (int i = 0; i < rta.size(); i++)
                lTotalSpent += rta.get(i).TxAmount;

        // Now go through each planned item - and change the TxAmount (budget amount)
        // by the amount we have already spent - so the TxAmount left is the budget not yet spent
        Float lTotalLeftToSpend = lTotalSpent;
        if(rpl != null)
        {
            for (int i = 0; i < rpl.size(); i++)
            {
                if(lTotalLeftToSpend < 0.00f)
                {
                    if(lTotalLeftToSpend < rpl.get(i).mMatchingTxAmount)
                    {
                        lTotalLeftToSpend -= rpl.get(i).mMatchingTxAmount;
                        rpl.get(i).mMatchingTxAmount = 0.00f;
                    }
                    else
                    {
                        rpl.get(i).mMatchingTxAmount -= lTotalLeftToSpend;
                        break;
                    }
                }
            }
        }

        // now - add in all the planned items which still have some budget left
        for (int i = 0; i < rpl.size(); i++)
            if(rpl.get(i).mPlannedName.compareTo("Dummy")!=0)
            {
                RecordPlanned rp = rpl.get(0);
                RecordTransaction rt = new RecordTransaction();
                rt.TxSeqNo = rp.mPlannedId;
                rt.TxAdded = new Date();
                rt.TxFilename = "Planned";
                rt.TxLineNo = 0;
                rt.TxDate = lTxEndDate;
                rt.TxType = "Planned";
                rt.TxSortCode = rp.mSortCode;
                rt.TxAccountNumber = rp.mAccountNo;
                rt.TxDescription = rp.mPlannedName;
                rt.TxAmount = rp.GetAmountAt(lTxEndDate);
                rt.TxBalance = 0.00f;
                rt.CategoryId = pSubCategoryId;
                rt.SubCategoryName =
                    MyDatabase.MyDB().getSubCategory(pSubCategoryId).SubCategoryName;
                rt.Comments = "planned";
                rt.BudgetYear = dateUtils().GetBudgetYear(lTxEndDate);
                rt.BudgetMonth = dateUtils().GetBudgetMonth(lTxEndDate);
                rt.HideBalance = true;

                rta.add(rt);
            }

        // sort by date - so the planned items can be sorted with the transactions
        Collections.sort(rta, new Comparator<RecordTransaction>()
        {
            public int compare(RecordTransaction o1, RecordTransaction o2)
            {
                return o2.TxDate.compareTo(o1.TxDate);
            }
        });

        return (rta);

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
        return (tableTransaction.getNextTxLineNo(pDate));
    }

    public RecordTransaction getSingleTransaction(Integer pTxSeqNo)
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
    //endregion

    //region Account Functions
    public void addAccount(RecordAccount ra)
    {
        tableAccount.addAccount(ra);
    }

    public void deleteAccount(RecordAccount ra)
    {
        tableAccount.deleteAccount(ra);
    }

    public void accountResequence(ArrayList<RecordAccount> raa)
    {
        tableAccount.accountResequence(raa);
    }

    public void unhideAllAccounts()
    {
        tableAccount.unhideAllAccounts();

    }

    public void updateAccount(RecordAccount ra)
    {
        tableAccount.updateAccount(ra);
    }

    public ArrayList<RecordAccount> getAccountList()
    {
        return (tableAccount.getAccountList());
    }

    public RecordAccount getAccountItem(int pAcSeqNo)
    {
        return (tableAccount.getSingleAccount(pAcSeqNo));
    }

    public RecordAccount getAccountItemByAccountNumber(String pSortCode, String pAccountNum)
    {
        return (tableAccount.getAccountItemByAccountNum(pSortCode, pAccountNum));
    }

    //endregion

    //region Category functions
    public void addCategory(RecordCategory rc)
    {
        tableCategory.addCategory(rc);
    }

    public void updateCategory(RecordCategory rc)
    {
        tableCategory.updateCategory(rc);
    }

    public void deleteCategory(RecordCategory rc)
    {
        ArrayList<RecordSubCategory> rscList;
        rscList = getSubCategoryList(rc.CategoryId);
        for (int i = 0; i < rscList.size(); i++)
        {
            tableSubCategory.deleteSubCategory(rscList.get(i));
        }
        tableCategoryBudget.deleteAllForCategory(rc.CategoryId);

        tableCategory.deleteCategory(rc);
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
    public void addCategoryBudget(RecordCategoryBudget rcb)
    {
        tableCategoryBudget.addCategoryBudget(rcb);
    }

    public void deleteAllCategoryBudgets()
    {
        tableCategoryBudget.deleteAll();
    }

    public void updateCategoryBudget(RecordCategoryBudget rcb)
    {
        tableCategoryBudget.updateCategoryBudget(rcb);
    }

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
    public void addSubCategory(RecordSubCategory rc)
    {
        tableSubCategory.addSubCategory(rc);
    }

    public void updateSubCategory(RecordSubCategory rc)
    {
        tableSubCategory.updateSubCategory(rc);
    }

    public void deleteSubCategory(RecordSubCategory rc)
    {
        tableSubCategory.deleteSubCategory(rc);
    }

    public void changeSubCategory(int oldSubCategoryId, int newSubCategoryId)
    {
        tableSubCategory.changeSubCategory(oldSubCategoryId, newSubCategoryId);
    }
    public RecordSubCategory getSubCategory(Integer pSubCategoryId)
    {
        return (tableSubCategory.getSubCategory(pSubCategoryId));
    }

    public ArrayList<RecordSubCategory> getSubCategoryList(Integer pCategoryId)
    {
        return tableSubCategory.getSubCategoryList(pCategoryId);
    }

    public ArrayList<RecordSubCategory> getSubCategoryListWithOld(Integer pCategoryId, Boolean pOld)
    {
        return tableSubCategory.getSubCategoryListWithOld(pCategoryId, pOld);
    }

    public List<RecordSubCategoryByMonth> getSubCategoryTotalByMonth(Integer subCategoryId)
    {
        return tableTransaction.getSubCategoryTotalByMonth(subCategoryId);
    }

    public List<RecordSubCategoryByMonth> getCategoryTotalByMonth(Integer subCategoryId)
    {
        return tableTransaction.getCategoryTotalByMonth(subCategoryId);
    }
    //endregion

    //region Planned Functions
    public void addPlanned(RecordPlanned rp)
    {
        tablePlanned.addPlanned(rp);
    }

    public void deletePlanned(RecordPlanned rp)
    {
        tablePlannedVariation.deleteVariationForPlannedId(rp.mPlannedId);
        tablePlanned.deletePlanned(rp);
    }

    public int createPlanned(int pTxSeqNo)
    {
        RecordTransaction rt = tableTransaction.getSingleTransaction(pTxSeqNo);
        if (rt == null)
            return (0);

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
        rp.mEndDate = dateUtils().StrToDate(MainActivity.context.getString(R.string.date_unknown_string));

        rp.mMatchingTxType = rt.TxType;
        rp.mMatchingTxDescription = rt.TxDescription;
        rp.mMatchingTxAmount = rt.TxAmount;

        rp.mPlanned = "";
        rp.mSubCategoryName = rt.SubCategoryName;

        tablePlanned.addPlanned(rp);

        return (rp.mPlannedId);
    }

    public void updatePlanned(RecordPlanned rp)
    {
        tablePlanned.updatePlanned(rp);
    }


    public ArrayList<RecordPlanned> GetAnnualPlannedList()
    {
        ArrayList<RecordPlanned> rpa = tablePlanned.GetAnnualPlannedList();
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

    public ArrayList<RecordPlanned> getPlannedList(boolean activeOnly)
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

    public ArrayList<RecordPlanned> getPlannedListForSubCategory(int pSubCategoryId)
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

    public RecordPlanned getSinglePlanned(Integer pPlannedId)
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

    public RecordPlanned GetAnnualSavingsPlannedItem()
    {
        return(tablePlanned.GetAnnualSavingsPlannedItem());
    }

    public RecordTransaction GetLastTransactionForPlannedItem(int pSubCategoryItem)
    {
        return(tablePlanned.getLastTransactionForPlannedItem(pSubCategoryItem));
    }

    private void ProcessGroup(Integer pMonth, Integer pYear,
                              ArrayList<RecordCategory> cl, ArrayList<RecordBudget> rb,
                              ArrayList<RecordBudget> rbspent, RecordBudgetClass rbc,
                              ArrayList<RecordBudgetGroup> lList, Integer pCategoryType)
    {
        RecordBudgetGroup rbg;

        ArrayList<RecordBudgetGroup> localList = new ArrayList<RecordBudgetGroup>();

        for (int i = 0; i < cl.size(); i++)
        {
            rbg = new RecordBudgetGroup();
            rbg.BudgetClassId = rbc.BudgetClassId;
            rbg.BudgetGroupId = rbc.budgetGroups.size() + 1;
            rbg.BudgetMonth = pMonth;
            rbg.BudgetYear = pYear;
            rbg.budgetGroupName = cl.get(i).CategoryName;
            rbg.CategoryId = cl.get(i).CategoryId;
            rbg.Monitor = cl.get(i).Monitor;
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
                    rbi.BudgetClassId = rbc.BudgetClassId;
                    rbi.BudgetGroupId = rbg.BudgetGroupId;
                    rbi.BudgetItemId = rbg.budgetItems.size() + 1;
                    rbi.groupedBudget = rbg.groupedBudget;
                    rbi.spent = 0.00f;
                    rbi.SubCategoryName = scl.get(j).SubCategoryName;
                    rbi.CategoryName = cl.get(i).CategoryName;

                    for (int l = 0; l < rbspent.size(); l++)
                    {
                        if (rbspent.get(l).SubCategoryId == rb2.SubCategoryId)
                        {
                            /* yes - update spent totals */
                            rbi.spent = rbspent.get(l).Amount;
                            rbg.spent += rbi.spent;
                            rbc.spent += rbi.spent;
                            break;
                        }
                    }

                    //
                    // Work out sub-category name and whether we need to add a
                    // frequency in brackets at the end
                    //
                    String lExtraName="";
                    if(rb2.mPlannedId>-1 || rb2.AnnualBudget)
                    {
                        RecordPlanned localrp=MyDB().getSinglePlanned(rb2.mPlannedId);
                        if(localrp.mPlannedType==RecordPlanned.mPTMonthly && localrp.mFrequencyMultiplier==1)
                        {
                            rbi.budgetItemName = scl.get(j).SubCategoryName;
                        }
                        else
                        {
                            lExtraName = DateUtils.dateUtils().PlannedTypeDescription(localrp, DateUtils.BudgetStart(pMonth, pYear));
                            rbi.budgetItemName = scl.get(j).SubCategoryName + " (" + lExtraName + ")";
                        }
                    }
                    else
                    {
                        rbi.budgetItemName = scl.get(j).SubCategoryName;
                    }


                    rbi.SubCategoryId = scl.get(j).SubCategoryId;
                    rbi.Monitor = scl.get(j).Monitor;
                    rbi.total = rb2.Amount;
                    rbi.origTotal = rbi.total;

                    if(
                        (rb2.AnnualBudget && !rb2.DueThisMonth && rbi.spent < -0.0001f) ||
                            (rb2.AnnualBudget && rb2.DueThisMonth ) ||
                            (!rb2.AnnualBudget))
                    {
                        //
                        // Handle overbudget
                        // don't mention if the budget or budget group are already being monitored
                        //
                        if (!rbi.groupedBudget && !rbi.Monitor && !rbg.Monitor &&
                            (rbi.spent.floatValue() < 0.00f && rbi.total.floatValue() > rbi.spent.floatValue()) ||
                            (rbi.spent.floatValue() > 0.00f && rbi.total.floatValue() < rbi.spent.floatValue()))
                        {
                            String lLine =
                                "Over budget on " + rbi.budgetItemName + ", " +
                                    "Orig " + Tools.moneyFormat(rbi.total) +
                                    ", New " + Tools.moneyFormat(rbi.spent);
                            addToNotes(lLine);

                            rbi.total = rbi.spent;
                        }

                        //
                        // Handle underbudget
                        // don't mention if the budget or budget group are already being monitored
                        //
                        if (!rbi.groupedBudget && !rbi.Monitor && !rbg.Monitor &&
                            (rbi.spent.floatValue() < 0.00f && rbi.total.floatValue() < rbi.spent.floatValue()) ||
                            (rbi.spent.floatValue() > 0.00f && rbi.total.floatValue() > rbi.spent.floatValue()))
                        {
                            if(!rb2.AnnualBudget)
                            {
                                String lLine =
                                    "Under budget on " + rbi.budgetItemName + ", " +
                                        "Orig " + Tools.moneyFormat(rbi.total) +
                                        ", New " + Tools.moneyFormat(rbi.spent);
                                addToNotes(lLine);

                                rbi.total = rbi.spent;
                            }
                        }

                        //
                        // If auto match transaction then replace the budget amount
                        // with the transaction amount always
                        // we still want to know about it - hence this is placed after
                        // the over budget bit
                        //
                        if (rb2.AutoMatchTransaction)
                        {
                            if (rbi.spent < -0.0001 || rbi.spent > 0.0001)
                            {
                                rbi.total = rbi.spent;
                                rbi.origTotal = rbi.total;
                            }
                        }


                        rbi.outstanding = rbi.total - rbi.spent;

                        rbi.RecCount = 0;
                        rbg.budgetItems.add(rbi);
                    }
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

                        rbi.BudgetClassId = rbc.BudgetClassId;
                        rbi.BudgetGroupId = rbg.BudgetGroupId;
                        rbi.BudgetItemId = rbg.budgetItems.size() + 1;
                        rbi.groupedBudget = rbg.groupedBudget;
                        rbi.budgetItemName = scl.get(j).SubCategoryName;
                        rbi.SubCategoryId = scl.get(j).SubCategoryId;
                        rbi.Monitor = scl.get(j).Monitor;
                        rbi.total = rb2.Amount;
                        rbi.origTotal = rbi.total;
                        rbi.spent = rb2.Amount;
                        rbi.outstanding = rbi.total - rb2.Amount;
                        rbi.SubCategoryName = scl.get(j).SubCategoryName;
                        rbi.CategoryName = cl.get(i).CategoryName;

                        rbi.RecCount = 0;

                        if(
                            (rb2.AnnualBudget && !rb2.DueThisMonth && (rbi.spent < -0.0001f || rbi.spent > 0.0001f)) ||
                                (rb2.AnnualBudget && rb2.DueThisMonth ) ||
                                (!rb2.AnnualBudget))
                        {
                            rbg.budgetItems.add(rbi);
                        }
                    }
                }

            }
            if (rbg.budgetItems.size() > 0)
                localList.add(rbg);
        }
        rbc.total = 0.00f;
        rbc.origTotal = rbc.total;
        rbc.spent = 0.00f;
        rbc.outstanding = 0.00f;
        for (int i = 0; i < localList.size(); i++)
        {
            rbg = localList.get(i);
            if (rbg.groupedBudget == false)
            {
                rbg.total = 0.00f;
                rbg.origTotal = 0.00f;
                rbg.spent = 0.00f;
                rbg.outstanding = 0.00f;
                for (int j = 0; j < rbg.budgetItems.size(); j++)
                {
                    RecordBudgetItem rbi = rbg.budgetItems.get(j);
                    rbg.total += rbi.total;
                    rbg.spent += rbi.spent;
                    rbg.outstanding += rbi.outstanding;
                }
            } else
            {
                rbg.total = 0.00f;
                rbg.origTotal = 0.00f;
                rbg.spent = 0.00f;
                rbg.outstanding = 0.00f;
                for (int j = 0; j < rbg.budgetItems.size(); j++)
                {
                    RecordBudgetItem rbi = rbg.budgetItems.get(j);
                    rbg.spent += rbi.spent;
                }
                RecordCategoryBudget rcb = MyDatabase.MyDB().tableCategoryBudget.getCategoryBudget(
                        rbg.CategoryId, pMonth, pYear);
                rbg.total = rcb.BudgetAmount;
                rbg.origTotal = rbg.total;


                if (  ((rbg.spent < 0.00f && rbg.total > rbg.spent) ||
                        (rbg.spent > 0.00f && rbg.total < rbg.spent)))
                {
                    if (!rbg.Monitor) {
                        String lLine =
                                "Gone over budget on " + rbg.budgetGroupName + ", " +
                                        "Orig " + Tools.moneyFormat(rbg.total) +
                                        ", New " + Tools.moneyFormat(rbg.spent);
                        addToNotes(lLine);
                    }
                    rbg.total = rbg.spent;
                }
                rbg.outstanding = rbg.total - rbg.spent;
            }
            rbc.total += rbg.total;
            rbc.spent += rbg.spent;
            rbc.outstanding += rbg.outstanding;
        }
        for (int i = 0; i < localList.size(); i++)
        {
            rbg = localList.get(i);
            lList.add(rbg);
        }
    }

    public RecordBudgetMonth getDatasetBudgetMonth(Integer pMonth, Integer pYear, boolean pIncludeThisBudgetOnly)
    {
        if (!Dirty)
            return (rbm);

        Notes = "";
        rbm = new RecordBudgetMonth();

        rbm.budgetMonth = pMonth;
        rbm.budgetYear = pYear;

        rbm.mCommonDataset = MyDatabase.MyDB().getCommonTransactionList();

        rbm.mAnnualBills = tableTransaction.GetAnnualBills(pYear);

        rbm.accounts = tableAccount.getAccountList();
        for (int i = 0; i < rbm.accounts.size(); i++)
        {
            RecordAccount ra = rbm.accounts.get(i);
            ra.RecordTransactions = tableTransaction.getTransactionList(ra.AcSortCode,
                    ra.AcAccountNumber, pMonth, pYear, pIncludeThisBudgetOnly);
            if(ra.AcDescription.compareTo("General Savings")==0)
            {
                if(ra.RecordTransactions.size()>0)
                {
                    RecordTransaction rt = ra.RecordTransactions.get(0);
                    if(rt.MarkerEndingBalance != null)
                    {
                        if(rt.MarkerEndingBalance < 1000.00)
                        {
                            addToNotes("General Savings shortfall.  Balance " + Tools.moneyFormat(rt.MarkerEndingBalance) + ", " +
                                "Ideal £1000.00, Shortfall " + Tools.moneyFormat(1000 - rt.MarkerEndingBalance));
                        }
                    }
                }
            }
            if(ra.AcDescription.compareTo("Emergency Savings")==0)
            {
                if(ra.RecordTransactions.size()>0)
                {
                    RecordTransaction rt = ra.RecordTransactions.get(0);
                    if(rt.MarkerEndingBalance != null)
                    {
                        if(rt.MarkerEndingBalance < 4000.00)
                            addToNotes("Emergency Savings shortfall.  Balance " + Tools.moneyFormat(rt.MarkerEndingBalance) + ", " +
                                "Ideal £4000.00, Shortfall " + Tools.moneyFormat(4000 - rt.MarkerEndingBalance));
                    }
                }
            }
        }


        ArrayList<RecordBudget> rb = tablePlanned.getBudgetList(pMonth, pYear);
        ArrayList<RecordBudget> rbspent = tablePlanned.getBudgetSpent(pMonth, pYear);
        ArrayList<RecordCategory> cl = tableCategory.getCategoryList();

        for (int i = 0; i < rbspent.size(); i++)
        {
            for (int j = 0; j < rb.size(); j++)
            {
                if (rbspent.get(i).SubCategoryId == rb.get(j).SubCategoryId)
                {
                    rbspent.get(i).AutoMatchTransaction = rb.get(j).AutoMatchTransaction;
                }
            }
        }

        rbm.budgetClasses.clear();

        RecordBudgetClass rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_monthly_expenses);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTMonthlyExpense);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_monthly_income);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTMonthlyIncome);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_annual_expenses);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTAnnualExpense);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_annual_income);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTAnnualIncome);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_extra_expenses);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTExtraExpense);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_extra_income);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTExtraIncome);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_unplanned_expenses);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTUnplannedExpense);

        rbc = new RecordBudgetClass();
        rbc.BudgetClassId = rbm.budgetClasses.size() + 1;
        rbc.budgetClassName = MainActivity.context.getString(R.string.budget_header_unplanned_income);
        rbm.budgetClasses.add(rbc);
        rbc.budgetGroups = new ArrayList<>();
        ProcessGroup(pMonth, pYear, cl, rb, rbspent, rbc, rbc.budgetGroups, RecordSubCategory.mSCTUnplannedIncome);

        Dirty = false;

        rbm.RefreshTotals();

        RecordAccount ra = rbm.FindAccount("11-03-95", "00038840");
        if (ra != null)
        {
            if (ra.RecordTransactions.size() > 0)
            {
                RecordTransaction startTransaction = ra.RecordTransactions.get(ra.RecordTransactions.size() - 1);

                rbm.startingBalance = startTransaction.MarkerStartingBalance;

                rbm.finalBudgetBalanceThisMonth =
                        rbm.startingBalance +
                                (rbm.monthlyIncome - rbm.monthlyExpense) +
                                (rbm.extraIncome - rbm.extraExpense);

                rbm.notes = Notes;
            }
        }

        return (rbm);

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
        ArrayList<RecordCommon> rta = tableCommon.getTransactionList();
        return (rta);
    }

    public RecordCommon getSingleCommonTransaction(Integer pTxSeqNo)
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

    public RecordCommon getSingleCommonTransaction(String pTxDescription)
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
    //endregion

    //region HelpPage Functions
    public void addHelpPage(RecordHelpPage hp)
    {
        tableHelpPage.addHelpPage(hp);
    }

    public void deleteHelpPage(RecordHelpPage hp)
    {
        tableHelpPage.deleteHelpPage(hp);
    }

    public void updateHelpPage(RecordHelpPage hp)
    {
        tableHelpPage.updateHelpPage(hp);
    }


    //endregion

    //region HelpParagraph Functions
    public void addHelpParagraph(RecordHelpParagraph hp)
    {
        tableHelpParagraph.addHelpParagraph(hp);
    }

    public void deleteHelpParagraph(RecordHelpParagraph hp)
    {
        tableHelpParagraph.deleteHelpParagraph(hp);
    }

    public void updateHelpParagraph(RecordHelpParagraph hp)
    {
        tableHelpParagraph.updateHelpParagraph(hp);
    }


    //endregion

    //region PlannedVariation Functions
    public void addVariation(RecordPlannedVariation rpv)
    {
        tablePlannedVariation.addVariation(rpv);
    }

    public void deleteVariation(RecordPlannedVariation rpv)
    {
        tablePlannedVariation.deleteVariation(rpv);
    }

    public void deleteVariationForPlannedId(int pPlannedId)
    {
        tablePlannedVariation.deleteVariationForPlannedId(pPlannedId);
    }

    public void updateVariation(RecordPlannedVariation rpv)
    {
        tablePlannedVariation.updateVariation(rpv);
    }

    public ArrayList<RecordPlannedVariation> getVariationList(int pPlannedId)
    {
        return(tablePlannedVariation.getVariationList(pPlannedId));
    }

    public RecordPlannedVariation getSingleVariation(Integer pVariationId)
    {
        return(tablePlannedVariation.getSingleVariation(pVariationId));
    }
    //endregion


}

 