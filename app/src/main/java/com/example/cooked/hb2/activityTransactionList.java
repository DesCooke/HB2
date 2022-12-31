package com.example.cooked.hb2;

import static com.example.cooked.hb2.Database.RecordSubCategory.mSCTAnnualExpense;
import static com.example.cooked.hb2.Database.RecordSubCategory.mSCTAnnualIncome;

import android.content.Intent;
import android.os.Bundle;

import com.example.cooked.hb2.Adapters.TransactionListAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategoryBudget;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.Tools;
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
    private Integer mCategoryId;
    private Integer mSubCategoryId;
    private String mCaption;
    private RecyclerView mBudgetList;
    public TextView mTextInfo1;
    public TextView mTextInfo2;
    public TextView mTextInfo3;
    public String mLine1;
    public String mLine2;
    public String mLine3;
    public TextView mTextTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            mTransactionListChanged = false;
            setContentView(R.layout.activity_transaction_list);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            mTextInfo1 = (TextView) findViewById(R.id.textInfo1);
            mTextInfo2 = (TextView) findViewById(R.id.textInfo2);
            mTextInfo3 = (TextView) findViewById(R.id.textInfo3);
            mTextTotal = (TextView) findViewById(R.id.txtTotal);

            setSupportActionBar(toolbar);

            mCaption = getIntent().getStringExtra("CAPTION");
            mBudgetYear = getIntent().getIntExtra("BUDGETYEAR", 0);
            mBudgetMonth = getIntent().getIntExtra("BUDGETMONTH", 0);
            mCategoryId = getIntent().getIntExtra("CATEGORYID", 0);
            mSubCategoryId = getIntent().getIntExtra("SUBCATEGORYID", 0);
            mLine1 = getIntent().getStringExtra("LINE1");
            mLine2 = getIntent().getStringExtra("LINE2");
            mLine3 = getIntent().getStringExtra("LINE3");

            setTitle(mCaption);

            HandleTextLine(mLine1, mTextInfo1);
            HandleTextLine(mLine2, mTextInfo2);
            HandleTextLine(mLine3, mTextInfo3);

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

    private void HandleTextLine(String line, TextView textView)
    {
        if(line == null || line.length()==0)
        {
            textView.setVisibility(View.GONE);
        }
        else
        {
            textView.setText(line);
            textView.setVisibility(View.VISIBLE);
        }
    }
    private void setupRecyclerView()
    {
        mDataset = null;
        if(mSubCategoryId > 0 && mBudgetYear > 0)
        {
            RecordSubCategory rsc = MyDatabase.MyDB().getSubCategory(mSubCategoryId);
            if(rsc.SubCategoryType == mSCTAnnualExpense || rsc.SubCategoryType == mSCTAnnualIncome)
                mDataset = MyDatabase.MyDB().getAnnualBudgetTrans(mBudgetYear, mBudgetMonth, mCategoryId, mSubCategoryId, mTextInfo3);
        }
        if(mDataset==null || mDataset.size()==0)
            mDataset = MyDatabase.MyDB().getBudgetTrans(mBudgetYear, mBudgetMonth, mCategoryId, mSubCategoryId);

        Float mBudgetLeft=0.00f;
        Float mTransactionTotal=0.00f;
        Boolean mAtleastOneBudgetDisplayed=false;
        for(int i=0;i<mDataset.size();i++)
        {
            RecordTransaction rec=mDataset.get(i);
            if(rec.TxType.compareTo("Planned") == 0)
            {
                mBudgetLeft += rec.TxAmount;
                mAtleastOneBudgetDisplayed=true;
            }
            else
            {
                mTransactionTotal += rec.TxAmount;
            }
        }
        if(mCategoryId>0)
        {
            RecordCategoryBudget rec1=MyDatabase.MyDB().getCategoryBudget(mCategoryId, mBudgetMonth, mBudgetYear);
            if(rec1!=null)
            {
                mBudgetLeft = rec1.BudgetAmount - mTransactionTotal;
                mAtleastOneBudgetDisplayed=true;
            }
        }
        if(mAtleastOneBudgetDisplayed)
        {
            mTextTotal.setText("Tx Total " + Tools.moneyFormat(mTransactionTotal*-1) +
                ", Budget Left " + Tools.moneyFormat(mBudgetLeft*-1));
        }
        else
        {
            mTextTotal.setText("Tx Total " + Tools.moneyFormat(mTransactionTotal*-1));
        }

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