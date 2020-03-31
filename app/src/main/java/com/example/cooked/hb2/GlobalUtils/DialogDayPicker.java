package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;


public class DialogDayPicker extends Dialog implements View.OnClickListener
{
    private TextView edtText;         // EditText on previous form
    public TextInputLayout tilText;   // TextInputLayer edit on previous form
    public MyInt mDay;                // used to pass Day to this form and back from it

    public TextInputLayout edtDay;    // TextInputLayer on this form
    public TextInputEditText tietDay; // TextInputEditText on this form

    public DialogDayPicker(Activity a)
    {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_day_picker);

        edtDay = findViewById(R.id.edtDay);
        tietDay = findViewById(R.id.tietDay);

        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        if (mDay.Value>0)
        {
            String lString=String.valueOf(mDay.Value);
            edtDay.getEditText().setText(lString);
        }

    }

    @Override
    public void onClick(View v)
    {
        String lFormat = MainActivity.context.getString(R.string.DayCaption);
        String lText = String.format(lFormat, tietDay.getText());
        if (edtText != null)
            edtText.setText(lText);
        if (tilText != null)
            tilText.getEditText().setText(lText);
        mDay.Value = Integer.valueOf(tietDay.getText().toString());
        dismiss();
    }

}
