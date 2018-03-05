package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.TransactionUI.TransactionAdapter;
import com.example.cooked.hb2.activityTransactionItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DialogTransactionList extends Dialog implements View.OnClickListener
{
    public TextView edtPlannedType;
    public MyInt mPlannedType;
    public Integer budgetYear;
    public Integer budgetMonth;
    public Integer subCategoryId;
    public DialogTransactionList(Activity a)
    {
        super(a);
    }
    private RecyclerView mBudgetList;
    public ArrayList<RecordTransaction> pList;
    private RecyclerView.LayoutManager mLayoutManagerButton;
    private TransactionAdapter mTransactionAdapterCurrent;

    public void GetTrans()
    {
        pList = MyDatabase.MyDB().getBudgetTrans(budgetYear, budgetMonth, subCategoryId);
    }
    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogTransactionList::" + argFunction, argMessage);
    }

    public void setupRecyclerView()
    {
        mTransactionAdapterCurrent = new TransactionAdapter(pList);
        mBudgetList.setAdapter(mTransactionAdapterCurrent);
        mTransactionAdapterCurrent.setOnItemClickListener(new TransactionAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                Intent intent = new Intent(MainActivity.context, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                MainActivity.context.startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.transactionlist_picker);

            mBudgetList = findViewById(R.id.budgetList);
            mBudgetList.setHasFixedSize(true);

            mLayoutManagerButton = new LinearLayoutManager(MainActivity.context);
            mBudgetList.setLayoutManager(mLayoutManagerButton);

            setupRecyclerView();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            RadioButton radYearly =findViewById(R.id.radYearly);
            RadioButton radMonthly =findViewById(R.id.radMonthly);
            RadioButton radWeekly =findViewById(R.id.radWeekly);
            mPlannedType.Value = RecordPlanned.mPTOneOff;
            if(radYearly.isChecked())
                mPlannedType.Value = RecordPlanned.mPTYearly;
            if(radMonthly.isChecked())
                mPlannedType.Value = RecordPlanned.mPTMonthly;
            if(radWeekly.isChecked())
                mPlannedType.Value = RecordPlanned.mPTWeekly;
            edtPlannedType.setText(RecordPlanned.mPlannedTypes[mPlannedType.Value]);

            dismiss();
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
            return;
        }
    }

}
