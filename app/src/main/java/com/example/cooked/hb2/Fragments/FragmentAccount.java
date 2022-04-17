package com.example.cooked.hb2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hb2.Adapters.TransactionListAdapter;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.activityTransactionItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentAccount extends Fragment
{
    private RecyclerView recyclerView;
    private TransactionListAdapter mAdapter;
    private ArrayList<RecordTransaction> _rta;
    private RecordBudgetMonth _rbm;
    public MainActivity lMainActivity;
    public String AcSortCode;
    public String AcAccountNumber;
    public String AcDescription;
    private FloatingActionButton fab;
    private boolean rotate = false;
    private LinearLayout lyAddMenu;
    private LinearLayout lyItems;
    private ArrayList<RecordCommon> _recordCommonList;

    public FragmentAccount()
    {
    }

    public void RefreshForm(ArrayList<RecordTransaction> rta, RecordBudgetMonth rbm)
    {
        try
        {
            _rta = rta;
            _rbm = rbm;

            //set data and list adapter
            if (recyclerView == null)
                return;
            UpdateRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void SetAccount(String acSortCode, String acAccountNumber, String acDescription)
    {
        try
        {
            AcSortCode = acSortCode;
            AcAccountNumber = acAccountNumber;
            AcDescription = acDescription;
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public static FragmentAccount newInstance()
    {
        FragmentAccount fragment = new FragmentAccount();
        return fragment;
    }

    private void initComponent(View root, LayoutInflater inflater)
    {
        try
        {
            _recordCommonList = _rbm.mCommonDataset;

            recyclerView = root.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);

            lyAddMenu = root.findViewById(R.id.lyAddMenu);
            lyItems = root.findViewById(R.id.lyItems);
            while (lyItems.getChildCount() > 0)
            {
                lyItems.removeView(lyItems.getChildAt(0));
                lyItems.invalidate();
            }

            for (int i = 0; i < _recordCommonList.size(); i++)
            {
                View cv = inflater.inflate(R.layout.card_menu_item, lyItems, false);
                TextView tv = cv.findViewById(R.id.tvCaption);
                tv.setText(_recordCommonList.get(i).TxDescription);
                cv.setTag(_recordCommonList.get(i));
                cv.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        onSelectAddItem(v);
                    }
                });
                lyItems.addView(cv, 0);
            }

            View cv = inflater.inflate(R.layout.card_menu_item, lyItems, false);
            TextView tv = cv.findViewById(R.id.tvCaption);
            tv.setText("<Blank>");
            cv.setTag(null);
            cv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onSelectAddItem(v);
                }
            });
            lyItems.addView(cv, 0);

            lyItems.invalidate();
            for (int i = 0; i < lyItems.getChildCount(); i++)
            {
                Tools.showOut(lyItems.getChildAt(i));
            }


            fab = root.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    toggleFabMode();
                }
            });

            CreateRecyclerView();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void CreateRecyclerView()
    {

        mAdapter = new TransactionListAdapter(getActivity(), _rta, _rbm);
        mAdapter.setOnItemClickListener(new TransactionListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecordTransaction obj)
            {
                obj.CheckForChange = true;
                Intent intent = new Intent(getActivity(), activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "EDIT");
                intent.putExtra("TxSeqNo", obj.TxSeqNo);
                startActivity(intent);
            }
        });

        if(AcDescription.compareTo("Annual Bills")==0)
        {
            fab.setVisibility(View.GONE);
        }
        else
        {
            fab.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(mAdapter);
    }

    private void UpdateRecyclerView()
    {
        mAdapter.UpdateList(_rta);
    }

    private void onSelectAddItem(View view)
    {
        try
        {
            toggleFabMode();
            RecordCommon rc = (RecordCommon) view.getTag();
            if (rc == null)
            {
                Intent intent = new Intent(MainActivity.context, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                intent.putExtra("SORTCODE", AcSortCode);
                intent.putExtra("ACCOUNTNUMBER", AcAccountNumber);
                intent.putExtra("DESCRIPTION", AcDescription);
                startActivity(intent);
            } else
            {
                Intent intent = new Intent(MainActivity.context, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                intent.putExtra("SORTCODE", AcSortCode);
                intent.putExtra("ACCOUNTNUMBER", AcAccountNumber);
                intent.putExtra("DESCRIPTION", AcDescription);
                intent.putExtra("TEMPLATEDESC", rc.TxDescription);

                startActivity(intent);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void toggleFabMode()
    {
        try
        {
            if (rotate == false)
            {
                Tools.rotateFab(fab, true);
                for (int i = 0; i < lyItems.getChildCount(); i++)
                {
                    Tools.showIn(lyItems.getChildAt(i));
                }
                rotate = true;
            } else
            {
                Tools.rotateFab(fab, false);
                for (int i = 0; i < lyItems.getChildCount(); i++)
                {
                    Tools.showOut(lyItems.getChildAt(i));
                }
                rotate = false;
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        initComponent(root, inflater);

        return root;
    }
}