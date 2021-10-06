package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import com.example.cooked.hb2.Adapters.VariationAdapter;
import com.example.cooked.hb2.CategoryUI.SubCategoryAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.Database.RecordPlannedVariation;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class activityPlanningVariation extends AppCompatActivity {

    public Integer plannedId;
    public RecordPlanned recordPlanned;
    public FloatingActionButton fab;
    public ArrayList<RecordPlannedVariation> mDataset;
    public RecyclerView mVariationList;
    public RecyclerView.LayoutManager mLayoutManagerCurrent;
    public VariationAdapter mVariationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_variation);

        FloatingActionButton fab = findViewById(R.id.fab);

        plannedId = getIntent().getIntExtra("PLANNEDID", 0);
        recordPlanned = MyDatabase.MyDB().getSinglePlanned(plannedId);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(activityPlanningVariation.this, activityPlanningVariationItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                intent.putExtra("PLANNEDID", plannedId);
                startActivity(intent);
            }
        });

        setTitle("Variations for " + recordPlanned.mPlannedName);
        CreateRecyclerView();
    }

    private void CreateRecyclerView()
    {
        try
        {
            mDataset = MyDatabase.MyDB().getVariationList(plannedId);
            mVariationList = findViewById(R.id.variationList);
            mVariationList.setHasFixedSize(true);
            mLayoutManagerCurrent = new LinearLayoutManager(this);
            mVariationList.setLayoutManager(mLayoutManagerCurrent);
            mVariationAdapter = new VariationAdapter(mDataset);
            mVariationList.setAdapter(mVariationAdapter);

            mVariationAdapter.setOnItemClickListener(new VariationAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, RecordPlannedVariation obj)
                {
                    Intent intent = new Intent(getApplicationContext(), activityPlanningVariationItem.class);
                    intent.putExtra("ACTIONTYPE", "EDIT");
                    intent.putExtra("VARIATIONID", obj.mVariationId);
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
            CreateRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }
}