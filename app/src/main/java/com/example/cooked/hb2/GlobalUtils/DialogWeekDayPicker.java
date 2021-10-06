package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class DialogWeekDayPicker extends Dialog implements View.OnClickListener
{
    public TextInputLayout tilText;
    public MyBoolean mMonday;
    public MyBoolean mTuesday;
    public MyBoolean mWednesday;
    public MyBoolean mThursday;
    public MyBoolean mFriday;
    public MyBoolean mSaturday;
    public MyBoolean mSunday;

    private CheckBox myMonday;
    private CheckBox myTuesday;
    private CheckBox myWednesday;
    private CheckBox myThursday;
    private CheckBox myFriday;
    private CheckBox mySaturday;
    private CheckBox mySunday;

    public DialogWeekDayPicker(Activity a)
    {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_weekday_picker);

        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        myMonday = findViewById(R.id.chkMonday);
        myTuesday = findViewById(R.id.chkTuesday);
        myWednesday = findViewById(R.id.chkWednesday);
        myThursday = findViewById(R.id.chkThursday);
        myFriday = findViewById(R.id.chkFriday);
        mySaturday = findViewById(R.id.chkSaturday);
        mySunday = findViewById(R.id.chkSunday);

        myMonday.setChecked(mMonday.Value);
        myTuesday.setChecked(mTuesday.Value);
        myWednesday.setChecked(mWednesday.Value);
        myThursday.setChecked(mThursday.Value);
        myFriday.setChecked(mFriday.Value);
        mySaturday.setChecked(mSaturday.Value);
        mySunday.setChecked(mSunday.Value);
    }

    @Override
    public void onClick(View v)
    {
        mMonday.Value = myMonday.isChecked();
        mTuesday.Value = myTuesday.isChecked();
        mWednesday.Value = myWednesday.isChecked();
        mThursday.Value = myThursday.isChecked();
        mFriday.Value = myFriday.isChecked();
        mSaturday.Value = mySaturday.isChecked();
        mSunday.Value = mySunday.isChecked();

        String lString = "";
        if (mMonday.Value)
            lString = lString + "Mon,";
        if (mTuesday.Value)
            lString = lString + "Tue,";
        if (mWednesday.Value)
            lString = lString + "Wed,";
        if (mThursday.Value)
            lString = lString + "Thu,";
        if (mFriday.Value)
            lString = lString + "Fri,";
        if (mSaturday.Value)
            lString = lString + "Sat,";
        if (mSunday.Value)
            lString = lString + "Sun,";

        String lCaption = MainActivity.context.getString(R.string.DaysCaption);
        String lText = String.format(lCaption, lString);
        if (tilText != null)
            Objects.requireNonNull(tilText.getEditText()).setText(lText);

        dismiss();
    }

}
