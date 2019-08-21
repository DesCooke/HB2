package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import com.example.cooked.hb2.Adapters.ViewPagerMainAdapter;
import com.example.cooked.hb2.Budget.BudgetAdapter;
import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordAccount;
import com.example.cooked.hb2.Database.RecordButton;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.Fragments.FragmentAccount;
import com.example.cooked.hb2.Fragments.FragmentBudget;
import com.example.cooked.hb2.Fragments.FragmentDashboard;
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

    private ViewPager view_pager;
    private TabLayout tab_layout;
    private TabHost _host;
    private boolean _newMode;

    private final String KEY_RECYCLER_STATE_BUTTON = "recycler_state_button";
    private final String KEY_RECYCLER_STATE_CURRENT = "recycler_state_current";
    private final String KEY_RECYCLER_STATE_GENERAL = "recycler_state_general";
    private final String KEY_RECYCLER_STATE_LONGTERM = "recycler_state_longterm";
    private final String KEY_RECYCLER_STATE_FAMILY = "recycler_state_family";
    private final String KEY_RECYCLER_STATE_CASH = "recycler_state_cash";

    private FragmentDashboard _fragmentDashboard;
    private FragmentBudget _fragmentBudget;
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
    private RecordBudgetMonth mDatasetBudgetMonth;

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
        _newMode=false;
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

            checkMode();
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onCreate", e.getMessage());
        }
    }

    private void checkMode()
    {
        if(_newMode)
        {
            _host.setVisibility(View.GONE);
            mTransactionListButton.setVisibility(View.GONE);

            view_pager.getLayoutParams().height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            view_pager.requestLayout();
            view_pager.setVisibility(View.VISIBLE);
            tab_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            view_pager.setVisibility(View.GONE);
            tab_layout.setVisibility(View.GONE);

            _host.setVisibility(View.VISIBLE);
            mTransactionListButton.setVisibility(View.VISIBLE);
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

            mDatasetBudgetMonth = MyDatabase.MyDB().getBudgetMonth(mCurrentBudgetMonth, mCurrentBudgetYear, swIncludeThisBudgetOnly.isChecked());

            mDatasetBudget = mDatasetBudgetMonth.budgetGroups;

            _fragmentDashboard.PopulateForm(mDatasetBudgetMonth);
            _fragmentBudget.PopulateForm(mDatasetBudgetMonth);

            lblMonthlyIncome.setText(String.format(Locale.ENGLISH, "£%.2f", mDatasetBudgetMonth.monthlyIncome));
            lblMonthlyExpense.setText(String.format(Locale.ENGLISH, "£%.2f", mDatasetBudgetMonth.monthlyExpense));
            lblMonthlyTotal.setText(String.format(Locale.ENGLISH, "£%.2f", abs(mDatasetBudgetMonth.amountLeft)));
            if( (mDatasetBudgetMonth.amountLeft) > 0.00f)
            {
                tvMonthlyLeftOverLabel.setText("Monthly Underspend by...");
            }
            else
            {
                if( (mDatasetBudgetMonth.amountLeft) < 0.00f)
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
            
            lblExtraIncome.setText(String.format(Locale.ENGLISH, "£%.2f", mDatasetBudgetMonth.extraIncome));
            lblExtraExpense.setText(String.format(Locale.ENGLISH, "£%.2f", mDatasetBudgetMonth.extraExpense));
            lblExtraTotal.setText(String.format(Locale.ENGLISH, "£%.2f", abs(mDatasetBudgetMonth.extraLeft)));
            if( (mDatasetBudgetMonth.extraLeft) > 0.00f)
            {
                tvExtraLeftOverLabel.setText("Extra Underspend by...");
            }
            else
            {
                if( (mDatasetBudgetMonth.extraLeft) < 0.00f)
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

            tvStartingBalance.setText(String.format(Locale.ENGLISH, "£%.2f", mDatasetBudgetMonth.startingBalance));

            lblBudgetTotal.setText(String.format(Locale.ENGLISH, "£%.2f",mDatasetBudgetMonth.finalBudgetBalanceThisMonth));
            
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
        _host = findViewById(R.id.mainTabHost);




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
        _host.setup();
        
        TabHost.TabSpec spec = _host.newTabSpec("Dashboard");
        spec.setContent(R.id.tabDasboard);
        spec.setIndicator("Dashboard");
        _host.addTab(spec);
        
        spec = _host.newTabSpec("Current");
        spec.setContent(R.id.tabCurrentAccount);
        spec.setIndicator("Current");
        _host.addTab(spec);
        
        spec = _host.newTabSpec("Cash");
        spec.setContent(R.id.tabCashAccount);
        spec.setIndicator("Cash");
        _host.addTab(spec);
        
        spec = _host.newTabSpec("Budget");
        spec.setContent(R.id.tabBudget);
        spec.setIndicator("Budget");
        _host.addTab(spec);
        
        spec = _host.newTabSpec("General");
        spec.setContent(R.id.tabGeneralSavings);
        spec.setIndicator("General");
        _host.addTab(spec);
        
        spec = _host.newTabSpec("Long Term");
        spec.setContent(R.id.tabLongTermSavings);
        spec.setIndicator("Long Term");
        _host.addTab(spec);
        
        spec = _host.newTabSpec("Family");
        spec.setContent(R.id.tabFamilySavings);
        spec.setIndicator("Family");
        _host.addTab(spec);

        _host.setOnTabChangedListener(new TabHost.OnTabChangeListener()
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



        ArrayList<RecordAccount> list = MyDatabase.MyDB().getAccountList();

        view_pager = findViewById(R.id.viewpager_main);
        ViewPagerMainAdapter adapter =
                new ViewPagerMainAdapter(getSupportFragmentManager());

        _fragmentDashboard = FragmentDashboard.newInstance();
        adapter.addFragment(_fragmentDashboard, "Dashboard");

        _fragmentBudget = FragmentBudget.newInstance();
        adapter.addFragment(_fragmentBudget, "Budget");

        for(int i=0;i<list.size();i++)
        {
            adapter.addFragment(FragmentAccount.newInstance(), list.get(i).AcDescription);
        }
        view_pager.setAdapter(adapter);

        tab_layout = findViewById(R.id.tab_main);
        tab_layout.setupWithViewPager(view_pager);

        view_pager.setVisibility(View.GONE);
        tab_layout.setVisibility(View.GONE);
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
            if (id == R.id.toggle_views)
            {
                _newMode = ! _newMode;
                checkMode();
                _fragmentDashboard.PopulateForm(mDatasetBudgetMonth);
                _fragmentBudget.PopulateForm(mDatasetBudgetMonth);
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
                if(_host.getVisibility()== View.VISIBLE)
                {
                    _host.setCurrentTab(obj.buttonId);
                    for (int i = 0; i < mDatasetButton.size(); i++)
                        mDatasetButton.get(i).selected = false;
                    obj.selected = true;

                    mTransactionAdapterButton.notifyDataSetChanged();
                }
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
