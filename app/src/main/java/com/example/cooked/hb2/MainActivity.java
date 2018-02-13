package com.example.cooked.hb2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyDownloads;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyPermission;
import com.example.cooked.hb2.TransactionUI.TransactionAdapter;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener
{
    public static Context context;

    private RecyclerView mTransactionListCurrent;
    private RecyclerView mTransactionListGeneral;
    private RecyclerView mTransactionListLongTerm;
    private RecyclerView mTransactionListFamily;
    private ArrayList<RecordTransaction> mDatasetCurrent;
    private ArrayList<RecordTransaction> mDatasetGeneral;
    private ArrayList<RecordTransaction> mDatasetLongTerm;
    private ArrayList<RecordTransaction> mDatasetFamily;
    private RecyclerView.LayoutManager mLayoutManagerCurrent;
    private RecyclerView.LayoutManager mLayoutManagerGeneral;
    private RecyclerView.LayoutManager mLayoutManagerLongTerm;
    private RecyclerView.LayoutManager mLayoutManagerFamily;
    private TransactionAdapter mTransactionAdapterCurrent;
    private TransactionAdapter mTransactionAdapterGeneral;
    private TransactionAdapter mTransactionAdapterLongTerm;
    private TransactionAdapter mTransactionAdapterFamily;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;
        if (!MyPermission.checkIfAlreadyHavePermission(this))
            MyPermission.requestForSpecificPermission(this);
        try
        {
            //MyLog.RemoveLog();
            MyLog.SetContext(this);
            MyLog.WriteLogMessage("Starting");

            SQLiteDatabase db = MyDatabase.MyDB().getWritableDatabase();
            
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    
            
        TabHost host = (TabHost)findViewById(R.id.mainTabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Dashboard");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Dashboard");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Current");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Current");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("General");
        spec.setContent(R.id.tab3);
        spec.setIndicator("General");
        host.addTab(spec);
        
        //Tab 4
        spec = host.newTabSpec("Long Term");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Long Term");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Family");
        spec.setContent(R.id.tab5);
        spec.setIndicator("Family");
        host.addTab(spec);

final TabWidget tw = (TabWidget)host.findViewById(android.R.id.tabs);
    for (int i = 0; i < tw.getChildCount(); ++i)
    {
        final View tabView = tw.getChildTabViewAt(i);
        final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setTextSize(8);
    }
            if(MyDownloads.MyDL().CollectFiles()==FALSE)
                return;
            
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            });
    
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
    
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);

            mDatasetCurrent = MyDatabase.MyDB().getTransactionList("11-03-95", "00038840");
            mDatasetGeneral = MyDatabase.MyDB().getTransactionList("11-18-11", "01446830");
            mDatasetLongTerm = MyDatabase.MyDB().getTransactionList("11-03-94", "02621503");
            mDatasetFamily = MyDatabase.MyDB().getTransactionList("11-03-94", "11522361");

            mTransactionListCurrent = (RecyclerView) findViewById(R.id.transactionListCurrent);
            mTransactionListGeneral = (RecyclerView) findViewById(R.id.transactionListGeneral);
            mTransactionListLongTerm = (RecyclerView) findViewById(R.id.transactionListLongTerm);
            mTransactionListFamily = (RecyclerView) findViewById(R.id.transactionListFamily);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mTransactionListCurrent.setHasFixedSize(true);
            mTransactionListGeneral.setHasFixedSize(true);
            mTransactionListLongTerm.setHasFixedSize(true);
            mTransactionListFamily.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManagerCurrent = new LinearLayoutManager(this);
            mTransactionListCurrent.setLayoutManager(mLayoutManagerCurrent);

            mLayoutManagerGeneral = new LinearLayoutManager(this);
            mTransactionListGeneral.setLayoutManager(mLayoutManagerGeneral);

            mLayoutManagerLongTerm = new LinearLayoutManager(this);
            mTransactionListLongTerm.setLayoutManager(mLayoutManagerLongTerm);

            mLayoutManagerFamily = new LinearLayoutManager(this);
            mTransactionListFamily.setLayoutManager(mLayoutManagerFamily);

            // specify an adapter (see also next example)
            mTransactionAdapterCurrent = new TransactionAdapter(mDatasetCurrent);
            mTransactionListCurrent.setAdapter(mTransactionAdapterCurrent);

            mTransactionAdapterGeneral = new TransactionAdapter(mDatasetGeneral);
            mTransactionListGeneral.setAdapter(mTransactionAdapterGeneral);

            mTransactionAdapterLongTerm = new TransactionAdapter(mDatasetLongTerm);
            mTransactionListLongTerm.setAdapter(mTransactionAdapterLongTerm);

            mTransactionAdapterFamily = new TransactionAdapter(mDatasetFamily);
            mTransactionListFamily.setAdapter(mTransactionAdapterFamily);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onCreate", e.getMessage());
        }
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
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onBackPressed", e.getMessage());
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        }
        catch(Exception e)
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
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onOptionsItemSelected", e.getMessage());
        }
        return super.onOptionsItemSelected(item);
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
            } else if (id == R.id.nav_gallery)
            {
        
            } else if (id == R.id.nav_slideshow)
            {
        
            } else if (id == R.id.nav_manage)
            {
        
            } else if (id == R.id.nav_share)
            {
        
            } else if (id == R.id.nav_send)
            {
        
            }
    
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MainActivity::onNavigationItemSelected", e.getMessage());
        }
        return true;
    }
}
