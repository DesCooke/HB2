package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.R;

import java.util.Objects;

public class DialogPlannedTypePicker extends Dialog implements View.OnClickListener
{
    public TextInputLayout tilPlannedType;
    public MyInt mPlannedType;
    public MyInt mFrequencyMultiplier;
    private RadioButton radYearly;
    private RadioButton radMonthly;
    private RadioButton radWeekly;
    private TextView txtFrequencyMultiplier;
    private Button btnAdd;
    private Button btnSubtract;
    private TextView txtInfo;

    public DialogPlannedTypePicker(Activity a)
    {
        super(a);
    }

    protected void setTxtInfo()
    {
        txtInfo.setText("Oneoff");
        if(mFrequencyMultiplier.Value==1)
        {
            if(radMonthly.isChecked())
                txtInfo.setText("Every month");
            if(radWeekly.isChecked())
                txtInfo.setText("Every week");
            if(radYearly.isChecked())
                txtInfo.setText("Every year");
        }
        else
        {
            if(radMonthly.isChecked())
                txtInfo.setText("Every " + String.valueOf(mFrequencyMultiplier.Value) + " months");
            if(radWeekly.isChecked())
                txtInfo.setText("Every " + String.valueOf(mFrequencyMultiplier.Value) + " weeks");
            if(radYearly.isChecked())
                txtInfo.setText("Every " + String.valueOf(mFrequencyMultiplier.Value) + " years");
        }
        if (tilPlannedType != null)
            tilPlannedType.getEditText().setText(txtInfo.getText());
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
        radOneoff.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        setTxtInfo();
                    }
                });

        radYearly = findViewById(R.id.radYearly);
        radYearly.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        setTxtInfo();
                    }
                });

        radMonthly = findViewById(R.id.radMonthly);
        radMonthly.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        setTxtInfo();
                    }
                });

        radWeekly = findViewById(R.id.radWeekly);
        radWeekly.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        setTxtInfo();
                    }
                });

        txtFrequencyMultiplier = findViewById(R.id.txtFrequencyMultiplier);
        txtFrequencyMultiplier.setText(String.valueOf(mFrequencyMultiplier.Value));

        txtInfo = findViewById(R.id.txtInfo);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mFrequencyMultiplier.Value++;
                        txtFrequencyMultiplier.setText(String.valueOf(mFrequencyMultiplier.Value));
                        setTxtInfo();
                    }
                });

        btnSubtract = findViewById(R.id.btnSubtract);
        btnSubtract.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(mFrequencyMultiplier.Value>1) {
                            mFrequencyMultiplier.Value--;
                            txtFrequencyMultiplier.setText(String.valueOf(mFrequencyMultiplier.Value));
                            setTxtInfo();
                        }
                    }
                });

        if(mPlannedType.Value==RecordPlanned.mPTOneOff)
            radOneoff.setChecked(true);
        if(mPlannedType.Value==RecordPlanned.mPTYearly)
            radYearly.setChecked(true);
        if(mPlannedType.Value==RecordPlanned.mPTMonthly)
            radMonthly.setChecked(true);
        if(mPlannedType.Value==RecordPlanned.mPTWeekly)
            radWeekly.setChecked(true);
        setTxtInfo();
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
            tilPlannedType.getEditText().setText(txtInfo.getText());
        dismiss();
    }

}
