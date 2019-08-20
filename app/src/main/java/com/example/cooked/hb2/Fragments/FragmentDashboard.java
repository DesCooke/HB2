package com.example.cooked.hb2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.R;

import java.util.Locale;

import static java.lang.Math.abs;

public class FragmentDashboard extends Fragment {

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

    public FragmentDashboard() {
    }

    public static FragmentDashboard newInstance() {
        FragmentDashboard fragment = new FragmentDashboard();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

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

        return root;
    }

    public void PopulateForm(RecordBudgetMonth rbm)
    {
        if(_monthlyIncome==null)
            return;

        _monthlyIncome.setText(String.format(Locale.ENGLISH, "£%.2f", rbm.monthlyIncome));
        _monthlyExpense.setText(String.format(Locale.ENGLISH, "£%.2f", rbm.monthlyExpense));
        _amountLeft.setText(String.format(Locale.ENGLISH, "£%.2f", abs(rbm.amountLeft)));
        if( (rbm.amountLeft) > 0.00f)
        {
            _lblAmountLeft.setText("Monthly Underspend by...");
        }
        else
        {
            if( (rbm.amountLeft) < 0.00f)
            {
                _lblAmountLeft.setText("Monthly Overspend by...");
            }
            else
            {
                {
                    _lblAmountLeft.setText("Monthly income matches expense");
                }
            }
        }

        _extraIncome.setText(String.format(Locale.ENGLISH, "£%.2f", rbm.extraIncome));
        _extraExpense.setText(String.format(Locale.ENGLISH, "£%.2f", rbm.extraExpense));
        _extraLeft.setText(String.format(Locale.ENGLISH, "£%.2f", abs(rbm.extraLeft)));
        if( (rbm.extraLeft) > 0.00f)
        {
            _lblExtraLeft.setText("Extra Underspend by...");
        }
        else
        {
            if( (rbm.extraLeft) < 0.00f)
            {
                _lblExtraLeft.setText("Extra Overspend by...");
            }
            else
            {
                {
                    _lblExtraLeft.setText("Extra income matches expense");
                }
            }
        }

        _startingBalance.setText(String.format(Locale.ENGLISH, "£%.2f", rbm.startingBalance));

        _finalBudgetBalanceThisMonth.setText(String.format(Locale.ENGLISH, "£%.2f",rbm.finalBudgetBalanceThisMonth));

    }
}