package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;


public class DialogDayPicker extends Dialog implements View.OnClickListener
{
    public TextView edtText;
    public TextInputLayout tilText;
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.day_picker);

        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        TextInputLayout myDay = findViewById(R.id.edtDay);
        if (myDay != null)
        {
            if (myDay.getEditText() != null)
            {
                if (myDay.getEditText().getText() != null)
                {
                    String lFormat = MainActivity.context.getString(R.string.DayCaption);
                    String lText = String.format(lFormat, myDay.getEditText().getText());
                    if (edtText != null)
                        edtText.setText(lText);
                    if (tilText != null)
                        tilText.getEditText().setText(lText);
                    mDay.Value = Integer.valueOf(myDay.getEditText().getText().toString());
                }
            }
        }
        dismiss();
    }

}
