package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordAccount;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;

import static com.example.cooked.hb2.Database.MyDatabase.MyDB;

public class activityAccountItem extends AppCompatActivity {

    int mAcSeqNo;
    RecordAccount mRec;
    TextInputLayout mAcDescription;
    TextInputLayout mAcStartingBalance;
    Button mBtnOk;
    Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAcDescription = findViewById(R.id.tilAcDescription);
        mAcStartingBalance = findViewById(R.id.tilAcStartingBalance);
        mBtnOk = findViewById(R.id.btnOk);
        mBtnCancel = findViewById(R.id.btnCancel);

        mAcSeqNo = getIntent().getIntExtra("AcSeqNo", -1);
        mRec = MyDatabase.MyDB().getAccountItem(mAcSeqNo);

        setTitle("Account " + mRec.AcSortCode + " " + mRec.AcAccountNumber);

        mAcDescription.getEditText().setText(mRec.AcDescription);
        mAcStartingBalance.getEditText().setText(mRec.AcStartingBalance.toString());

        mBtnOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    mRec.AcDescription = mAcDescription.getEditText().getText().toString();
                    mRec.AcStartingBalance = Float.parseFloat(mAcStartingBalance.getEditText().getText().toString());
                    MyDatabase.MyDB().updateAccount(mRec);
                }
                catch(Exception e)
                {
                    ErrorDialog.Show("Error in activityAccountItem::onClick", e.getMessage());
                }
                finish();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });

    }

}
