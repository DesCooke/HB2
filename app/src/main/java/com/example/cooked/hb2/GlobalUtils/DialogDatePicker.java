package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cooked.hb2.R;

import java.util.Calendar;
import java.util.Date;


public class DialogDatePicker extends Dialog implements View.OnClickListener
{
    public Activity activity;
    public TextView txtStartDate;
    public TextInputLayout tilStartDate;
    private DateUtils dateUtils;
    private DatePicker datePicker;
    private boolean setInitialDate;
    private Date initialDate;
    public Switch switch1;
    public Long dateUnknownMillisecs;
    public String dateUnknownString;
    public Calendar calendar;

    public DialogDatePicker(Activity a)
    {
        super(a);
        activity=a;
        setInitialDate = false;
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogDatePicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_picker);

        Button ok = findViewById(R.id.btnOk);
        datePicker = findViewById(R.id.datePicker);
        switch1 = findViewById(R.id.switch1);

        calendar = Calendar.getInstance();
        dateUnknownMillisecs=Long.parseLong(activity.getResources().getString(R.string.date_unknown_millisecs));
        calendar.setTimeInMillis(dateUnknownMillisecs);
        MyString myString=new MyString();
        DateUtils.DateToStr(calendar.getTime(), myString);
        dateUnknownString=myString.Value;

        ok.setOnClickListener(this);
        dateUtils = new DateUtils();
        if (setInitialDate)
        {
            calendar.setTime(initialDate);

            if(calendar.getTimeInMillis()>=dateUnknownMillisecs)
            {
                datePicker.setVisibility(View.INVISIBLE);
                switch1.setChecked(false);
                calendar.setTimeInMillis(dateUnknownMillisecs);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
            else
            {
                switch1.setChecked(true);
                datePicker.setVisibility(View.VISIBLE);
                calendar.setTime(initialDate);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }

        }

        switch1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                try {
                    if(switch1.isChecked())
                    {
                        datePicker.setVisibility(View.VISIBLE);
                        if(calendar.getTimeInMillis()>=dateUnknownMillisecs)
                        {
                            calendar.setTime(new Date());
                            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        }
                        else
                        {
                            calendar.setTime(initialDate);
                            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        }
                    }
                    else
                    {
                        datePicker.setVisibility(View.INVISIBLE);
                        calendar.setTimeInMillis(dateUnknownMillisecs);
                        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
                finally {
                }
            }
        });

    }

    public void setInitialDate(Date date)
    {
        initialDate = date;
        setInitialDate = true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnOk:
                MyString ms = new MyString();
                if (!dateUtils.DatePickerToStr(datePicker, ms))
                    return;
                if (txtStartDate != null)
                    txtStartDate.setText(ms.Value);
                if (tilStartDate != null)
                    tilStartDate.getEditText().setText(ms.Value);
                break;
            default:
                break;
        }
        dismiss();
    }

}
