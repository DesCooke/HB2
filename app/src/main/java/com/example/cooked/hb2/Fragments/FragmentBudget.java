package com.example.cooked.hb2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cooked.hb2.Adapters.BudgetListSectioned;
import com.example.cooked.hb2.Budget.RecordBudgetListItem;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.List;

public class FragmentBudget extends Fragment {

    private RecyclerView recyclerView;
    private BudgetListSectioned mAdapter;
    private RecordBudgetMonth _rbm;
    private NestedScrollView _nsv;
    private LinearLayout _ll;
    public MainActivity lMainActivity;

    public FragmentBudget() {
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
        mAdapter = new BudgetListSectioned(getActivity(), _rbm);
        mAdapter.lMainActivity=lMainActivity;
        recyclerView.setAdapter(mAdapter);
/*
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _nsv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerView.requestLayout();
        _nsv.requestLayout();
        _ll.requestLayout();
*/
        // on item list clicked
        mAdapter.setOnItemClickListener(new BudgetListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecordBudgetListItem obj, int position) {
                //Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    public static FragmentBudget newInstance() {
        FragmentBudget fragment = new FragmentBudget();
        return fragment;
    }

    private void initComponent(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        _nsv = root.findViewById(R.id.nested_scroll_view);
        _ll = root.findViewById(R.id.ll);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_budget, container, false);

        initComponent(root);

        populate();

        return root;
    }
}