package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.Window;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.TransactionUI.TransactionAdapter;
import com.example.cooked.hb2.activityPlanningItem;
import com.example.cooked.hb2.activityTransactionItem;

import java.util.ArrayList;


public class DialogTransactionList extends Dialog implements View.OnClickListener
{
    public Integer budgetYear;
    public Integer budgetMonth;
    public Integer subCategoryId;

    public DialogTransactionList(Activity a)
    {
        super(a);
    }

    private RecyclerView mBudgetList;
    private ArrayList<RecordTransaction> pList;

    public void GetTrans()
    {
        pList = MyDatabase.MyDB().getBudgetTrans(budgetYear, budgetMonth, subCategoryId);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogTransactionList::" + argFunction, argMessage);
    }

    private void setupRecyclerView()
    {
        TransactionAdapter mTransactionAdapterCurrent = new TransactionAdapter(pList);
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.transactionlist);

        mBudgetList = findViewById(R.id.budgetList);
        mBudgetList.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManagerButton = new LinearLayoutManager(MainActivity.context);
        mBudgetList.setLayoutManager(mLayoutManagerButton);

        setupRecyclerView();
    }

    @Override
    public void onClick(View v)
    {
        dismiss();
    }

}
