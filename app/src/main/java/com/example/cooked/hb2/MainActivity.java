package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cooked.hb2.Adapters.ViewPagerMainAdapter;
import com.example.cooked.hb2.GlobalUtils.DialogBudgetPicker;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.Fragments.FragmentAccount;
import com.example.cooked.hb2.Fragments.FragmentBudget;
import com.example.cooked.hb2.Fragments.FragmentDashboard;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyDownloads;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyPermission;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.ArrayList;
import java.util.Date;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
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


    private Integer mCurrentBudgetYear;
    private Integer mCurrentBudgetMonth;

    private void setupStatics(Context lcontext)
    {
        MyLog.WriteLogMessage("MainActivity:setupStatics:Starting");
        try
        {
            context = lcontext;
            MyResources.setContext(context);
            MyLog.ClearLog();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupStatics:Ending");
    }

    private void initialiseVariables()
    {
        MyLog.WriteLogMessage("MainActivity:initialiseVariables:Starting");
        try
        {
            mCurrentBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
            mCurrentBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
            MyDatabase.MyDB().Dirty = true;
            uiDirty = true;
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:initialiseVariables:Ending");
    }

    private void createOrUpdate()
    {
        MyLog.WriteLogMessage("MainActivity:createOrUpdate:Starting");
        try
        {
            if (uiDirty)
            {
                createFromDB();
                createUI();
                uiDirty = false;
            } else
            {
                updateUI();
            }
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createOrUpdate:Ending");
    }

    private void updateUI()
    {
        MyLog.WriteLogMessage("MainActivity:updateUI:Starting");
        try
        {
            for (int i = 0; i < _fragmentAccounts.size(); i++)
                _fragmentAccounts.get(i).refreshUI();
            mDatasetBudgetMonth.RefreshTotals();
            if (_fragmentBudget != null)
                _fragmentBudget.refreshUI();
            if (_fragmentDashboard != null)
                _fragmentDashboard.refreshUI();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:updateUI:Ending");
    }

    private void createFromDB()
    {
        MyLog.WriteLogMessage("MainActivity:createFromDB:Starting");
        try
        {
            mDatasetBudgetMonth = MyDatabase.MyDB().getDatasetBudgetMonth
                    (mCurrentBudgetMonth, mCurrentBudgetYear, true);
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createFromDB:Ending");
    }

    private void createUI()
    {
        MyLog.WriteLogMessage("MainActivity:createUI:Starting");
        try
        {
            createTitle();

            createAndLoadFragments();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createUI:Ending");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MyLog.WriteLogMessage("MainActivity:onCreate:Starting");
        super.onCreate(savedInstanceState);

        setupStatics(this);
        
        if (!MyPermission.checkIfAlreadyHavePermission(this))
            MyPermission.requestForSpecificPermission(this);
        try
        {
            if (MyDownloads.MyDL().CollectFiles() == FALSE)
                return;

            initialiseVariables();

            setupActivity();

            createOrUpdate();

            setupNavigationSideBar();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupStatics:Ending");
    }

    private void increaseBudgetPeriod(View view)
    {
        MyLog.WriteLogMessage("MainActivity:increaseBudgetPeriod:Starting");
        try
        {
            mCurrentBudgetMonth++;
            if (mCurrentBudgetMonth > 12)
            {
                mCurrentBudgetMonth = 1;
                mCurrentBudgetYear++;
            }
            createOrUpdate();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:increaseBudgetPeriod:Ending");
    }
    
    private void decreaseBudgetPeriod(View view)
    {
        MyLog.WriteLogMessage("MainActivity:decreaseBudgetPeriod:Starting");
        try
        {
            mCurrentBudgetMonth--;
            if (mCurrentBudgetMonth < 1)
            {
                mCurrentBudgetMonth = 12;
                mCurrentBudgetYear--;
            }
            createOrUpdate();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:decreaseBudgetPeriod:Ending");
    }

    public void createTitle()
    {
        MyLog.WriteLogMessage("MainActivity:createTitle:Starting");
        try
        {
            Date lFrom = DateUtils.dateUtils().BudgetStart(mCurrentBudgetMonth, mCurrentBudgetYear);
            Date lTo = DateUtils.dateUtils().BudgetEnd(mCurrentBudgetMonth, mCurrentBudgetYear);
            MyString lFromStr = new MyString();
            MyString lToStr = new MyString();
            DateUtils.dateUtils().DateToStr(lFrom, lFromStr);
            DateUtils.dateUtils().DateToStr(lTo, lToStr);
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null)
            {
                ab.setTitle("HomeBudget");
                ab.setSubtitle(lFromStr.Value + " -> " + lToStr.Value);
            }
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createTitle:Ending");
    }

    public void fragmentsRefresh()
    {
        MyLog.WriteLogMessage("MainActivity:fragmentsRefresh:Starting");
        try
        {
            mDatasetBudgetMonth.RefreshTotals();

            if (_fragmentDashboard != null)
                _fragmentDashboard.RefreshForm(mDatasetBudgetMonth);
            if (_fragmentBudget != null)
                _fragmentBudget.RefreshForm(mDatasetBudgetMonth);
            for (int i = 0; i < _fragmentAccounts.size(); i++)
            {
                FragmentAccount fa = _fragmentAccounts.get(i);
                if (fa != null)
                {
                    RecordAccount ra = mDatasetBudgetMonth.FindAccount(fa.AcSortCode, fa.AcAccountNumber);
                    fa.RefreshForm(ra.RecordTransactions);
                }
            }
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:fragmentsRefresh:Ending");
    }

    public void createAdapterAndFragments()
    {
        MyLog.WriteLogMessage("MainActivity:createAdapterAndFragments:Starting");
        try
        {
            ViewPagerMainAdapter adapter = new ViewPagerMainAdapter(getSupportFragmentManager());

            _fragmentDashboard = FragmentDashboard.newInstance();
            adapter.addFragment(_fragmentDashboard, "Dashboard");

            _fragmentBudget = FragmentBudget.newInstance();
            _fragmentBudget.lMainActivity = this;
            adapter.addFragment(_fragmentBudget, "Budget");

            _fragmentAccounts = new ArrayList<>();
            for (int i = 0; i < mDatasetBudgetMonth.accounts.size(); i++)
            {
                RecordAccount ra = mDatasetBudgetMonth.accounts.get(i);

                FragmentAccount fa = FragmentAccount.newInstance();
                fa.SetAccount(ra.AcSortCode, ra.AcAccountNumber, ra.AcDescription);
                _fragmentAccounts.add(fa);
                adapter.addFragment(fa, ra.AcDescription);
            }
            view_pager.setAdapter(adapter);

            tab_layout.setupWithViewPager(view_pager);
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createAdapterAndFragments:Ending");
    }

    public void loadFragments()
    {
        MyLog.WriteLogMessage("MainActivity:loadFragments:Starting");
        try
        {
            _fragmentDashboard.PopulateForm(mDatasetBudgetMonth);
            _fragmentBudget.PopulateForm(mDatasetBudgetMonth);
            for (int i = 0; i < _fragmentAccounts.size(); i++)
            {
                FragmentAccount fa = _fragmentAccounts.get(i);
                if (fa != null)
                {
                    RecordAccount ra = mDatasetBudgetMonth.FindAccount(fa.AcSortCode, fa.AcAccountNumber);
                    fa.PopulateForm(ra.RecordTransactions);
                }
            }
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:loadFragments:Ending");
    }

    public void createAndLoadFragments()
    {
        MyLog.WriteLogMessage("MainActivity:createAndLoadFragments:Starting");
        try
        {
            createAdapterAndFragments();

            loadFragments();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createAndLoadFragments:Ending");
    }

    private void setupActivity()
    {
        MyLog.WriteLogMessage("MainActivity:setupActivity:Starting");
        try
        {
            setContentView(R.layout.activity_main);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            txtNotes = findViewById(R.id.txtNotes);

            view_pager = findViewById(R.id.viewpager_main);
            tab_layout = findViewById(R.id.tab_main);

        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupActivity:Ending");
    }
    
    @Override
    public void onBackPressed()
    {
        MyLog.WriteLogMessage("MainActivity:onBackPressed:Starting");
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
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onBackPressed:Ending");
    }
    
    private void setupNavigationSideBar()
    {
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Starting");
        try
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
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Ending");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MyLog.WriteLogMessage("MainActivity:onCreateOptionsMenu:Starting");
        try
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onCreateOptionsMenu:Ending");
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        MyLog.WriteLogMessage("MainActivity:onOptionsItemSelected:Starting");
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
                createOrUpdate();
            }
            if(id==R.id.ChangeDate)
            {
                DialogBudgetPicker dbp=new DialogBudgetPicker(this);
                dbp.show();
            }
            if(id==R.id.showLog)
            {
                Intent intent = new Intent(this, activityLog.class);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onOptionsItemSelected:Ending");
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        MyLog.WriteLogMessage("MainActivity:onNavigationItemSelected:Starting");
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
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onNavigationItemSelected:Ending");
        return true;
    }
    
    @Override
    protected void onPause()
    {
        MyLog.WriteLogMessage("MainActivity:onPause:Starting");
        try
        {
            super.onPause();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onPause:Ending");
    }
    
    @Override
    public void onResume()
    {
        MyLog.WriteLogMessage("MainActivity:onResume:Starting");
        try
        {
            super.onResume();
            createOrUpdate();
        }
        catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onResume:Ending");
    }
    
}
