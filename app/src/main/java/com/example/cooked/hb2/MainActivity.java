package com.example.cooked.hb2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hb2.Budget.BudgetAdapter;
import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordButton;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyApiSpecific;
import com.example.cooked.hb2.GlobalUtils.MyDownloads;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyPermission;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.TransactionUI.TransactionAdapter;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Context context;

    private RecyclerView mTransactionListButton;
    private RecyclerView mTransactionListCurrent;
    private RecyclerView mTransactionListGeneral;
    private RecyclerView mTransactionListLongTerm;
    private RecyclerView mTransactionListFamily;
    private RecyclerView mTransactionListCash;
    private ExpandableListView budgetListView;

    private ArrayList<RecordBudgetGroup> mDatasetBudget;
    private ArrayList<RecordButton> mDatasetButton;
    private ArrayList<RecordTransaction> mDatasetCurrent;
    private ArrayList<RecordTransaction> mDatasetGeneral;
    private ArrayList<RecordTransaction> mDatasetLongTerm;
    private ArrayList<RecordTransaction> mDatasetFamily;
    private ArrayList<RecordTransaction> mDatasetCash;
    
    private RecyclerView.LayoutManager mLayoutManagerButton;
    private RecyclerView.LayoutManager mLayoutManagerCurrent;
    private RecyclerView.LayoutManager mLayoutManagerGeneral;
    private RecyclerView.LayoutManager mLayoutManagerLongTerm;
    private RecyclerView.LayoutManager mLayoutManagerFamily;
    private RecyclerView.LayoutManager mLayoutManagerCash;
    
    private BudgetAdapter budgetAdapter;
    private ImageAdapter mTransactionAdapterButton;
    private TransactionAdapter mTransactionAdapterCurrent;
    private TransactionAdapter mTransactionAdapterGeneral;
    private TransactionAdapter mTransactionAdapterLongTerm;
    private TransactionAdapter mTransactionAdapterFamily;
    private TransactionAdapter mTransactionAdapterCash;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ImageButton imgLeft;
    private ImageButton imgRight;
    private TextView txtBudgetTitle;
    
    private Integer mCurrentBudgetYear;
    private Integer mCurrentBudgetMonth;
    
    private void setupStatics(Context lcontext) {
        context = lcontext;
        MyResources.setContext(context);
        MyApiSpecific.setContext(context);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupStatics(this);

        if (!MyPermission.checkIfAlreadyHavePermission(this))
            MyPermission.requestForSpecificPermission(this);
        try {
            MyLog.WriteLogMessage("Starting");

            if (MyDownloads.MyDL().CollectFiles() == FALSE)
                return;

            setupActivity();

            mCurrentBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
            mCurrentBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
            
            setupBudget();
            
            setupTabs();

            setupNavigationSideBar();

            setupRecyclerViews();
            
        } catch (Exception e) {
            ErrorDialog.Show("Error in MainActivity::onCreate", e.getMessage());
        }
    }

    private void IncreaseBudgetPeriod(View view)
    {
        mCurrentBudgetMonth++;
        if(mCurrentBudgetMonth>12)
        {
            mCurrentBudgetMonth=1;
            mCurrentBudgetYear++;
        }
        setupBudget();
    }
    
    private void DecreaseBudgetPeriod(View view)
    {
        mCurrentBudgetMonth--;
        if(mCurrentBudgetMonth<1)
        {
            mCurrentBudgetMonth=12;
            mCurrentBudgetYear--;
        }
        setupBudget();
    }

    private void setupBudget()
    {
        try
        {
            String lTitle = DateUtils.dateUtils().MonthAsText(mCurrentBudgetMonth) + " / " +
                mCurrentBudgetYear.toString();
            txtBudgetTitle.setText(lTitle);
            
            mDatasetBudget = MyDatabase.MyDB().getBudget(mCurrentBudgetMonth,mCurrentBudgetYear);
    
            //get reference of the ExpandableListView
            budgetListView = (ExpandableListView) findViewById(R.id.budgetList);
    
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
                    Toast.makeText(getBaseContext(), " Clicked on :: " + budgetGroupInfo.budgetGroupName
                        + "/" + budgetItemInfo.budgetItemName, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getBaseContext(), " Header is :: " + budgetGroupInfo.budgetGroupName,
                        Toast.LENGTH_LONG).show();
            
                    return false;
                }
            });
        } catch (Exception e) {
            ErrorDialog.Show("Error in MainActivity::setupBudget", e.getMessage());
        }
    }
    
//method to expand all groups
    private void expandAll() {
        int count = budgetAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            budgetListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = budgetAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            budgetListView.collapseGroup(i);
        }
    }
    private void setupActivity() {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        txtBudgetTitle = findViewById(R.id.txtBudgetTitle);
        
        imgLeft = findViewById(R.id.imgLeft);
        imgLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DecreaseBudgetPeriod(view);
            }
        });

        imgRight = findViewById(R.id.imgRight);
        imgRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IncreaseBudgetPeriod(view);
            }
        });
        
        fab = (FloatingActionButton) findViewById(R.id.fab);
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

    private void setupTabs() {
        TabHost host = (TabHost) findViewById(R.id.mainTabHost);
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

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.compareTo("Cash")==0)
                {
                    fab.setVisibility(View.VISIBLE);
                }
                else
                {
                    fab.setVisibility(View.GONE);
                }
            }});
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            ErrorDialog.Show("Error in MainActivity::onBackPressed", e.getMessage());
        }
    }

    private void setupNavigationSideBar() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        } catch (Exception e) {
            ErrorDialog.Show("Error in MainActivity::onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
        } catch (Exception e) {
            ErrorDialog.Show("Error in MainActivity::onOptionsItemSelected", e.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<RecordButton>getButtonList()
    {
        ArrayList<RecordButton>lList = new ArrayList<>();
        lList.add(new RecordButton(0, R.drawable.button, "Dashboard"));
        lList.add(new RecordButton(1, R.drawable.button, "Current Account"));
        lList.add(new RecordButton(2, R.drawable.button, "Cash Account"));
        lList.add(new RecordButton(3, R.drawable.button, "Budget"));
        lList.add(new RecordButton(4, R.drawable.button, "General Savings"));
        lList.add(new RecordButton(5, R.drawable.button, "Long Term Savings"));
        lList.add(new RecordButton(6, R.drawable.button, "Family Savings"));
        lList.get(0).selected = true;
        return(lList);
    }
    private void setupRecyclerViews() {
        mDatasetButton = getButtonList();
        mDatasetCurrent = MyDatabase.MyDB().getTransactionList("11-03-95", "00038840");
        mDatasetGeneral = MyDatabase.MyDB().getTransactionList("11-18-11", "01446830");
        mDatasetLongTerm = MyDatabase.MyDB().getTransactionList("11-03-94", "02621503");
        mDatasetFamily = MyDatabase.MyDB().getTransactionList("11-03-94", "11522361");
        mDatasetCash = MyDatabase.MyDB().getTransactionList("Cash", "Cash");

        mTransactionListButton = (RecyclerView) findViewById(R.id.buttonList);
        mTransactionListCurrent = (RecyclerView) findViewById(R.id.transactionListCurrent);
        mTransactionListGeneral = (RecyclerView) findViewById(R.id.transactionListGeneral);
        mTransactionListLongTerm = (RecyclerView) findViewById(R.id.transactionListLongTerm);
        mTransactionListFamily = (RecyclerView) findViewById(R.id.transactionListFamily);
        mTransactionListCash = (RecyclerView) findViewById(R.id.transactionListCash);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mTransactionListButton.setHasFixedSize(true);
        mTransactionListCurrent.setHasFixedSize(true);
        mTransactionListGeneral.setHasFixedSize(true);
        mTransactionListLongTerm.setHasFixedSize(true);
        mTransactionListFamily.setHasFixedSize(true);
        mTransactionListCash.setHasFixedSize(true);

        mLayoutManagerButton = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        mTransactionListButton.setLayoutManager(mLayoutManagerButton);

        // use a linear layout manager
        mLayoutManagerCurrent = new LinearLayoutManager(this);
        mTransactionListCurrent.setLayoutManager(mLayoutManagerCurrent);

        mLayoutManagerGeneral = new LinearLayoutManager(this);
        mTransactionListGeneral.setLayoutManager(mLayoutManagerGeneral);

        mLayoutManagerLongTerm = new LinearLayoutManager(this);
        mTransactionListLongTerm.setLayoutManager(mLayoutManagerLongTerm);

        mLayoutManagerFamily = new LinearLayoutManager(this);
        mTransactionListFamily.setLayoutManager(mLayoutManagerFamily);

        mLayoutManagerCash = new LinearLayoutManager(this);
        mTransactionListCash.setLayoutManager(mLayoutManagerCash);

        mTransactionAdapterButton = new ImageAdapter(mDatasetButton);
        mTransactionListButton.setAdapter(mTransactionAdapterButton);
        
        mTransactionAdapterButton.setOnItemClickListener(new ImageAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordButton obj)
            {
                TabHost host = (TabHost) findViewById(R.id.mainTabHost);
                host.setCurrentTab(obj.buttonId);
                for(int i=0;i<mDatasetButton.size();i++)
                    mDatasetButton.get(i).selected = false;
                obj.selected = true;
                mTransactionAdapterButton.notifyDataSetChanged();
                
            }
        });


        // specify an adapter (see also next example)
        mTransactionAdapterCurrent = new TransactionAdapter(mDatasetCurrent);
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

        mTransactionAdapterGeneral = new TransactionAdapter(mDatasetGeneral);
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

        mTransactionAdapterLongTerm = new TransactionAdapter(mDatasetLongTerm);
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

        mTransactionAdapterFamily = new TransactionAdapter(mDatasetFamily);
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

        mTransactionAdapterCash = new TransactionAdapter(mDatasetCash);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_category) {
                Intent intent = new Intent(this, activityCategory.class);
                startActivity(intent);
            } else if (id == R.id.nav_planning) {
                Intent intent = new Intent(this, activityPlanning.class);
                startActivity(intent);
            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            ErrorDialog.Show("Error in MainActivity::onNavigationItemSelected", e.getMessage());
        }
        return true;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        setupRecyclerViews();
    }

}
