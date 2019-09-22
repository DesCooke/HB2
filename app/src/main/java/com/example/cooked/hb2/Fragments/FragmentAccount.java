package com.example.cooked.hb2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hb2.Adapters.TransactionListAdapter;
import com.example.cooked.hb2.GlobalUtils.Tools;
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
    public View back_drop;
    private boolean rotate=false;
    private LinearLayout lyAddMenu;
    private TextView tv1;
    private CardView cv1;
    private TextView tv2;
    private CardView cv2;
    private TextView tv3;
    private CardView cv3;

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



        lyAddMenu = root.findViewById(R.id.lyAddMenu);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tv1=new TextView(getContext());
        tv1.setLayoutParams(params);
        tv1.setText("Add Posh Lunch");
        tv1.setTextAppearance(R.style.TextAppearance_AppCompat_Display1);
        tv1.setPadding(8, 8, 8, 8);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cv1 = new CardView(getContext());
        cv1.setLayoutParams(params);
        cv1.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cv1.setRadius(3.00f);
        cv1.setCardElevation(2.00f);
        cv1.addView(tv1);
        cv1.setPadding(8, 8, 8, 8);
        //cv.setContentPadding(R.dimen.spacing_medium, R.dimen.spacing_medium, R.dimen.spacing_medium, R.dimen.spacing_medium);

        lyAddMenu.addView(cv1, 0, params);



        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tv2=new TextView(getContext());
        tv2.setLayoutParams(params);
        tv2.setText("Add Daily Lunch");
        tv2.setTextAppearance(R.style.TextAppearance_AppCompat_Display1);
        tv2.setPadding(8, 8, 8, 8);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cv2 = new CardView(getContext());
        cv2.setLayoutParams(params);
        cv2.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cv2.setRadius(3.00f);
        cv2.setCardElevation(2.00f);
        cv2.addView(tv2);
        cv2.setPadding(8, 8, 8, 8);
        //cv.setContentPadding(R.dimen.spacing_medium, R.dimen.spacing_medium, R.dimen.spacing_medium, R.dimen.spacing_medium);

        lyAddMenu.addView(cv2, 0, params);


        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tv3=new TextView(getContext());
        tv3.setLayoutParams(params);
        tv3.setText("Add");
        tv3.setTextAppearance(R.style.TextAppearance_AppCompat_Display1);
        tv3.setPadding(8, 8, 8, 8);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cv3 = new CardView(getContext());
        cv3.setLayoutParams(params);
        cv3.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cv3.setRadius(3.00f);
        cv3.setCardElevation(2.00f);
        cv3.setPadding(8, 8, 8, 8);
        cv3.addView(tv3);
        //cv.setContentPadding(R.dimen.spacing_medium, R.dimen.spacing_medium, R.dimen.spacing_medium, R.dimen.spacing_medium);

        lyAddMenu.addView(cv3, 0, params);






        Tools.initShowOut(cv1);
        Tools.initShowOut(cv2);
        Tools.initShowOut(cv3);

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*
                Intent intent = new Intent(MainActivity.context, activityTransactionItem.class);
                intent.putExtra("ACTIONTYPE", "ADD");
                intent.putExtra("SORTCODE", AcSortCode);
                intent.putExtra("ACCOUNTNUMBER", AcAccountNumber);
                intent.putExtra("DESCRIPTION", AcDescription);
                startActivity(intent);

                 */
                toggleFabMode(view);
            }
        });
        back_drop.setVisibility(View.GONE);
        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab);
            }
        });
    }

    private void toggleFabMode(View v) {
        rotate = Tools.rotateFab(v, !rotate);
        if (rotate) {
            Tools.showIn(cv1);
            Tools.showIn(cv2);
            Tools.showIn(cv3);
//            ViewAnimation.showIn(lyt_mic);
  //          ViewAnimation.showIn(lyt_call);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            Tools.showOut(cv1);
            Tools.showOut(cv2);
            Tools.showOut(cv3);
    //        ViewAnimation.showOut(lyt_mic);
      //      ViewAnimation.showOut(lyt_call);
            back_drop.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        initComponent(root);

        populate();

        return root;
    }
}