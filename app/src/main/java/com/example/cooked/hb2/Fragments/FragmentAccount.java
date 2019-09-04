package com.example.cooked.hb2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooked.hb2.Adapters.TransactionListAdapter;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

public class FragmentAccount extends Fragment {
    private RecyclerView recyclerView;
    private TransactionListAdapter mAdapter;
    private ArrayList<RecordTransaction> _rta;
    public MainActivity lMainActivity;
    public String AcSortCode;
    public String AcAccountNumber;

    public FragmentAccount() {
    }

    public void SetAccount(String acSortCode, String acAccountNumber)
    {
        AcSortCode=acSortCode;
        AcAccountNumber=acAccountNumber;
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
    private void populate()
    {
        mAdapter = new TransactionListAdapter(getActivity(), _rta);
        recyclerView.setAdapter(mAdapter);
    }



    public static FragmentAccount newInstance() {
        FragmentAccount fragment = new FragmentAccount();
        return fragment;
    }

    private void initComponent(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        initComponent(root);

        populate();

        return root;
    }
}