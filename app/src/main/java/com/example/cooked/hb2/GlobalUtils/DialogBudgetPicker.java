package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.Calendar;
import java.util.Date;

import static com.example.cooked.hb2.MainActivity.context;


public class DialogBudgetPicker extends Dialog
{
    public TextView txtStartDate;
    public TextInputLayout tilStartDate;
    private DateUtils dateUtils;
    private DatePicker datePicker;
    private boolean setInitialDate;
    private Date initialDate;
    public int BudgetYear;
    public int BudgetMonth;
    public TextView tvBudgetYearMonth;
    public TextView tvBudgetMonth;
    public ImageButton btnLeft;
    public ImageButton btnRight;
    public TextView tvBudgetDates;
    public MainActivity MyMainActivity;
    public Button btnOk;

    public DialogBudgetPicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogBudgetPicker::" + argFunction, argMessage);
    }

    public void SetUI()
    {
        tvBudgetYearMonth.setText(DateUtils.budgetMonthYear(BudgetMonth,BudgetYear));
        tvBudgetMonth.setText(DateUtils.getMonthLong(BudgetMonth,BudgetYear));
        tvBudgetDates.setText(DateUtils.BudgetStartAsStr(BudgetMonth, BudgetYear) + " -> " +
                DateUtils.BudgetEndAsStr(BudgetMonth, BudgetYear));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_budget_picker);

            tvBudgetYearMonth = findViewById(R.id.tvBudgetYearMonth);
            tvBudgetMonth = findViewById(R.id.tvBudgetMonth);
            tvBudgetDates = findViewById(R.id.tvBudgetDates);
            btnOk = findViewById(R.id.btnOk);

            btnLeft = findViewById(R.id.btnLeft);
            btnRight = findViewById(R.id.btnRight);

            btnLeft.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BudgetMonth--;
                    if(BudgetMonth<1)
                    {
                        BudgetYear--;
                        BudgetMonth=12;
                    }
                    SetUI();
                }
            });

            btnRight.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BudgetMonth++;
                    if(BudgetMonth>12)
                    {
                        BudgetYear++;
                        BudgetMonth=1;
                    }
                    SetUI();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MyMainActivity.RecreateUI(BudgetYear, BudgetMonth);
                    dismiss();
                }
            });

            SetUI();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }
}
