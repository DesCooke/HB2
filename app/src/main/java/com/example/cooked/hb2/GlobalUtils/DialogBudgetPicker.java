package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.cooked.hb2.R;

import java.util.Calendar;
import java.util.Date;


public class DialogBudgetPicker extends Dialog
{
    public TextView txtStartDate;
    public TextInputLayout tilStartDate;
    private DateUtils dateUtils;
    private DatePicker datePicker;
    private boolean setInitialDate;
    private Date initialDate;

    public DialogBudgetPicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogBudgetPicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_budget_picker);

        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }
}
