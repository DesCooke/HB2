package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;


public class DialogWeekDayPicker extends Dialog implements View.OnClickListener
{
    public TextView edtText;
    public MyBoolean mMonday;
    public MyBoolean mTuesday;
    public MyBoolean mWednesday;
    public MyBoolean mThursday;
    public MyBoolean mFriday;
    public MyBoolean mSaturday;
    public MyBoolean mSunday;

    public DialogWeekDayPicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogWeekDayPicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.weekday_picker);

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
            CheckBox myMonday = findViewById(R.id.chkMonday);
            CheckBox myTuesday = findViewById(R.id.chkTuesday);
            CheckBox myWednesday = findViewById(R.id.chkWednesday);
            CheckBox myThursday = findViewById(R.id.chkThursday);
            CheckBox myFriday = findViewById(R.id.chkFriday);
            CheckBox mySaturday = findViewById(R.id.chkSaturday);
            CheckBox mySunday = findViewById(R.id.chkSunday);

            mMonday.Value = myMonday.isChecked();
            mTuesday.Value = myTuesday.isChecked();
            mWednesday.Value = myWednesday.isChecked();
            mThursday.Value = myThursday.isChecked();
            mFriday.Value = myFriday.isChecked();
            mSaturday.Value = mySaturday.isChecked();
            mSunday.Value = mySunday.isChecked();
            
            String lString="";
            if(mMonday.Value)
                lString = lString + "Mon,";
            if(mTuesday.Value)
                lString = lString + "Tue,";
            if(mWednesday.Value)
                lString = lString + "Wed,";
            if(mThursday.Value)
                lString = lString + "Thu,";
            if(mFriday.Value)
                lString = lString + "Fri,";
            if(mSaturday.Value)
                lString = lString + "Sat,";
            if(mSunday.Value)
                lString = lString + "Sun,";

            String lCaption= MainActivity.context.getString(R.string.DaysCaption);
            String lText = String.format(lCaption, lString);
            edtText.setText(lText);
            
            dismiss();
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
    }

}
