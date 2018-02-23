package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.R;

import java.util.Calendar;
import java.util.Date;


public class DialogPlannedTypePicker extends Dialog implements View.OnClickListener
{
    public TextView edtPlannedType;
    public MyInt mPlannedType;

    public DialogPlannedTypePicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogPlannedTypePicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.plannedtype_picker);

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
            RadioButton radYearly =findViewById(R.id.radYearly);
            RadioButton radMonthly =findViewById(R.id.radMonthly);
            RadioButton radWeekly =findViewById(R.id.radWeekly);
            mPlannedType.Value = RecordPlanned.mPTOneOff;
            if(radYearly.isChecked())
              mPlannedType.Value = RecordPlanned.mPTYearly;
            if(radMonthly.isChecked())
              mPlannedType.Value = RecordPlanned.mPTMonthly;
            if(radWeekly.isChecked())
              mPlannedType.Value = RecordPlanned.mPTWeekly;
            edtPlannedType.setText(RecordPlanned.mPlannedTypes[mPlannedType.Value]);
            
            dismiss();
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
            return;
        }
    }

}
