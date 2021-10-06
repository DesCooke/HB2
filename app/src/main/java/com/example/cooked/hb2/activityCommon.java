package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.TransactionUI.CommonAdapter;
import com.example.cooked.hb2.TransactionUI.PlannedAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class activityCommon extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    protected void SetupRecyclerView()
    {
        try
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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }


}
