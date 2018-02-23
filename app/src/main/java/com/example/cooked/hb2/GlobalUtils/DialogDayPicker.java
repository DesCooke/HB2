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


public class DialogDayPicker extends Dialog implements View.OnClickListener
{
    public TextView edtText;
    public MyInt mDay;

    public DialogDayPicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        ErrorDialog.Show("Error in DialogDayPicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.day_picker);

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
            edtText.setText("Day: " + myDay.getEditText().getText() );
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
