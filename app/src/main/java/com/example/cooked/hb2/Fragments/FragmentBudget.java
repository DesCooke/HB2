package com.example.cooked.hb2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.R;

import java.util.List;

public class FragmentBudget extends Fragment {

    private RecyclerView recyclerView;
    private BudgetListSectioned mAdapter;

    public FragmentBudget() {
    }

    public void populate(RecordBudgetMonth recordBudgetMonth) {
        //set data and list adapter
        mAdapter = new BudgetListSectioned(this, recordBudgetMonth);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, People obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    public static FragmentBudget newInstance() {
        FragmentBudget fragment = new FragmentBudget();
        return fragment;
    }

    private void initComponent(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_budget, container, false);

        initComponent(root);

        return root;
    }
}