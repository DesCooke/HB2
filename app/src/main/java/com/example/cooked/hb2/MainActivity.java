package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.cooked.hb2.Budget.BudgetAdapter;
import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordButton;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.DialogTransactionList;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyDownloads;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyPermission;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.TransactionUI.TransactionAdapter;

import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener
{
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    
    private final String KEY_RECYCLER_STATE_BUTTON = "recycler_state_button";
    private final String KEY_RECYCLER_STATE_CURRENT = "recycler_state_current";
    private final String KEY_RECYCLER_STATE_GENERAL = "recycler_state_general";
    private final String KEY_RECYCLER_STATE_LONGTERM = "recycler_state_longterm";
    private final String KEY_RECYCLER_STATE_FAMILY = "recycler_state_family";
    private final String KEY_RECYCLER_STATE_CASH = "recycler_state_cash";
    
    private RecyclerView mTransactionListButton;
    private RecyclerView mTransactionListCommonButton;
    private RecyclerView mTransactionListCurrent;
    private RecyclerView mTransactionListGeneral;
    private RecyclerView mTransactionListLongTerm;
    private RecyclerView mTransactionListFamily;
    private RecyclerView mTransactionListCash;
    private ExpandableListView budgetListView;

    private Switch swIncludeThisBudgetOnly;
    private ArrayList<RecordBudgetGroup> mDatasetBudget;
    private ArrayList<RecordButton> mDatasetButton;
    private ArrayList<RecordButton> mDatasetCommonButton;

    public TextView txtNotes;
    private Bundle mButtonViewState;
    private Bundle mCurrentViewState;
    private Bundle mGeneralViewState;
    private Bundle mLongTermViewState;
    private Bundle mFamilyViewState;
    private Bundle mCashViewState;
    
    private BudgetAdapter budgetAdapter;
    private ImageAdapter mTransactionAdapterButton;
    private ImageAdapter mTransactionAdapterCommonButton;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView txtBudgetTitle;
    private TextView txtBankAccountTitle;
    private TextView txtCashAccountTitle;


    private Integer mCurrentBudgetYear;
    private Integer mCurrentBudgetMonth;
    private Integer mCurrentBABudgetYear;
    private Integer mCurrentBABudgetMonth;
    private Integer mCurrentCABudgetYear;
    private Integer mCurrentCABudgetMonth;

    private TextView lblMonthlyExpense;
    private TextView lblMonthlyIncome;
    private TextView lblMonthlyTotal;
    private TextView lblExtraExpense;
    private TextView lblExtraIncome;
    private TextView lblExtraTotal;
    private TextView lblBudgetTotal;
    private TextView tvStartingBalance;
    private TextView tvMonthlyLeftOverLabel;
    private TextView tvExtraLeftOverLabel;

    public int mSelectedButton;
    
    private void setupStatics(Context lcontext)
    {
        context = lcontext;
        MyResources.setContext(context);
    }
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setupStatics(this);
        
        if (!MyPermission.checkIfAlreadyHavePermission(this))
            MyPermission.requestForSpecificPermission(this);
        try
        {
            MyLog.WriteLogMessage("Starting");
            
            if (MyDownloads.MyDL().CollectFiles() == FALSE)
                return;
            
            setupActivity();
            
            mCurrentBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
            mCurrentBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
            mCurrentBABudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
            mCurrentBABudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
            mCurrentCABudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
            mCurrentCABudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();

            setupBudget();
            
            setupTabs();
            
            setupNavigationSideBar();
            
            setupRecyclerViews();
            
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onCreate", e.getMessage());
        }
    }
    
    private void IncreaseBudgetPeriod(View view)
    {
        mCurrentBudgetMonth++;
        if (mCurrentBudgetMonth > 12)
        {
            mCurrentBudgetMonth = 1;
            mCurrentBudgetYear++;
        }
        setupBudget();
    }
    
    private void DecreaseBudgetPeriod(View view)
    {
        mCurrentBudgetMonth--;
        if (mCurrentBudgetMonth < 1)
        {
            mCurrentBudgetMonth = 12;
            mCurrentBudgetYear--;
        }
        setupBudget();
    }

    private void IncreaseBABudgetPeriod(View view)
    {
        mCurrentBABudgetMonth++;
        if (mCurrentBABudgetMonth > 12)
        {
            mCurrentBABudgetMonth = 1;
            mCurrentBABudgetYear++;
        }
        setupBudget();
        setupBankAccountView();
    }

    private void DecreaseBABudgetPeriod(View view)
    {
        mCurrentBABudgetMonth--;
        if (mCurrentBABudgetMonth < 1)
        {
            mCurrentBABudgetMonth = 12;
            mCurrentBABudgetYear--;
        }
        setupBudget();
        setupBankAccountView();
    }

    private void IncreaseCABudgetPeriod(View view)
    {
        mCurrentCABudgetMonth++;
        if (mCurrentCABudgetMonth > 12)
        {
            mCurrentCABudgetMonth = 1;
            mCurrentCABudgetYear++;
        }
        setupBudget();
        setupCashView();
    }

    private void ToggleIncludeThisBudgetOnly(View view)
    {
        setupBankAccountView();
    }

    private void DecreaseCABudgetPeriod(View view)
    {
        mCurrentCABudgetMonth--;
        if (mCurrentCABudgetMonth < 1)
        {
            mCurrentCABudgetMonth = 12;
            mCurrentCABudgetYear--;
        }
        setupBudget();
        setupCashView();
    }

    public void setupBudget()
    {
        try
        {
            txtNotes.setText("Notes");

            MyDatabase.MyDB().txtNotes = txtNotes;

            String lTitle = DateUtils.dateUtils().MonthAsText(mCurrentBudgetMonth) + " / " +
                mCurrentBudgetYear.toString();
            txtBudgetTitle.setText(lTitle);

            lTitle = DateUtils.dateUtils().MonthAsText(mCurrentBABudgetMonth) + " / " +
                    mCurrentBABudgetYear.toString();
            txtBankAccountTitle.setText(lTitle);

            lTitle = DateUtils.dateUtils().MonthAsText(mCurrentCABudgetMonth) + " / " +
                    mCurrentCABudgetYear.toString();
            txtCashAccountTitle.setText(lTitle);

            mDatasetBudget = MyDatabase.MyDB().getBudget(mCurrentBudgetMonth, mCurrentBudgetYear);

            Float lTotal = 0.00f;
            Float lSpent = 0.00f;
            Float lOutstanding = 0.00f;
            
            Float l_BCIncome = 0.00f;
            Float l_BCExpense = 0.00f;
            Float l_BCEIncome = 0.00f;
            Float l_BCEExpense = 0.00f;
            for (int i = 0; i < mDatasetBudget.size(); i++)
            {
                RecordBudgetGroup rbg = mDatasetBudget.get(i);
                rbg.lMainActivity=this;

                if (rbg.budgetGroupName.compareTo(getString(R.string.budget_header_monthly_income)) == 0)
                {
                    l_BCIncome = rbg.total;
                }
                if (rbg.budgetGroupName.compareTo(getString(R.string.budget_header_monthly_expenses)) == 0)
                {
                    lTotal += (rbg.total*-1);
                    lSpent += (rbg.spent*-1);
                    lOutstanding += (rbg.outstanding*-1);
                    l_BCExpense = rbg.total * -1;
                }
                if (rbg.budgetGroupName.compareTo(getString(R.string.budget_header_extra_income)) == 0)
                {
                    l_BCEIncome = rbg.total;
                }
                if (rbg.budgetGroupName.compareTo(getString(R.string.budget_header_extra_expenses)) == 0)
                {
                    lTotal += (rbg.total*-1);
                    lSpent += (rbg.spent*-1);
                    lOutstanding += (rbg.outstanding*-1);
                    l_BCEExpense = rbg.total * -1;
                }

                
            }

            lblMonthlyIncome.setText(String.format(Locale.ENGLISH, "£%.2f", l_BCIncome));
            lblMonthlyExpense.setText(String.format(Locale.ENGLISH, "£%.2f", l_BCExpense));
            lblMonthlyTotal.setText(String.format(Locale.ENGLISH, "£%.2f", abs(l_BCIncome-l_BCExpense)));
            if( (l_BCIncome-l_BCExpense) > 0.00f)
            {
                tvMonthlyLeftOverLabel.setText("Monthly Underspend by...");
            }
            else
            {
                if( (l_BCIncome-l_BCExpense) < 0.00f)
                {
                    tvMonthlyLeftOverLabel.setText("Monthly Overspend by...");
                }
                else
                {
                    {
                        tvMonthlyLeftOverLabel.setText("Monthly income matches expense");
                    }
                }
            }
            
            lblExtraIncome.setText(String.format(Locale.ENGLISH, "£%.2f", l_BCEIncome));
            lblExtraExpense.setText(String.format(Locale.ENGLISH, "£%.2f", l_BCEExpense));
            lblExtraTotal.setText(String.format(Locale.ENGLISH, "£%.2f", abs(l_BCEIncome - l_BCEExpense)));
            if( (l_BCEIncome-l_BCEExpense) > 0.00f)
            {
                tvExtraLeftOverLabel.setText("Extra Underspend by...");
            }
            else
            {
                if( (l_BCEIncome-l_BCEExpense) < 0.00f)
                {
                    tvExtraLeftOverLabel.setText("Extra Overspend by...");
                }
                else
                {
                    {
                        tvExtraLeftOverLabel.setText("Extra income matches expense");
                    }
                }
            }

            ArrayList<RecordTransaction> lData = MyDatabase.MyDB().getTransactionList("11-03-95", "00038840", false, mCurrentBABudgetMonth, mCurrentBABudgetYear, swIncludeThisBudgetOnly.isChecked());
            tvStartingBalance.setText("£0.00");
            Float lStartBalance=0.00f;
            if(lData.size()>0)
            {
                lStartBalance=Float.parseFloat(lData.get(lData.size()-1).TxDescription);
                tvStartingBalance.setText(String.format(Locale.ENGLISH, "£%.2f", lStartBalance));
            }

            lblBudgetTotal.setText(String.format(Locale.ENGLISH, "£%.2f",
                lStartBalance + (l_BCIncome-l_BCExpense) + (l_BCEIncome - l_BCEExpense)));
            
            //get reference of the ExpandableListView
            budgetListView = findViewById(R.id.budgetList);
            
            // create the adapter by passing your ArrayList data
            budgetAdapter = new BudgetAdapter(MainActivity.this, mDatasetBudget);
            
            if (budgetAdapter == null)
            {
                ErrorDialog.Show("budgetAdapter is null", "budgetAdapter is null");
            }
            
            if (budgetListView == null)
            {
                ErrorDialog.Show("budgetListView is null", "budgetListView is null");
            }
            
            // attach the adapter to the expandable list view
            budgetListView.setAdapter(budgetAdapter);
            
            //expand all the Groups
            collapseAll();

            // setOnChildClickListener listener for child row click
            budgetListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
            {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
                {
                    //get the group header
                    RecordBudgetGroup budgetGroupInfo = mDatasetBudget.get(groupPosition);
                    //get the child info
                    RecordBudgetItem budgetItemInfo = budgetGroupInfo.budgetItems.get(childPosition);
                    //display it or do something with it
                    DialogTransactionList dtl = new DialogTransactionList(MainActivity.this);
                    try
                    {
                        dtl.budgetYear = mCurrentBudgetYear;
                        dtl.budgetMonth = mCurrentBudgetMonth;
                        dtl.subCategoryId = budgetItemInfo.SubCategoryId;
                        dtl.GetTrans();
                        dtl.show();
                    }
                    catch (Exception e)
                    {
                        ErrorDialog.Show("Error in MainActivity::onChildClick", e.getMessage());
                    }
                    return false;
                }
            });
            // setOnGroupClickListener listener for group heading click
            budgetListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
            {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                {
                    //get the group header
                    RecordBudgetGroup budgetGroupInfo = mDatasetBudget.get(groupPosition);
                    //display it or do something with it
                    return false;
                }
            });


        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::setupBudget", e.getMessage());
        }
    }

    //method to expand all groups
    private void expandAll()
    {
        int count = budgetAdapter.getGroupCount();
        for (int i = 0; i < count; i++)
        {
            budgetListView.expandGroup(i);
        }
    }
    
    //method to collapse all groups
    private void collapseAll()
    {
        int count = budgetAdapter.getGroupCount();
        for (int i = 0; i < count; i++)
        {
            budgetListView.collapseGroup(i);
        }
    }
    
    private void ToggleShowPlanned(View view)
    {
        setupRecyclerViews();
    }
    
    private void setupActivity()
    {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        txtBudgetTitle = findViewById(R.id.txtBudgetTitle);
        txtBankAccountTitle = findViewById(R.id.txtBankAccountTitle);
        txtCashAccountTitle = findViewById(R.id.txtCashAccountTitle);
        txtNotes = findViewById(R.id.txtNotes);

        ImageButton imgLeft = findViewById(R.id.imgLeft);
        imgLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DecreaseBudgetPeriod(view);
            }
        });
    
        ImageButton imgRight = findViewById(R.id.imgRight);
        imgRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IncreaseBudgetPeriod(view);
            }
        });

        ImageButton imgLeftBA = findViewById(R.id.imgLeftBA);
        imgLeftBA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DecreaseBABudgetPeriod(view);
            }
        });

        ImageButton imgRightBA = findViewById(R.id.imgRightBA);
        imgRightBA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IncreaseBABudgetPeriod(view);
            }
        });

        ImageButton imgLeftCA = findViewById(R.id.imgLeftCA);
        imgLeftCA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DecreaseCABudgetPeriod(view);
            }
        });

        ImageButton imgRightCA = findViewById(R.id.imgRightCA);
        imgRightCA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IncreaseCABudgetPeriod(view);
            }
        });

        lblMonthlyExpense = findViewById(R.id.tvMonthlyExpense);
        lblMonthlyIncome = findViewById(R.id.tvMonthlyIncome);
        lblMonthlyTotal = findViewById(R.id.tvMonthlyLeftOver);
        lblExtraExpense = findViewById(R.id.tvExtraExpense);
        lblExtraIncome = findViewById(R.id.tvExtraIncome);
        lblExtraTotal = findViewById(R.id.tvExtraLeftOver);
        lblBudgetTotal = findViewById(R.id.tvFinalBudgetBalance);
        tvStartingBalance = findViewById(R.id.tvStartingBalance);
        tvMonthlyLeftOverLabel = findViewById(R.id.tvMonthlyLeftOverLabel);
        tvExtraLeftOverLabel = findViewById(R.id.tvExtraLeftOverLabel);

        swIncludeThisBudgetOnly = findViewById(R.id.swIncludeThisBudgetOnly);
        swIncludeThisBudgetOnly.setChecked(true);
        swIncludeThisBudgetOnly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ToggleIncludeThisBudgetOnly(view);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                startActivity(intent);
            }
        });
    }
    
    private void setupTabs()
    {
        TabHost host = findViewById(R.id.mainTabHost);
        host.setup();
        
        TabHost.TabSpec spec = host.newTabSpec("Dashboard");
        spec.setContent(R.id.tabDasboard);
        spec.setIndicator("Dashboard");
        host.addTab(spec);
        
        spec = host.newTabSpec("Current");
        spec.setContent(R.id.tabCurrentAccount);
        spec.setIndicator("Current");
        host.addTab(spec);
        
        spec = host.newTabSpec("Cash");
        spec.setContent(R.id.tabCashAccount);
        spec.setIndicator("Cash");
        host.addTab(spec);
        
        spec = host.newTabSpec("Budget");
        spec.setContent(R.id.tabBudget);
        spec.setIndicator("Budget");
        host.addTab(spec);
        
        spec = host.newTabSpec("General");
        spec.setContent(R.id.tabGeneralSavings);
        spec.setIndicator("General");
        host.addTab(spec);
        
        spec = host.newTabSpec("Long Term");
        spec.setContent(R.id.tabLongTermSavings);
        spec.setIndicator("Long Term");
        host.addTab(spec);
        
        spec = host.newTabSpec("Family");
        spec.setContent(R.id.tabFamilySavings);
        spec.setIndicator("Family");
        host.addTab(spec);
        
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
                if (tabId.compareTo("Cash") == 0)
                {
                    fab.setVisibility(View.VISIBLE);
                } else
                {
                    fab.setVisibility(View.GONE);
                }
            }
        });
    }
    
    @Override
    public void onBackPressed()
    {
        try
        {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);
            } else
            {
                super.onBackPressed();
            }
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onBackPressed", e.getMessage());
        }
    }
    
    private void setupNavigationSideBar()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            
            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onOptionsItemSelected", e.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }
    
    private ArrayList<RecordButton> getButtonList()
    {
        ArrayList<RecordButton> lList = new ArrayList<>();
        lList.add(new RecordButton(0, R.drawable.button, "Dashboard"));
        lList.add(new RecordButton(1, R.drawable.button, "Current Account"));
        lList.add(new RecordButton(2, R.drawable.button, "Cash Account"));
        lList.add(new RecordButton(3, R.drawable.button, "Budget"));
        lList.add(new RecordButton(4, R.drawable.button, "General Savings"));
        lList.add(new RecordButton(5, R.drawable.button, "Long Term Savings"));
        lList.add(new RecordButton(6, R.drawable.button, "Family Savings"));
        lList.get(0).selected = true;
        return (lList);
    }

    private ArrayList<RecordButton> getCommonButtonList()
    {
        ArrayList<RecordButton> lList = new ArrayList<>();
        ArrayList<RecordCommon> lItems = MyDatabase.MyDB().getCommonTransactionList();
        MyLog.WriteLogMessage("Common Button List - count " + lItems.size());
        for(int i=0;i<lItems.size();i++)
        {
            MyLog.WriteLogMessage("   " + lItems.get(i).TxDescription);
            lList.add(new RecordButton(i, R.drawable.button, lItems.get(i).TxDescription));
        }
        return (lList);
    }

    private void setupBankAccountView()
    {
        ArrayList<RecordTransaction> mDatasetCurrent = MyDatabase.MyDB().getTransactionList("11-03-95", "00038840", false, mCurrentBABudgetMonth, mCurrentBABudgetYear, swIncludeThisBudgetOnly.isChecked());
        mTransactionListCurrent =  findViewById(R.id.transactionListCurrent);
        mTransactionListCurrent.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerCommonButton = new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false);
        mTransactionListCommonButton.setLayoutManager(mLayoutManagerCommonButton);
        RecyclerView.LayoutManager mLayoutManagerButton = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        mTransactionListButton.setLayoutManager(mLayoutManagerButton);
        RecyclerView.LayoutManager mLayoutManagerCurrent = new LinearLayoutManager(this);
        mTransactionListCurrent.setLayoutManager(mLayoutManagerCurrent);

        TransactionAdapter mTransactionAdapterCurrent = new TransactionAdapter(mDatasetCurrent);
        mTransactionListCurrent.setAdapter(mTransactionAdapterCurrent);
        mTransactionAdapterCurrent.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);
            }
        });
    }

    private void setupCashView()
    {
        ArrayList<RecordTransaction> mDatasetCash = MyDatabase.MyDB().getTransactionList("Cash", "Cash", false, mCurrentCABudgetMonth, mCurrentCABudgetYear, false);
        mTransactionListCash = findViewById(R.id.transactionListCash);
        mTransactionListCash.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerCash = new LinearLayoutManager(this);
        mTransactionListCash.setLayoutManager(mLayoutManagerCash);

        TransactionAdapter mTransactionAdapterCash = new TransactionAdapter(mDatasetCash);
        mTransactionListCash.setAdapter(mTransactionAdapterCash);
        mTransactionAdapterCash.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);
            }
        });


    }

    private void setupRecyclerViews()
    {
        mDatasetButton = getButtonList();
        mDatasetCommonButton = getCommonButtonList();
        ArrayList<RecordTransaction> mDatasetGeneral = MyDatabase.MyDB().getTransactionList("11-18-11", "01446830", false);
        ArrayList<RecordTransaction> mDatasetLongTerm = MyDatabase.MyDB().getTransactionList("11-03-94", "02621503", false);
        ArrayList<RecordTransaction> mDatasetFamily = MyDatabase.MyDB().getTransactionList("11-03-94", "11522361", false);

        mTransactionListButton = findViewById(R.id.buttonList);
        mTransactionListGeneral = findViewById(R.id.transactionListGeneral);
        mTransactionListLongTerm =  findViewById(R.id.transactionListLongTerm);
        mTransactionListFamily = findViewById(R.id.transactionListFamily);
        mTransactionListCommonButton = findViewById(R.id.commonButtonList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mTransactionListButton.setHasFixedSize(true);
        mTransactionListGeneral.setHasFixedSize(true);
        mTransactionListLongTerm.setHasFixedSize(true);
        mTransactionListFamily.setHasFixedSize(true);
        mTransactionListCommonButton.setHasFixedSize(true);


        // use a linear layout manager

        RecyclerView.LayoutManager mLayoutManagerGeneral = new LinearLayoutManager(this);
        mTransactionListGeneral.setLayoutManager(mLayoutManagerGeneral);
    
        RecyclerView.LayoutManager mLayoutManagerLongTerm = new LinearLayoutManager(this);
        mTransactionListLongTerm.setLayoutManager(mLayoutManagerLongTerm);
    
        RecyclerView.LayoutManager mLayoutManagerFamily = new LinearLayoutManager(this);
        mTransactionListFamily.setLayoutManager(mLayoutManagerFamily);
    
        mTransactionAdapterButton = new ImageAdapter(mDatasetButton);
        mTransactionListButton.setAdapter(mTransactionAdapterButton);
        
        mTransactionAdapterButton.setOnItemClickListener(new ImageAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordButton obj)
            {
                TabHost host = findViewById(R.id.mainTabHost);
                host.setCurrentTab(obj.buttonId);
                for (int i = 0; i < mDatasetButton.size(); i++)
                    mDatasetButton.get(i).selected = false;
                obj.selected = true;

                mTransactionAdapterButton.notifyDataSetChanged();
                
            }
        });
        
        mTransactionAdapterCommonButton = new ImageAdapter(mDatasetCommonButton);
        mTransactionListCommonButton.setAdapter(mTransactionAdapterCommonButton);
        mTransactionAdapterCommonButton.setOnItemClickListener(new ImageAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordButton obj)
            {
                MyLog.WriteLogMessage("Setting TEMPLATEDESC to " + obj.buttonText);
                Intent intent = new Intent(MainActivity.this, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                intent.putExtra("TEMPLATEDESC", obj.buttonText);
                startActivity(intent);
            }
        });
        
        // specify an adapter (see also next example)

        TransactionAdapter mTransactionAdapterGeneral = new TransactionAdapter(mDatasetGeneral);
        mTransactionListGeneral.setAdapter(mTransactionAdapterGeneral);
        mTransactionAdapterGeneral.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);
            }
        });
    
        TransactionAdapter mTransactionAdapterLongTerm = new TransactionAdapter(mDatasetLongTerm);
        mTransactionListLongTerm.setAdapter(mTransactionAdapterLongTerm);
        mTransactionAdapterLongTerm.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);
            }
        });
    
        TransactionAdapter mTransactionAdapterFamily = new TransactionAdapter(mDatasetFamily);
        mTransactionListFamily.setAdapter(mTransactionAdapterFamily);
        mTransactionAdapterFamily.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);
            }
        });
    
        setupBankAccountView();
        setupCashView();
    }
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        try
        {
            // Handle navigation view item clicks here.
            int id = item.getItemId();
            
            if (id == R.id.nav_category)
            {
                Intent intent = new Intent(this, activityCategory.class);
                startActivity(intent);
            }
            if (id == R.id.nav_planning)
            {
                Intent intent = new Intent(this, activityPlanning.class);
                startActivity(intent);
            }
            if (id == R.id.nav_common)
            {
                Intent intent = new Intent(this, activityCommon.class);
                startActivity(intent);
            }
            if (id == R.id.nav_databasedump)
            {
                MyDatabase.MyDB().dumpDatabase();
            }
            if (id == R.id.nav_account)
            {
                Intent intent = new Intent(this, activityAccount.class);
                startActivity(intent);
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onNavigationItemSelected", e.getMessage());
        }
        return true;
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        
        try
        {
            mSelectedButton = 0;
            for (int i = 0; i < mDatasetButton.size(); i++)
                if(mDatasetButton.get(i).selected)
                    mSelectedButton = i;

            // save RecyclerView state
            mButtonViewState = new Bundle();
            mCurrentViewState = new Bundle();
            mGeneralViewState = new Bundle();
            mLongTermViewState = new Bundle();
            mFamilyViewState = new Bundle();
            mCashViewState = new Bundle();
            
            Parcelable listStateButton = mTransactionListButton.getLayoutManager().onSaveInstanceState();
            mButtonViewState.putParcelable(KEY_RECYCLER_STATE_BUTTON, listStateButton);
            
            Parcelable listStateCurrent = mTransactionListCurrent.getLayoutManager().onSaveInstanceState();
            mCurrentViewState.putParcelable(KEY_RECYCLER_STATE_CURRENT, listStateCurrent);
            
            Parcelable listStateGeneral = mTransactionListGeneral.getLayoutManager().onSaveInstanceState();
            mGeneralViewState.putParcelable(KEY_RECYCLER_STATE_GENERAL, listStateGeneral);
            
            Parcelable listStateLongTerm = mTransactionListLongTerm.getLayoutManager().onSaveInstanceState();
            mLongTermViewState.putParcelable(KEY_RECYCLER_STATE_LONGTERM, listStateLongTerm);
            
            Parcelable listStateFamily = mTransactionListFamily.getLayoutManager().onSaveInstanceState();
            mFamilyViewState.putParcelable(KEY_RECYCLER_STATE_FAMILY, listStateFamily);
            
            Parcelable listStateCash = mTransactionListCash.getLayoutManager().onSaveInstanceState();
            mCashViewState.putParcelable(KEY_RECYCLER_STATE_CASH, listStateCash);
            
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onPause", e.getMessage());
        }
        
    }
    
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        setupBudget();

        setupRecyclerViews();
        
        if (mButtonViewState != null)
        {
            Parcelable listStateButton = mButtonViewState.getParcelable(KEY_RECYCLER_STATE_BUTTON);
            mTransactionListButton.getLayoutManager().onRestoreInstanceState(listStateButton);
        }
        
        if (mCurrentViewState != null)
        {
            Parcelable listStateCurrent = mCurrentViewState.getParcelable(KEY_RECYCLER_STATE_CURRENT);
            mTransactionListCurrent.getLayoutManager().onRestoreInstanceState(listStateCurrent);
        }
        
        if (mGeneralViewState != null)
        {
            Parcelable listStateGeneral = mGeneralViewState.getParcelable(KEY_RECYCLER_STATE_GENERAL);
            mTransactionListGeneral.getLayoutManager().onRestoreInstanceState(listStateGeneral);
        }
        
        if (mLongTermViewState != null)
        {
            Parcelable listStateLongTerm = mLongTermViewState.getParcelable(KEY_RECYCLER_STATE_LONGTERM);
            mTransactionListLongTerm.getLayoutManager().onRestoreInstanceState(listStateLongTerm);
        }
        
        if (mFamilyViewState != null)
        {
            Parcelable listStateFamily = mFamilyViewState.getParcelable(KEY_RECYCLER_STATE_FAMILY);
            mTransactionListFamily.getLayoutManager().onRestoreInstanceState(listStateFamily);
        }
        
        if (mCashViewState != null)
        {
            Parcelable listStateCash = mCashViewState.getParcelable(KEY_RECYCLER_STATE_CASH);
            mTransactionListCash.getLayoutManager().onRestoreInstanceState(listStateCash);
        }
        for (int i = 0; i < mDatasetButton.size(); i++)
            mDatasetButton.get(i).selected = false;
        mDatasetButton.get(mSelectedButton).selected = true;

    }
    
}
