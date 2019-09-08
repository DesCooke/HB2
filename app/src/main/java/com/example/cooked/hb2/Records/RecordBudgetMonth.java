package com.example.cooked.hb2.Records;

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
    public ArrayList<RecordAccount> accounts;

    public RecordBudgetMonth()
    {
        startingBalance=0.00f;
        monthlyIncome=0.00f;
        monthlyExpense=0.00f;
        amountLeft=0.00f;
        extraIncome=0.00f;
        extraExpense=0.00f;
        extraLeft=0.00f;
        finalBudgetBalanceThisMonth=0.00f;
        budgetClasses = new ArrayList<RecordBudgetClass>();
        notes="";
        accounts = new ArrayList<RecordAccount>();
    }

    public RecordAccount FindAccount(String acSortCode, String acAccountNumber)
    {
        for(int i=0;i<accounts.size();i++)
        {
            RecordAccount ra = accounts.get(i);
            if(ra.AcSortCode.compareTo(acSortCode)==0 &&
               ra.AcAccountNumber.compareTo(acAccountNumber)==0)
            {
                return(ra);
            }
        }
        return(null);
    }

    private void RefreshOutstanding()
    {
        float ctotal=0.00f;
        float cspent=0.00f;
        for(int i=0;i<budgetClasses.size();i++)
        {
            float gtotal=0.00f;
            float gspent=0.00f;

            RecordBudgetClass rbc=budgetClasses.get(i);
            for(int j=0;j<rbc.budgetGroups.size();j++)
            {
                float itotal=0.00f;
                float ispent=0.00f;

                RecordBudgetGroup rbg=rbc.budgetGroups.get(j);
                for(int k=0;k<rbg.budgetItems.size();k++)
                {
                    RecordBudgetItem rbi=rbg.budgetItems.get(k);
                    rbi.outstanding=rbi.total-rbi.spent;
                    itotal+=rbi.total;
                    ispent+=rbi.spent;
                }
                if(!rbg.groupedBudget)
                    rbg.total=itotal;
                rbg.spent=ispent;
                rbg.outstanding=rbg.total-rbg.spent;
                gtotal+=rbg.total;
                gspent+=rbg.spent;
            }
            rbc.total=gtotal;
            rbc.spent=gspent;
            rbc.outstanding=rbc.total-rbc.spent;
            ctotal+=rbc.total;
            cspent+=rbc.spent;
        }
    }

    public void RefreshTotals()
    {
        Float lTotal = 0.00f;
        Float lSpent = 0.00f;
        Float lOutstanding = 0.00f;

        Float l_BCIncome = 0.00f;
        Float l_BCExpense = 0.00f;
        Float l_BCEIncome = 0.00f;
        Float l_BCEExpense = 0.00f;

        RefreshOutstanding();

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

