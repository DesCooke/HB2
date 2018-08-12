package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.TransactionUI.PlannedAdapter;

import java.util.ArrayList;

public class activityPlanning extends AppCompatActivity
{
    public Switch swActiveOnly;
    private Bundle mPlannedViewState;
    public RecyclerView mPlannedList;
    private final String KEY_RECYCLER_STATE_PLANNED = "recycler_state_planned";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        setTitle("Planning");

        FloatingActionButton fab = findViewById(R.id.fab);
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

        swActiveOnly = findViewById(R.id.swActiveOnly);
        swActiveOnly.setChecked(true);
        swActiveOnly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetupRecyclerView();
            }
        });

        SetupRecyclerView();
    }

    protected void SetupRecyclerView()
    {
        ArrayList<RecordPlanned> mDataset = MyDatabase.MyDB().getPlannedList(swActiveOnly.isChecked());
        mPlannedList = findViewById(R.id.plannedList);
        mPlannedList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerCurrent = new LinearLayoutManager(this);
        mPlannedList.setLayoutManager(mLayoutManagerCurrent);
        PlannedAdapter mPlannedAdapter = new PlannedAdapter(mDataset);
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

        if (mPlannedViewState != null)
        {
            Parcelable listStateButton = mPlannedViewState.getParcelable(KEY_RECYCLER_STATE_PLANNED);
            mPlannedList.getLayoutManager().onRestoreInstanceState(listStateButton);
        }
    }

    protected void onPause() {
        super.onPause();

        try {
            mPlannedViewState = new Bundle();

            Parcelable listStateButton = mPlannedList.getLayoutManager().onSaveInstanceState();
            mPlannedViewState.putParcelable(KEY_RECYCLER_STATE_PLANNED, listStateButton);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in activityPlanned::onPause", e.getMessage());
        }
    }
}
