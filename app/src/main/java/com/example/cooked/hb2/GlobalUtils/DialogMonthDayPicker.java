package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;


public class DialogMonthDayPicker extends Dialog implements View.OnClickListener,
        AdapterView.OnItemSelectedListener
{
    Activity _activity;
    public TextView edtText;
    public TextInputLayout tilText;
    public MyInt mMonth;
    public MyInt mDay;
    public TextInputLayout myDay;
    public Spinner spin;

    public DialogMonthDayPicker(Activity a)
    {
        super(a);
        _activity = a;
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogMonthDayPicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_monthday_picker);

        Button btnOk = findViewById(R.id.btnOk);
        spin = (Spinner) findViewById(R.id.spMonth);
        myDay = findViewById(R.id.edtDay);

        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(_activity,android.R.layout.simple_spinner_item,DateUtils.MonthNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        if(mMonth.Value>0)
            spin.setSelection(mMonth.Value-1);
        if(mDay.Value>0)
           myDay.getEditText().setText(String.valueOf(mDay.Value));

        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        TextInputLayout myDay = findViewById(R.id.edtDay);
        if (myDay != null && myDay.getEditText() != null && myDay.getEditText().getText() != null)
        {
            if (mMonth != null )
            {
                mMonth.Value = spin.getSelectedItemPosition()+1;
                mDay.Value = Integer.valueOf(myDay.getEditText().getText().toString());

                String lText = DateUtils.formatDayAndMonth(mDay.Value, mMonth.Value);
                if (edtText != null)
                    edtText.setText(lText);
                if (tilText != null)
                    tilText.getEditText().setText(lText);
            }
        }
        dismiss();
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

}
