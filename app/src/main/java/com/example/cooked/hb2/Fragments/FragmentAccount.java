package com.example.cooked.hb2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooked.hb2.Adapters.TransactionListAdapter;
import com.example.cooked.hb2.ImageAdapter;
import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.activityTransactionItem;

import java.util.ArrayList;

public class FragmentAccount extends Fragment {
    private RecyclerView recyclerView;
    private TransactionListAdapter mAdapter;
    private ArrayList<RecordTransaction> _rta;
    public MainActivity lMainActivity;
    public String AcSortCode;
    public String AcAccountNumber;
    public String AcDescription;
    private FloatingActionButton fab;

    public FragmentAccount() {
    }

    public void SetAccount(String acSortCode, String acAccountNumber, String acDescription)
    {
        AcSortCode=acSortCode;
        AcAccountNumber=acAccountNumber;
        AcDescription=acDescription;
    }

    public void SetMainActivity(MainActivity ma)
    {
        lMainActivity = ma;
    }

    public void RefreshForm(ArrayList<RecordTransaction> rta)
    {
        _rta=rta;
        //set data and list adapter
        if (recyclerView == null)
            return;
        populate();
    }
    public void PopulateForm(ArrayList<RecordTransaction> rta)
    {
        _rta=rta;
        //set data and list adapter
        if (recyclerView == null)
            return;
        populate();
    }

    public void refreshUI()
    {
        if(mAdapter!=null)
            mAdapter.refreshUI();
    }

    private void populate()
    {
        mAdapter = new TransactionListAdapter(getActivity(), _rta);
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

        recyclerView.setAdapter(mAdapter);
    }

    public static FragmentAccount newInstance() {
        FragmentAccount fragment = new FragmentAccount();
        return fragment;
    }

    private void initComponent(View root)
    {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.context, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                intent.putExtra("SORTCODE", AcSortCode);
                intent.putExtra("ACCOUNTNUMBER", AcAccountNumber);
                intent.putExtra("DESCRIPTION", AcDescription);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        initComponent(root);

        populate();

        return root;
    }
}