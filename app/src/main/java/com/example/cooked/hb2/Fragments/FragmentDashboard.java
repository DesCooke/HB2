package com.example.cooked.hb2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.R;

import java.util.Locale;

import static java.lang.Math.abs;

public class FragmentDashboard extends Fragment
{

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