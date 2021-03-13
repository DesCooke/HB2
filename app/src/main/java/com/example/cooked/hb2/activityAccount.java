package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.TransactionUI.AccountAdapter;
import com.example.cooked.hb2.helper.DragItemTouchHelper;

import java.util.ArrayList;

public class activityAccount extends AppCompatActivity
{
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView mAccountList;
    private RecyclerView.LayoutManager mLayoutManagerCurrent;
    private AccountAdapter mAccountAdapter;
    private ArrayList<RecordAccount> mDataset;
    private ArrayList<RecordAccount> mOriginalDataset;
    private Boolean mAccountListChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            mAccountListChanged = false;
            setContentView(R.layout.activity_account_list);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mOriginalDataset = MyDatabase.MyDB().getAccountList();

            setTitle("Accounts");

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
            mDataset = MyDatabase.MyDB().getAccountList();

            mAccountList = findViewById(R.id.accountList);
            mAccountList.setHasFixedSize(true);

            mLayoutManagerCurrent = new LinearLayoutManager(this);
            mAccountList.setLayoutManager(mLayoutManagerCurrent);

            mAccountAdapter = new AccountAdapter(this, mDataset);
            mAccountList.setAdapter(mAccountAdapter);

            mAccountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, RecordAccount obj)
                {
                    Intent intent = new Intent(getApplicationContext(), activityAccountItem.class);
                    intent.putExtra("AcSeqNo", obj.AcSeqNo);
                    //startActivity(intent);
                    int lRequestCode = getResources().getInteger(R.integer.account_item_return);
                    startActivityForResult(intent, lRequestCode);
                }
            });

            mAccountAdapter.setDragListener(new AccountAdapter.OnStartDragListener() {
                @Override
                public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                    mItemTouchHelper.startDrag(viewHolder);
                }
            });

            ItemTouchHelper.Callback callback = new DragItemTouchHelper(mAccountAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mAccountList);

        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MyLog.WriteLogMessage("ActivityAccount:onCreateOptionsMenu:Starting");
        try
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.activity_account_menu, menu);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("ActivityAccount:onCreateOptionsMenu:Ending");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        MyLog.WriteLogMessage("ActivityAccount:onOptionsItemSelected:Starting");
        try
        {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.unhideAllAccounts)
            {
                MyDatabase.MyDB().unhideAllAccounts();
                SetupRecyclerView();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("ActivityAccount:onOptionsItemSelected:Ending");
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        if(!mAccountListChanged)
        {
            if(mOriginalDataset.size()!=mDataset.size())
            {
                mAccountListChanged=true;
            }
            else
            {
                for(int i=0;i<mOriginalDataset.size();i++)
                {
                    if(mOriginalDataset.get(i).IsSameAs(mDataset.get(i)))
                        continue;
                    mAccountListChanged=true;
                    break;
                }
            }
        }
        Intent intent = new Intent();
        intent.putExtra("ACCOUNTLISTCHANGED", mAccountListChanged);
        setResult(RESULT_OK, intent);
        finish();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getResources().getInteger(R.integer.account_item_return))
        {
            if(resultCode == RESULT_OK)
            {
                boolean lAccountListChanged = data.getBooleanExtra("ACCOUNTLISTCHANGED",false);
                if(lAccountListChanged)
                    mAccountListChanged=true;
            }
        }
    }
}
