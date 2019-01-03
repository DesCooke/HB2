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
import com.example.cooked.hb2.Database.RecordAccount;
import com.example.cooked.hb2.TransactionUI.AccountAdapter;
import com.example.cooked.hb2.TransactionUI.CommonAdapter;

import java.util.ArrayList;

public class activityAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Accounts");

        SetupRecyclerView();
    }

    protected void SetupRecyclerView()
    {
        ArrayList<RecordAccount> mDataset = MyDatabase.MyDB().getAccountList();
        RecyclerView mAccountList = findViewById(R.id.accountList);
        mAccountList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerCurrent = new LinearLayoutManager(this);
        mAccountList.setLayoutManager(mLayoutManagerCurrent);
        AccountAdapter mAccountAdapter = new AccountAdapter(mDataset);
        mAccountList.setAdapter(mAccountAdapter);
        mAccountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordAccount obj)
            {
                /*
                Intent intent = new Intent(getApplicationContext(), activityCommonItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);*/
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
