package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cooked.hb2.Adapters.ViewPagerMainAdapter;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    //region Members
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    private FragmentManager _fragmentManager;
    private ViewPager view_pager;
    private TabLayout tab_layout;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private boolean uiDirty;

    private FragmentDashboard _fragmentDashboard;
    private FragmentBudget _fragmentBudget;
    private ArrayList<FragmentAccount> _fragmentAccounts;
    private FragmentAccount _fragmentAnnualBills;

    private RecordBudgetMonth mDatasetBudgetMonth;

    public TextView txtNotes;
    public TextView txtBudgetPeriod;
    public ImageButton btnNextPeriod;
    public ImageButton btnPrevPeriod;

    private Toolbar toolbar;

    public ViewPagerMainAdapter adapter;

    private int mCurrentBudgetYear;
    private int mCurrentBudgetMonth;
    private int mPrevBudgetYear;
    private int mPrevBudgetMonth;
    //endregion

    //region Constructors/Destructors
    protected void onCreate(Bundle savedInstanceState)
    {
        MyLog.WriteLogMessage("MainActivity:onCreate:Starting");
        super.onCreate(savedInstanceState);

        setupStatics(this);

        MyPermission.EnsureAccessToExternalDrive(this);

        try
        {
            if(MyPermission.AccessAllowed())
            {
                if (MyDownloads.MyDL().FileCountToProcess()>0)
                {
                    Toast toast = Toast.makeText(this, "Processing Bank Files", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (MyDownloads.MyDL().CollectFiles() == FALSE)
                    return;

                RecreateUI();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupStatics:Ending");
    }
    //endregion

    //region Form Functions
    @Override
    public void onBackPressed()
    {
        MyLog.WriteLogMessage("MainActivity:onBackPressed:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onBackPressed:Ending");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MyLog.WriteLogMessage("MainActivity:onCreateOptionsMenu:Starting");
        try
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        } catch (Exception e)
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
            if(MyPermission.AccessAllowed()) {

                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();
                if (id == R.id.RefreshDBAndUI) {
                    // remove fragments from the fragment manager - forces them to be
                    // recreated
                    MyDatabase.MyDB().Dirty=true;

                    RemoveFragments();

                    mDatasetBudgetMonth = MyDatabase.MyDB().getDatasetBudgetMonth
                            (mCurrentBudgetMonth, mCurrentBudgetYear, true);

                    // Create the fragments
                    createAdapterAndFragments();
                    populateFragments();
                }
                if (id == R.id.showLog) {
                    Intent intent = new Intent(this, activityLog.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e)
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
            if(MyPermission.AccessAllowed()) {

                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_category) {
                    Intent intent = new Intent(this, activityCategory.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_planning) {
                    Intent intent = new Intent(this, activityPlanning.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_common) {
                    Intent intent = new Intent(this, activityCommon.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_databasedump) {
                    MyDatabase.MyDB().dumpDatabase();
                }
                if (id == R.id.nav_account) {
                    Intent intent = new Intent(this, activityAccount.class);
                    //startActivity(intent);
                    int lRequestCode = getResources().getInteger(R.integer.account_list_return);
                    startActivityForResult(intent, lRequestCode);
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        } catch (Exception e)
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
            if(MyPermission.AccessAllowed()) {

                super.onPause();
            }
        } catch (Exception e)
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
            if(MyPermission.AccessAllowed()) {

                super.onResume();
                if (MyDownloads.MyDL().CollectFiles() == FALSE)
                    return;
                RefreshFragments(mCurrentBudgetYear, mCurrentBudgetMonth);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onResume:Ending");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(MyPermission.AccessAllowed()) {

            if (requestCode == getResources().getInteger(R.integer.account_list_return)) {
                if (resultCode == RESULT_OK)
                {
                    boolean lAccountListChanged = data.getBooleanExtra("ACCOUNTLISTCHANGED", false);
                    // remove fragments from the fragment manager - forces them to be
                    // recreated
                    RemoveFragments();

                    // read from db - Dirty ensures everything is read
                    mDatasetBudgetMonth = MyDatabase.MyDB().getDatasetBudgetMonth
                        (mCurrentBudgetMonth, mCurrentBudgetYear, true);

                    // Create the fragments
                    createAdapterAndFragments();
                    populateFragments();
                }
            }
        }
    }

    //endregion

    //region Other functions
    public void RefreshFragments(int budgetYear, int budgetMonth)
    {
        try
        {
            if(MyPermission.AccessAllowed()) {
                SetBudget(budgetYear, budgetMonth);

                loadFromDB();

                setTheTitle();

                _fragmentDashboard.RefreshForm(mDatasetBudgetMonth);
                _fragmentBudget.RefreshForm(mDatasetBudgetMonth);
                for (int i = 0; i < _fragmentAccounts.size(); i++) {
                    _fragmentAccounts.get(i).RefreshForm(mDatasetBudgetMonth.accounts.get(i).RecordTransactions, mDatasetBudgetMonth);
                }
                _fragmentAnnualBills.RefreshForm(mDatasetBudgetMonth.mAnnualBills, mDatasetBudgetMonth);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    protected void RecreateUI()
    {
        if(MyPermission.AccessAllowed()) {
            SetBudget(DateUtils.dateUtils().CurrentBudgetYear(), DateUtils.dateUtils().CurrentBudgetMonth());

            setContentAndGetViews();

            loadFromDB();

            MyLog.WriteLogMessage("MainActivity:RecreateUI:Starting");

            setTheTitle();

            createAdapterAndFragments();

            populateFragments();

            setupNavigationSideBar();

            MyLog.WriteLogMessage("MainActivity:RecreateUI:Ending");

        }
    }

    private void SetBudget(int pBudgetYear, int pBudgetMonth)
    {
        try
        {
            if(MyPermission.AccessAllowed()) {
                mCurrentBudgetYear = pBudgetYear;
                mCurrentBudgetMonth = pBudgetMonth;

                if(mPrevBudgetYear!=mCurrentBudgetYear || mPrevBudgetMonth!=  mCurrentBudgetMonth)
                {
                    MyDatabase.MyDB().Dirty = true;
                    uiDirty = true;
                }
                mPrevBudgetYear = mCurrentBudgetYear;
                mPrevBudgetMonth = mCurrentBudgetMonth;

            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void setupStatics(Context lcontext)
    {
        MyLog.WriteLogMessage("MainActivity:setupStatics:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {
                context = lcontext;
                MyResources.setContext(context);
                MyLog.ClearLog();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupStatics:Ending");
    }

    private void loadFromDB()
    {
        MyLog.WriteLogMessage("MainActivity:loadFromDB:Starting");
        try
        {
            if(MyPermission.AccessAllowed())
            {
                if (MyDatabase.MyDB().Dirty)
                {
                    Toast toast = Toast.makeText(this, "Recalculating Budget", Toast.LENGTH_SHORT);
                    toast.show();
                }

                MyLog.WriteLogMessage("MainActivity:loadFromDB:Calling MyDatabase.MyDB().getDatasetBudgetMonth");
                mDatasetBudgetMonth = MyDatabase.MyDB().getDatasetBudgetMonth
                    (mCurrentBudgetMonth, mCurrentBudgetYear, true);
                MyLog.WriteLogMessage("MainActivity:loadFromDB:Returned from MyDatabase.MyDB().getDatasetBudgetMonth");

            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:loadFromDB:Ending");
    }

    private void setTheTitle()
    {
        MyLog.WriteLogMessage("MainActivity:setTheTitle:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {

                Date lFrom = DateUtils.dateUtils().BudgetStart(mCurrentBudgetMonth, mCurrentBudgetYear);
                Date lTo = DateUtils.dateUtils().BudgetEnd(mCurrentBudgetMonth, mCurrentBudgetYear);
                MyString lFromStr = new MyString();
                MyString lToStr = new MyString();
                DateUtils.dateUtils().DateToStr(lFrom, lFromStr);
                DateUtils.dateUtils().DateToStr(lTo, lToStr);
                ActionBar ab = getSupportActionBar();
                if (ab != null) {
                    ab.setTitle(getString(R.string.app_name));
                    ab.setSubtitle(" (" + getString(R.string.app_version) + ")");
                    txtBudgetPeriod.setText(lFromStr.Value + " -> " + lToStr.Value);
                }
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setTheTitle:Ending");
    }

    private void createAdapterAndFragments()
    {
        MyLog.WriteLogMessage("MainActivity:createAdapterAndFragments:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {

                _fragmentManager = getSupportFragmentManager();
                adapter = new ViewPagerMainAdapter(_fragmentManager);

                _fragmentDashboard = FragmentDashboard.newInstance();
                adapter.addFragment(_fragmentDashboard, "Dashboard");

                _fragmentBudget = FragmentBudget.newInstance();
                _fragmentBudget.lMainActivity = this;
                adapter.addFragment(_fragmentBudget, "Budget");

                _fragmentAccounts = new ArrayList<>();
                for (int i = 0; i < mDatasetBudgetMonth.accounts.size(); i++) {
                    RecordAccount ra = mDatasetBudgetMonth.accounts.get(i);

                    if(ra.AcHidden==0)
                    {
                        FragmentAccount fa = FragmentAccount.newInstance();
                        fa.SetAccount(ra.AcSortCode, ra.AcAccountNumber, ra.AcDescription);
                        _fragmentAccounts.add(fa);
                        adapter.addFragment(fa, ra.AcDescription);
                    }
                }

                _fragmentAnnualBills = FragmentAccount.newInstance();
                _fragmentAnnualBills.SetAccount("", "", "Annual Bills");
                adapter.addFragment(_fragmentAnnualBills, "Annual Bills");

                view_pager.setAdapter(adapter);

                tab_layout.setupWithViewPager(view_pager);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:createAdapterAndFragments:Ending");
    }

    private void populateFragments()
    {
        MyLog.WriteLogMessage("MainActivity:populateFragments:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {

                _fragmentDashboard.PopulateForm(mDatasetBudgetMonth);
                _fragmentBudget.PopulateForm(mDatasetBudgetMonth);
                for (int i = 0; i < _fragmentAccounts.size(); i++) {
                    FragmentAccount fa = _fragmentAccounts.get(i);
                    if (fa != null) {
                        RecordAccount ra = mDatasetBudgetMonth.FindAccount(fa.AcSortCode, fa.AcAccountNumber);
                        fa.RefreshForm(ra.RecordTransactions, mDatasetBudgetMonth);
                    }
                }
                _fragmentAnnualBills.RefreshForm(mDatasetBudgetMonth.mAnnualBills, mDatasetBudgetMonth);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:populateFragments:Ending");
    }

    private void setContentAndGetViews()
    {
        MyLog.WriteLogMessage("MainActivity:setContentAndGetViews:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {

                setContentView(R.layout.activity_main);
                toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                txtNotes = findViewById(R.id.txtNotes);
                txtBudgetPeriod = findViewById(R.id.txtBudgetPeriod);

                view_pager = findViewById(R.id.viewpager_main);
                tab_layout = findViewById(R.id.tab_main);

                drawer = findViewById(R.id.drawer_layout);
                navigationView = findViewById(R.id.nav_view);

                btnPrevPeriod = findViewById(R.id.btnPrevPeriod);
                btnPrevPeriod.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            mCurrentBudgetMonth--;
                            if (mCurrentBudgetMonth == 0) {
                                mCurrentBudgetYear--;
                                mCurrentBudgetMonth = 12;
                            }
                            RefreshFragments(mCurrentBudgetYear, mCurrentBudgetMonth);
                        } finally {
                        }
                    }
                });

                btnNextPeriod = findViewById(R.id.btnNextPeriod);
                btnNextPeriod.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        try {

                            mCurrentBudgetMonth++;
                            if (mCurrentBudgetMonth == 13) {
                                mCurrentBudgetYear++;
                                mCurrentBudgetMonth = 1;
                            }

                            RefreshFragments(mCurrentBudgetYear, mCurrentBudgetMonth);
                        } finally {
                        }
                    }
                });
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setContentAndGetViews:Ending");
    }

    private void setupNavigationSideBar()
    {
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Starting");
        try
        {
            if(MyPermission.AccessAllowed()) {

                toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                navigationView.setItemIconTintList(null);
                navigationView.setNavigationItemSelectedListener(this);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Ending");
    }

    private void RemoveFragments()
    {
        if(MyPermission.AccessAllowed()) {

            for (int i = 0; i < _fragmentAccounts.size(); i++) {
                Fragment f = _fragmentAccounts.get(i);
                FragmentTransaction trans = _fragmentManager.beginTransaction();
                trans.remove((Fragment) f);
                trans.commit();
            }

        }
    }

    //endregion

}
