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


public class DialogDatePicker extends Dialog implements View.OnClickListener
{
    public TextView txtStartDate;
    public TextInputLayout tilStartDate;
    private DateUtils dateUtils;
    private DatePicker datePicker;
    private boolean setInitialDate;
    private Date initialDate;

    public DialogDatePicker(Activity a)
    {
        super(a);
        setInitialDate=false;
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogDatePicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_date_picker);

            Button ok= findViewById(R.id.btnOk);
            datePicker= findViewById(R.id.datePicker);

            ok.setOnClickListener(this);
            dateUtils=new DateUtils();
            if(setInitialDate)
            {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(initialDate);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void setInitialDate(Date date)
    {
        initialDate=date;
        setInitialDate=true;
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            switch(v.getId())
            {
                case R.id.btnOk:
                    MyString ms=new MyString();
                    if(!dateUtils.DatePickerToStr(datePicker, ms))
                        return;
                    if(txtStartDate!=null)
                        txtStartDate.setText(ms.Value);
                    if(tilStartDate!=null)
                        tilStartDate.getEditText().setText(ms.Value);
                    break;
                default:
                    break;
            }
            dismiss();
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
    }

}
