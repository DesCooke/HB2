package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class DialogDayPicker extends Dialog implements View.OnClickListener
{
    private Activity _activity;
    private TextView edtText;         // EditText on previous form
    public TextInputLayout tilText;   // TextInputLayer edit on previous form
    public MyInt mDay;                // used to pass Day to this form and back from it

    public TextInputLayout edtDay;    // TextInputLayer on this form
    public TextInputEditText tietDay; // TextInputEditText on this form

    public DialogDayPicker(Activity a)
    {
        super(a);
        _activity=a;
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
            edtDay.getEditText().selectAll();
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
