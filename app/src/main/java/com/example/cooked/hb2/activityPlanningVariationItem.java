package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.Database.RecordPlannedVariation;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.Date;
import java.util.Locale;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class activityPlanningVariationItem extends AppCompatActivity
{
    public Integer plannedId;
    public Integer variationId;
    public RecordPlanned recordPlanned;
    public RecordPlannedVariation recordVariation;
    public String actionType;

    public TextInputLayout tilVariationName;
    public TextInputLayout tilEffDate;
    public TextInputLayout tilAmount;
    public DialogDatePicker ddpStart;
    public Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_variation_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tilVariationName = findViewById(R.id.tilVariationName);
        tilEffDate = findViewById(R.id.tilEffDate);
        tilAmount = findViewById(R.id.tilAmount);
        btnDelete = findViewById(R.id.btnDelete);

        actionType = getIntent().getStringExtra("ACTIONTYPE");

        ddpStart = new DialogDatePicker(this);
        ddpStart.tilStartDate = findViewById(R.id.tilEffDate);

        if(actionType.compareTo("ADD")==0)
        {
            plannedId = getIntent().getIntExtra("PLANNEDID", 0);
            recordPlanned = MyDatabase.MyDB().getSinglePlanned(plannedId);
            setTitle("Adding a Variation for " + recordPlanned.mPlannedName);
            MyString myString = new MyString();
            if (!dateUtils().DateToStr(new Date(), myString))
                return;
            tilEffDate.getEditText().setText(myString.Value);
            btnDelete.setVisibility(View.INVISIBLE);
        }

        if(actionType.compareTo("EDIT")==0)
        {
            variationId = getIntent().getIntExtra("VARIATIONID", 0);
            recordVariation = MyDatabase.MyDB().getSingleVariation(variationId);
            plannedId = recordVariation.mPlannedId;
            recordPlanned = MyDatabase.MyDB().getSinglePlanned(plannedId);

            setTitle("Editing a Variation for " + recordPlanned.mPlannedName);
            MyString myString = new MyString();
            if (!dateUtils().DateToStr(recordVariation.mEffDate, myString))
                return;
            tilEffDate.getEditText().setText(myString.Value);
            String lText = String.format(Locale.UK, "%.2f", recordVariation.mAmount);
            tilAmount.getEditText().setText(lText);
            tilVariationName.getEditText().setText(recordVariation.mVariationName);
        }

    }

    public void pickEffDateTime(View view)
    {
        try
        {
            Date date = dateUtils().StrToDate(ddpStart.tilStartDate.getEditText().getText().toString());
            ddpStart.setInitialDate(date);
            ddpStart.show();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void saveItem(View view)
    {
        try
        {
            if(actionType.compareTo("ADD")==0)
            {
                RecordPlannedVariation rec = new RecordPlannedVariation();
                rec.mVariationName = tilVariationName.getEditText().getText().toString();
                rec.mEffDate = dateUtils().StrToDate(ddpStart.tilStartDate.getEditText().getText().toString());
                rec.mAmount = Float.valueOf(tilAmount.getEditText().getText().toString());
                rec.mPlannedId = plannedId;
                MyDatabase.MyDB().addVariation(rec);

                finish();
            }
            if(actionType.compareTo("EDIT")==0)
            {
                recordVariation.mVariationName = tilVariationName.getEditText().getText().toString();
                recordVariation.mEffDate = dateUtils().StrToDate(ddpStart.tilStartDate.getEditText().getText().toString());
                recordVariation.mAmount = Float.valueOf(tilAmount.getEditText().getText().toString());
                MyDatabase.MyDB().updateVariation(recordVariation);
                finish();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void deleteItem(View view)
    {
        try
        {
            if(actionType.compareTo("EDIT")==0)
            {
                MyDatabase.MyDB().deleteVariation(recordVariation);
                finish();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

}