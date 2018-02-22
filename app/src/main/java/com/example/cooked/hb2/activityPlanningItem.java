package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.cooked.hb2.GlobalUtils.DialogPlannedTypePicker;
import com.example.cooked.hb2.GlobalUtils.MyInt;

public class activityPlanningItem extends AppCompatActivity
{
    public TextView edtPlannedType;
    public MyInt mPlannedType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        edtPlannedType = findViewById(R.id.edtPlannedType);
        mPlannedType = new MyInt();
        
    }
    
    public void pickPlannedType(View view)
    {
        DialogPlannedTypePicker ppp = new DialogPlannedTypePicker(this);
        ppp.edtPlannedType = edtPlannedType;
        ppp.mPlannedType = mPlannedType;
        ppp.show();
    }
}
