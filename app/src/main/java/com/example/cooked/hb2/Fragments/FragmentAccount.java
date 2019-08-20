package com.example.cooked.hb2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooked.hb2.R;

public class FragmentAccount extends Fragment {

    public FragmentAccount() {
    }

    public static FragmentAccount newInstance() {
        FragmentAccount fragment = new FragmentAccount();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        return root;
    }
}