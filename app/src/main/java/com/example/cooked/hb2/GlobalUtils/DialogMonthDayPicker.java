package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooked.hb2.R;


public class DialogMonthDayPicker extends Dialog implements View.OnClickListener
{
    public TextView edtText;
    public MyInt mMonth;
    public MyInt mDay;

    public DialogMonthDayPicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogMonthDayPicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.monthday_picker);

            Button btnOk=findViewById(R.id.btnOk);

            btnOk.setOnClickListener(this);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            TextInputLayout myDay = findViewById(R.id.edtDay);
            TextInputLayout myMonth = findViewById(R.id.edtMonth);
            edtText.setText("Day: " + myDay.getEditText().getText() + ", Month " + myMonth.getEditText().getText() );
            mMonth.Value = Integer.valueOf(myMonth.getEditText().getText().toString());
            mDay.Value = Integer.valueOf(myDay.getEditText().getText().toString());
            
            dismiss();
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
            return;
        }
    }

}