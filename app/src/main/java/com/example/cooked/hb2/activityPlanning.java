package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Switch;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.TransactionUI.PlannedAdapter;

import java.util.ArrayList;

public class activityPlanning extends AppCompatActivity
{
    public Switch swActiveOnly;
    private Bundle mPlannedViewState;
    public RecyclerView mPlannedList;
    private final String KEY_RECYCLER_STATE_PLANNED = "recycler_state_planned";
    private TextInputLayout tilFilter;
    private TextInputEditText tietFilter;
    private String currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_planning);
            Toolbar toolbar = findViewById(R.id.toolbar);
            tilFilter = findViewById(R.id.tilFilter);
            tietFilter = findViewById(R.id.tietFilter);
            tietFilter.addTextChangedListener
                    (
                            new TextWatcher()
                            {
                                public void afterTextChanged(Editable s)
                                {

                                }

                                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after)
                                {
                                }

                                public void onTextChanged(CharSequence s, int start,
                                          int before, int count)
                                {
                                    currentFilter = tietFilter.getText().toString();
                                    SetupRecyclerView();
                                }
                            }
                    );

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

            currentFilter="";
            SetupRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    protected void SetupRecyclerView()
    {
        try
        {
            ArrayList<RecordPlanned> mDataset = MyDatabase.MyDB().getPlannedList(swActiveOnly.isChecked());
            ArrayList<RecordPlanned> mFilteredDataset;
            if(currentFilter.length()==0)
            {
                mFilteredDataset = mDataset;
            }
            else
            {
                mFilteredDataset = new ArrayList<RecordPlanned>();
                String lNeedle=currentFilter.toUpperCase();
                for (int i=0;i<mDataset.size(); i++)
                {
                    RecordPlanned rec = mDataset.get(i);
                    String lHaystack=rec.mPlannedName.toUpperCase();

                    if(lHaystack.contains(lNeedle))
                    {
                        mFilteredDataset.add(rec);
                    }
                }
            }
            mPlannedList = findViewById(R.id.plannedList);
            mPlannedList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManagerCurrent = new LinearLayoutManager(this);
            mPlannedList.setLayoutManager(mLayoutManagerCurrent);
            PlannedAdapter mPlannedAdapter = new PlannedAdapter(mFilteredDataset);
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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        try
        {
            SetupRecyclerView();

            if (mPlannedViewState != null)
            {
                Parcelable listStateButton = mPlannedViewState.getParcelable(KEY_RECYCLER_STATE_PLANNED);
                mPlannedList.getLayoutManager().onRestoreInstanceState(listStateButton);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    protected void onPause()
    {
        super.onPause();

        try
        {
            mPlannedViewState = new Bundle();

            Parcelable listStateButton = mPlannedList.getLayoutManager().onSaveInstanceState();
            mPlannedViewState.putParcelable(KEY_RECYCLER_STATE_PLANNED, listStateButton);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }
}
