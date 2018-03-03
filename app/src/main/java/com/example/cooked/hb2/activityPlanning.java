package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.TransactionUI.PlannedAdapter;

import java.util.ArrayList;

public class activityPlanning extends AppCompatActivity
{
    private RecyclerView mPlannedList;
    private ArrayList<RecordPlanned> mDataset;
    private RecyclerView.LayoutManager mLayoutManagerCurrent;
    private PlannedAdapter mPlannedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        setTitle("Planning");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), activityPlanningItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                startActivity(intent);
            }
        });

        SetupRecyclerView();
    }

    protected void SetupRecyclerView()
    {
        mDataset = MyDatabase.MyDB().getPlannedList();
        mPlannedList = (RecyclerView) findViewById(R.id.plannedList);
        mPlannedList.setHasFixedSize(true);
        mLayoutManagerCurrent = new LinearLayoutManager(this);
        mPlannedList.setLayoutManager(mLayoutManagerCurrent);
        mPlannedAdapter = new PlannedAdapter(mDataset);
        mPlannedList.setAdapter(mPlannedAdapter);
        mPlannedAdapter.setOnItemClickListener(new PlannedAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordPlanned obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityPlanningItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("PlannedId", obj.mPlannedId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        SetupRecyclerView();
    }


}