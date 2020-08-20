package com.example.cooked.hb2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Adapters.VariationAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordBudgetProgress;
import com.example.cooked.hb2.Database.RecordPlannedVariation;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordBudgetClass;
import com.example.cooked.hb2.Records.RecordBudgetGroup;
import com.example.cooked.hb2.Records.RecordBudgetItem;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.TransactionUI.BudgetProgressAdapter;
import com.example.cooked.hb2.activityPlanningVariationItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Math.abs;

public class FragmentDashboard extends Fragment
{
    private View _root;
    private TextView _startingBalance;
    private TextView _monthlyIncome;
    private TextView _monthlyExpense;
    private TextView _amountLeft;
    private TextView _lblAmountLeft;
    private TextView _extraIncome;
    private TextView _extraExpense;
    private TextView _extraLeft;
    private TextView _lblExtraLeft;
    private TextView _finalBudgetBalanceThisMonth;
    private TextView _notes;
    private RecordBudgetMonth _rbm;

    public ArrayList<RecordBudgetProgress> mDataset;
    public RecyclerView mBudgetProgressList;
    public RecyclerView.LayoutManager mLayoutManagerCurrent;
    public BudgetProgressAdapter mBudgetProgressAdapter;

    public FragmentDashboard()
    {
    }

    public static FragmentDashboard newInstance()
    {
        FragmentDashboard fragment = new FragmentDashboard();
        return fragment;
    }

    private void initComponent(View root)
    {
        try
        {
            _startingBalance = root.findViewById(R.id.dbbs_StartingBalance);
            _monthlyIncome = root.findViewById(R.id.dbbs_MonthlyIncome);
            _monthlyExpense = root.findViewById(R.id.dbbs_MonthlyExpense);
            _amountLeft = root.findViewById(R.id.dbbs_AmountLeft);
            _lblAmountLeft = root.findViewById(R.id.dbbs_LblAmountLeft);
            _extraIncome = root.findViewById(R.id.dbbs_ExtraIncome);
            _extraExpense = root.findViewById(R.id.dbbs_ExtraExpense);
            _extraLeft = root.findViewById(R.id.dbbs_ExtraLeft);
            _lblExtraLeft = root.findViewById(R.id.dbbs_lblExtraLeft);
            _finalBudgetBalanceThisMonth = root.findViewById(R.id.dbbs_FinalBudgetBalanceThisMonth);
            _notes = root.findViewById(R.id.dbbs_Notes);
            _root=root;

        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void CreateRecyclerView()
    {
        try
        {
            if(_rbm==null)
                return;

            mDataset = new ArrayList<RecordBudgetProgress>();

            for(int i=0;i<_rbm.budgetClasses.size();i++)
            {
                RecordBudgetClass rbc=_rbm.budgetClasses.get(i);
                for(int j=0;j<rbc.budgetGroups.size();j++)
                {
                    RecordBudgetGroup rbg = rbc.budgetGroups.get(j);
                    if(rbg.Monitor)
                    {
                        RecordBudgetProgress rbp = new RecordBudgetProgress();
                        rbp.mTitle=rbg.budgetGroupName;
                        rbp.mTotalAmount=rbg.origTotal;
                        rbp.mLeftAmount=rbg.outstanding;
                        rbp.mSpentAmount=rbg.spent;

                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                        Date date = calendar.getTime();
                        int currentDay = calendar.get(Calendar.DATE);
                        int lPerc=0;
                        if(currentDay == 26)
                        {
                            lPerc=0;
                        }
                        else
                        {
                            if(currentDay==25)
                            {
                                lPerc=100;
                            }
                            else
                            {
                                if(currentDay>26)
                                {
                                    int lDays=currentDay-26;
                                    lPerc=(int)(lDays/30 * 100);
                                }
                                else
                                {
                                    int lDays=currentDay + 5;
                                    lPerc=(int)((lDays*100)/30);
                                }
                            }
                        }

                        rbp.mPercInMonth=lPerc;
                        float lFloat=rbg.spent / rbg.origTotal * 100.00f;
                        rbp.mSpentPerc=(int)lFloat;
                        mDataset.add(rbp);
                    }

                    for(int k=0;k<rbg.budgetItems.size();k++)
                    {
                        RecordBudgetItem rbi=rbg.budgetItems.get(k);
                        if(rbi.Monitor)
                        {
                            RecordBudgetProgress rbp = new RecordBudgetProgress();
                            rbp.mTitle=rbg.budgetGroupName + "/" + rbi.budgetItemName;
                            rbp.mTotalAmount=rbi.origTotal;
                            rbp.mLeftAmount=rbi.outstanding;
                            rbp.mSpentAmount=rbi.spent;

                            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                            Date date = calendar.getTime();
                            int currentDay = calendar.get(Calendar.DATE);
                            int lPerc=0;
                            if(currentDay == 26)
                            {
                                lPerc=0;
                            }
                            else
                            {
                                if(currentDay==25)
                                {
                                    lPerc=100;
                                }
                                else
                                {
                                    if(currentDay>26)
                                    {
                                        int lDays=currentDay-26;
                                        lPerc=(int)(lDays/30 * 100);
                                    }
                                    else
                                    {
                                        int lDays=currentDay + 5;
                                        lPerc=(int)((lDays*100)/30);
                                    }
                                }
                            }

                            rbp.mPercInMonth=lPerc;
                            float lFloat=rbi.spent / rbi.origTotal * 100.00f;
                            rbp.mSpentPerc=(int)lFloat;
                            mDataset.add(rbp);
                        }
                    }
                }
            }

            mBudgetProgressList = _root.findViewById(R.id.budgetProgressList);
            mBudgetProgressList.setHasFixedSize(true);
            mLayoutManagerCurrent = new LinearLayoutManager(_root.getContext());
            mBudgetProgressList.setLayoutManager(mLayoutManagerCurrent);
            mBudgetProgressAdapter = new BudgetProgressAdapter(mDataset);
            mBudgetProgressList.setAdapter(mBudgetProgressAdapter);
/*
            mBudgetProgressAdapter.setOnItemClickListener(new VariationAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, RecordPlannedVariation obj)
                {
                    Intent intent = new Intent(getApplicationContext(), activityPlanningVariationItem.class);
                    intent.putExtra("ACTIONTYPE", "EDIT");
                    intent.putExtra("VARIATIONID", obj.mVariationId);
                    startActivity(intent);
                }
            });
 */
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }


    public void refreshUI()
    {
        try
        {
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initComponent(root);

        populate();
        return root;
    }

    public void RefreshForm(RecordBudgetMonth rbm)
    {
        try
        {
            PopulateForm(rbm);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void PopulateForm(RecordBudgetMonth rbm)
    {
        try
        {
            _rbm = rbm;
            if (_monthlyIncome == null)
                return;
            populate();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void populate()
    {
        try
        {
            CreateRecyclerView();

            _monthlyIncome.setText(String.format(Locale.ENGLISH, "£%.2f", _rbm.monthlyIncome));
            _monthlyExpense.setText(String.format(Locale.ENGLISH, "£%.2f", _rbm.monthlyExpense));
            _amountLeft.setText(String.format(Locale.ENGLISH, "£%.2f", abs(_rbm.amountLeft)));
            if ((_rbm.amountLeft) > 0.00f)
            {
                _lblAmountLeft.setText("Monthly Underspend by...");
            } else
            {
                if ((_rbm.amountLeft) < 0.00f)
                {
                    _lblAmountLeft.setText("Monthly Overspend by...");
                } else
                {
                    {
                        _lblAmountLeft.setText("Monthly income matches expense");
                    }
                }
            }

            _extraIncome.setText(String.format(Locale.ENGLISH, "£%.2f", _rbm.extraIncome));
            _extraExpense.setText(String.format(Locale.ENGLISH, "£%.2f", _rbm.extraExpense));
            _extraLeft.setText(String.format(Locale.ENGLISH, "£%.2f", abs(_rbm.extraLeft)));
            if ((_rbm.extraLeft) > 0.00f)
            {
                _lblExtraLeft.setText("Extra Underspend by...");
            } else
            {
                if ((_rbm.extraLeft) < 0.00f)
                {
                    _lblExtraLeft.setText("Extra Overspend by...");
                } else
                {
                    {
                        _lblExtraLeft.setText("Extra income matches expense");
                    }
                }
            }

            _startingBalance.setText(String.format(Locale.ENGLISH, "£%.2f", _rbm.startingBalance));

            _finalBudgetBalanceThisMonth.setText(String.format(Locale.ENGLISH, "£%.2f", _rbm.finalBudgetBalanceThisMonth));

            _notes.setText(_rbm.notes);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }
}