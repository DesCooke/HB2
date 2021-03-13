package com.example.cooked.hb2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.GlobalUtils.MyLog;

public class activityHelp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private Toolbar mTopToolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
        setContentView(R.layout.activity_help);
            mTopToolbar = (Toolbar) findViewById(R.id.help_toolbar);
            setSupportActionBar(mTopToolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener
                (
                        new NavigationView.OnNavigationItemSelectedListener()
                        {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem)
                            {
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawers();
                                return true;
                            }
                        }
                );

        setupNavigationSideBar();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Ending");
    }

    private void setupNavigationSideBar()
    {
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Starting");
        try
        {
            toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:setupNavigationSideBar:Ending");
    }

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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("MainActivity:onNavigationItemSelected:Ending");
        return true;
    }

}