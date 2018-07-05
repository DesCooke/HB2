package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.TransactionUI.CommonAdapter;
import com.example.cooked.hb2.TransactionUI.PlannedAdapter;

import java.util.ArrayList;

public class activityCommon extends AppCompatActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        setTitle("Common Transactions");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), activityCommonItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                startActivity(intent);
            }
        });

        SetupRecyclerView();
    }

    protected void SetupRecyclerView()
    {
        ArrayList<RecordCommon> mDataset = MyDatabase.MyDB().getCommonTransactionList();
        RecyclerView mCommonList = findViewById(R.id.commonList);
        mCommonList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerCurrent = new LinearLayoutManager(this);
        mCommonList.setLayoutManager(mLayoutManagerCurrent);
        CommonAdapter mCommonAdapter = new CommonAdapter(mDataset);
        mCommonList.setAdapter(mCommonAdapter);
        mCommonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordCommon obj)
            {
                Intent intent = new Intent(getApplicationContext(), activityCommonItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
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
