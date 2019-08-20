package com.example.cooked.hb2.Budget;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.MainActivity;

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
    public ArrayList<RecordBudgetGroup> budgetGroups;
}