package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import android.widget.Switch;
import android.widget.TextView;

import com.example.cooked.hb2.Adapters.ViewPagerMainAdapter;
import com.example.cooked.hb2.Adapters.BudgetAdapter;
import com.example.cooked.hb2.Records.RecordBudgetGroup;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.Database.RecordButton;
import com.example.cooked.hb2.Fragments.FragmentAccount;
import com.example.cooked.hb2.Fragments.FragmentBudget;
import com.example.cooked.hb2.Fragments.FragmentDashboard;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyDownloads;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyPermission;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener
{
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    private ViewPager view_pager;
    private TabLayout tab_layout;

    private boolean uiDirty;

    private FragmentDashboard _fragmentDashboard;
    private FragmentBudget _fragmentBudget;
    private ArrayList<FragmentAccount> _fragmentAccounts;

    private RecordBudgetMonth mDatasetBudgetMonth;

    public TextView txtNotes;

    private Toolbar toolbar;
    private FloatingActionButton fab;


    private Integer mCurrentBudgetYear;
    private Integer mCurrentBudgetMonth;

    private void setupStatics(Context lcontext)
    {
        context = lcontext;
        MyResources.setContext(context);
    }

    private void initialiseVariables()
    {
        mCurrentBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
        mCurrentBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
        MyDatabase.MyDB().Dirty = true;
        uiDirty = true;
    }

    private void loadOrRefresh()
    {
        if(uiDirty)
        {
            loadFromDB();
            loadUI();
            uiDirty=false;
        }
        else
        {
            refreshUI();
        }
    }

    private void refreshUI()
    {
        for(int i=0;i<_fragmentAccounts.size();i++)
            _fragmentAccounts.get(i).refreshUI();
        mDatasetBudgetMonth.RefreshTotals();
        if(_fragmentBudget!=null)
            _fragmentBudget.refreshUI();
        if(_fragmentDashboard!=null)
            _fragmentDashboard.refreshUI();

    }

    private void loadFromDB()
    {
        mDatasetBudgetMonth = MyDatabase.MyDB().getDatasetBudgetMonth
                (mCurrentBudgetMonth, mCurrentBudgetYear, true);
    }

    private void loadUI()
    {
        setupTitle();

        createAndLoadFragments();
    }

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

            initialiseVariables();

            setupActivity();

            loadOrRefresh();

            setupNavigationSideBar();
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
        loadOrRefresh();
    }
    
    private void DecreaseBudgetPeriod(View view)
    {
        mCurrentBudgetMonth--;
        if (mCurrentBudgetMonth < 1)
        {
            mCurrentBudgetMonth = 12;
            mCurrentBudgetYear--;
        }
        loadOrRefresh();
    }

    public void setupTitle()
    {
        Date lFrom=DateUtils.dateUtils().BudgetStart(mCurrentBudgetMonth, mCurrentBudgetYear);
        Date lTo=DateUtils.dateUtils().BudgetEnd(mCurrentBudgetMonth, mCurrentBudgetYear);
        MyString lFromStr=new MyString();
        MyString lToStr=new MyString();
        DateUtils.dateUtils().DateToStr(lFrom, lFromStr);
        DateUtils.dateUtils().DateToStr(lTo, lToStr);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab!=null)
        {
            ab.setTitle("HomeBudget");
            ab.setSubtitle(lFromStr.Value + " -> " + lToStr.Value);
        }
    }

    public void fragmentsRefresh()
    {
        mDatasetBudgetMonth.RefreshTotals();

        if(_fragmentDashboard!=null)
            _fragmentDashboard.RefreshForm(mDatasetBudgetMonth);
        if(_fragmentBudget!=null)
            _fragmentBudget.RefreshForm(mDatasetBudgetMonth);
        for(int i=0;i<_fragmentAccounts.size();i++)
        {
            FragmentAccount fa=_fragmentAccounts.get(i);
            if(fa!=null)
            {
                RecordAccount ra=mDatasetBudgetMonth.FindAccount(fa.AcSortCode, fa.AcAccountNumber);
                fa.RefreshForm(ra.RecordTransactions);
            }
        }
    }

    public void createAdapterAndFragments()
    {
        ViewPagerMainAdapter adapter = new ViewPagerMainAdapter(getSupportFragmentManager());

        _fragmentDashboard = FragmentDashboard.newInstance();
        adapter.addFragment(_fragmentDashboard, "Dashboard");

        _fragmentBudget = FragmentBudget.newInstance();
        _fragmentBudget.lMainActivity=this;
        adapter.addFragment(_fragmentBudget, "Budget");

        _fragmentAccounts = new ArrayList<>();
        for(int i=0;i<mDatasetBudgetMonth.accounts.size();i++)
        {
            RecordAccount ra = mDatasetBudgetMonth.accounts.get(i);
            FragmentAccount fa= FragmentAccount.newInstance();
            fa.SetAccount(ra.AcSortCode, ra.AcAccountNumber, ra.AcDescription);
            _fragmentAccounts.add(fa);
            adapter.addFragment(fa, ra.AcDescription);
        }
        view_pager.setAdapter(adapter);

        tab_layout.setupWithViewPager(view_pager);
    }

    public void loadFragments()
    {
        _fragmentDashboard.PopulateForm(mDatasetBudgetMonth);
        _fragmentBudget.PopulateForm(mDatasetBudgetMonth);
        for(int i=0;i<_fragmentAccounts.size();i++)
        {
            FragmentAccount fa=_fragmentAccounts.get(i);
            if(fa!=null)
            {
                RecordAccount ra=mDatasetBudgetMonth.FindAccount(fa.AcSortCode, fa.AcAccountNumber);
                fa.PopulateForm(ra.RecordTransactions);
            }
        }
    }

    public void createAndLoadFragments()
    {
        createAdapterAndFragments();

        loadFragments();
    }

    private void setupActivity()
    {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtNotes = findViewById(R.id.txtNotes);

        view_pager = findViewById(R.id.viewpager_main);
        tab_layout = findViewById(R.id.tab_main);

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
            if(id==R.id.RefreshDBAndUI)
            {
                uiDirty=true;
                MyDatabase.MyDB().Dirty=true;
                loadOrRefresh();
            }
            
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onOptionsItemSelected", e.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }
    
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
    }
    
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        loadOrRefresh();
    }
    
}
