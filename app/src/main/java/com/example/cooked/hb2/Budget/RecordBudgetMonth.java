package com.example.cooked.hb2.Budget;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

public class RecordBudgetMonth
{
    public Float startingBalance;
    public Float monthlyIncome;
    public Float monthlyExpense;
    public Float amountLeft;
    public Float extraIncome;
    public Float extraExpense;
    public Float extraLeft;
    public Float finalBudgetBalanceThisMonth;
    public ArrayList<RecordBudgetClass> budgetClasses;
    public String notes;

    public void RefreshTotals()
    {
        Float lTotal = 0.00f;
        Float lSpent = 0.00f;
        Float lOutstanding = 0.00f;

        Float l_BCIncome = 0.00f;
        Float l_BCExpense = 0.00f;
        Float l_BCEIncome = 0.00f;
        Float l_BCEExpense = 0.00f;
        for (int i = 0; i < budgetClasses.size(); i++)
        {
            RecordBudgetClass rbc = budgetClasses.get(i);

            if (rbc.budgetClassName.compareTo(MainActivity.context.getString(R.string.budget_header_monthly_income)) == 0)
            {
                l_BCIncome = rbc.total;
            }
            if (rbc.budgetClassName.compareTo(MainActivity.context.getString(R.string.budget_header_monthly_expenses)) == 0)
            {
                lTotal += (rbc.total*-1);
                lSpent += (rbc.spent*-1);
                lOutstanding += (rbc.outstanding*-1);
                l_BCExpense = rbc.total * -1;
            }
            if (rbc.budgetClassName.compareTo(MainActivity.context.getString(R.string.budget_header_extra_income)) == 0)
            {
                l_BCEIncome = rbc.total;
            }
            if (rbc.budgetClassName.compareTo(MainActivity.context.getString(R.string.budget_header_extra_expenses)) == 0)
            {
                lTotal += (rbc.total*-1);
                lSpent += (rbc.spent*-1);
                lOutstanding += (rbc.outstanding*-1);
                l_BCEExpense = rbc.total * -1;
            }


        }

        monthlyIncome = l_BCIncome;
        monthlyExpense = l_BCExpense;
        amountLeft = l_BCIncome-l_BCExpense;

        extraIncome = l_BCEIncome;
        extraExpense = l_BCEExpense;
        extraLeft = l_BCEIncome - l_BCEExpense;
    }
}

