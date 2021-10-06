package com.example.cooked.hb2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooked.hb2.GlobalUtils.MyLog;

public class activityLog extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_log);
            setTitle("Activity Log");
            TextView tv = findViewById(R.id.txtLog);
            String lLog = MyLog.GetText();
            tv.setText(lLog);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }
}
