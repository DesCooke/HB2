package com.example.cooked.hb2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooked.hb2.Adapters.BudgetListAdapter;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

public class FragmentAccount extends Fragment {
    private RecyclerView recyclerView;
    private TransactionListAdapter mAdapter;
    private RecordBudgetMonth _rbm;
    public MainActivity lMainActivity;

    public FragmentAccount() {
    }

    public void SetMainActivity(MainActivity ma)
    {
        lMainActivity = ma;
    }

    public void RefreshForm(RecordBudgetMonth recordBudgetMonth)
    {
        _rbm=recordBudgetMonth;
        //set data and list adapter
        if (recyclerView == null)
            return;
        populate();
    }
    public void PopulateForm(RecordBudgetMonth recordBudgetMonth)
    {
        _rbm=recordBudgetMonth;
        //set data and list adapter
        if (recyclerView == null)
            return;
        populate();
    }
    private void populate()
    {
        mAdapter = new TransactionListAdapter(getActivity(), _rbm);
        recyclerView.setAdapter(mAdapter);
    }



    public static FragmentBudget newInstance() {
        FragmentBudget fragment = new FragmentBudget();
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
    }}