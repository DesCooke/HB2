package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Switch;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.TransactionUI.PlannedAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    public ArrayList<RecordPlanned> mDataset;

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
            mDataset = MyDatabase.MyDB().getPlannedList(swActiveOnly.isChecked());
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
            mDataset = MyDatabase.MyDB().getPlannedList(swActiveOnly.isChecked());
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
