package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;

import com.example.cooked.hb2.Adapters.TransactionListAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.TransactionUI.TransactionAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class activityTransactionList extends AppCompatActivity
{

    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView mTransactionList;
    private RecyclerView.LayoutManager mLayoutManagerCurrent;
    private TransactionListAdapter mTransactionListAdapter;
    private ArrayList<RecordTransaction> mDataset;
    private ArrayList<RecordTransaction> mOriginalDataset;
    private Boolean mTransactionListChanged;
    private Integer mBudgetYear;
    private Integer mBudgetMonth;
    private Integer mSubCategoryId;
    private String mCaption;
    private RecyclerView mBudgetList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            mTransactionListChanged = false;
            setContentView(R.layout.activity_transaction_list);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mCaption = getIntent().getStringExtra("CAPTION");
            mBudgetYear = getIntent().getIntExtra("BUDGETYEAR", 0);
            mBudgetMonth = getIntent().getIntExtra("BUDGETMONTH", 0);
            mSubCategoryId = getIntent().getIntExtra("SUBCATEGORYID", 0);

            ((TextView) toolbar.getChildAt(0)).setTextSize(12);
            setTitle(mCaption);

            mBudgetList = findViewById(R.id.budgetList);
            mBudgetList.setHasFixedSize(true);

            RecyclerView.LayoutManager mLayoutManagerButton = new LinearLayoutManager(MainActivity.context);
            mBudgetList.setLayoutManager(mLayoutManagerButton);

            setupRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    private void setupRecyclerView()
    {
        mDataset = MyDatabase.MyDB().getBudgetTrans(mBudgetYear, mBudgetMonth, mSubCategoryId);

        TransactionAdapter mTransactionAdapterCurrent = new TransactionAdapter(mDataset);
        mBudgetList.setAdapter(mTransactionAdapterCurrent);
        mTransactionAdapterCurrent.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                if (obj.TxType.compareTo("Planned") == 0)
                {
                    Intent intent = new Intent(MainActivity.context, activityPlanningItem.class);
                    intent.putExtra("ACTIONTYPE", "EDIT");
                    intent.putExtra("PlannedId", obj.TxSeqNo);
                    MainActivity.context.startActivity(intent);
                } else
                {
                    Intent intent = new Intent(MainActivity.context, activityTransactionItem.class);
                    intent.putExtra("ACTIONTYPE", "EDIT");
                    intent.putExtra("TxSeqNo", obj.TxSeqNo);
                    MainActivity.context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        MyLog.WriteLogMessage("activityTransaction:onResume:Starting");
        try
        {
            super.onResume();
            setupRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("activityTransaction:onResume:Ending");
    }

}