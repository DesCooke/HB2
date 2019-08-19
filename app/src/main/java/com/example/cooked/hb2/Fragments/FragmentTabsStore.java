package com.example.cooked.hb2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooked.hb2.R;

public class FragmentTabsStore extends Fragment {

    public FragmentTabsStore() {
    }

    public static FragmentTabsStore newInstance() {
        FragmentTabsStore fragment = new FragmentTabsStore();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tabs_store, container, false);

        return root;
    }
}