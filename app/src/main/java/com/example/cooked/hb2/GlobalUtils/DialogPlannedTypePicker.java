package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.R;

import java.util.Objects;

public class DialogPlannedTypePicker extends Dialog implements View.OnClickListener
{
    public TextInputLayout tilPlannedType;
    public MyInt mPlannedType;
    private RadioButton radYearly;
    private RadioButton radMonthly;
    private RadioButton radWeekly;


    public DialogPlannedTypePicker(Activity a)
    {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_plannedtype_picker);

        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        RadioButton radOneoff = findViewById(R.id.radOneOff);
        radYearly = findViewById(R.id.radYearly);
        radMonthly = findViewById(R.id.radMonthly);
        radWeekly = findViewById(R.id.radWeekly);

        if(mPlannedType.Value==RecordPlanned.mPTOneOff)
            radOneoff.setChecked(true);
        if(mPlannedType.Value==RecordPlanned.mPTYearly)
            radYearly.setChecked(true);
        if(mPlannedType.Value==RecordPlanned.mPTMonthly)
            radMonthly.setChecked(true);
        if(mPlannedType.Value==RecordPlanned.mPTWeekly)
            radWeekly.setChecked(true);
    }

    @Override
    public void onClick(View v)
    {
        mPlannedType.Value = RecordPlanned.mPTOneOff;
        if (radYearly.isChecked())
            mPlannedType.Value = RecordPlanned.mPTYearly;
        if (radMonthly.isChecked())
            mPlannedType.Value = RecordPlanned.mPTMonthly;
        if (radWeekly.isChecked())
            mPlannedType.Value = RecordPlanned.mPTWeekly;
        if (tilPlannedType != null)
            Objects.requireNonNull(tilPlannedType.getEditText()).setText(RecordPlanned.mPlannedTypes[mPlannedType.Value]);

        dismiss();
    }

}
