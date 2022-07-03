package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class activityAccountItem extends AppCompatActivity
{

    int mAcSeqNo;
    RecordAccount mRec;
    TextInputLayout mAcSortCode;
    TextInputLayout mAcAccountNumber;
    TextInputLayout mAcDescription;
    TextInputLayout mAcStartingBalance;
    Button mBtnOk;
    Button mBtnCancel;
    CheckBox mChkHidden;
    CheckBox mChkUseCategory;
    String mActionType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_account_item);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mAcSortCode = findViewById(R.id.tilAcSortCode);
            mAcAccountNumber = findViewById(R.id.tilAcNumber);
            mAcDescription = findViewById(R.id.tilAcDescription);
            mAcStartingBalance = findViewById(R.id.tilAcStartingBalance);
            mBtnOk = findViewById(R.id.btnOk);
            mBtnCancel = findViewById(R.id.btnCancel);
            mChkHidden = findViewById(R.id.chkHidden);
            mChkUseCategory = findViewById(R.id.chkUseCategory);

            mActionType = getIntent().getStringExtra("ACTIONTYPE");

            if(mActionType.compareTo("ADD")==0)
            {
                mAcSeqNo = -1; // gets created when inserting
                mRec = new RecordAccount();
                setTitle("New Account");

                mAcSortCode.getEditText().setText("");
                mAcAccountNumber.getEditText().setText("");
                mAcDescription.getEditText().setText("");
                mAcStartingBalance.getEditText().setText("");
                mChkHidden.setChecked(false);
                mChkUseCategory.setChecked(false);
            }
            else
            {
                mAcSeqNo = getIntent().getIntExtra("AcSeqNo", -1);
                mRec = MyDatabase.MyDB().getAccountItem(mAcSeqNo);
                setTitle("Account " + mRec.AcSortCode + " " + mRec.AcAccountNumber);

                mAcSortCode.getEditText().setText(mRec.AcSortCode);
                mAcAccountNumber.getEditText().setText(mRec.AcAccountNumber);
                mAcDescription.getEditText().setText(mRec.AcDescription);
                mAcStartingBalance.getEditText().setText(mRec.AcStartingBalance.toString());
                mChkHidden.setChecked(false);
                if(mRec.AcHidden!=0)
                    mChkHidden.setChecked(true);
                mChkUseCategory.setChecked(mRec.AcUseCategory);
            }

            mBtnOk.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        boolean lAccountListChanged=false;
                        if(  (mRec.AcHidden==0 && mChkHidden.isChecked()) ||
                                (mRec.AcHidden!=0 && !mChkHidden.isChecked())
                        )
                        {
                            lAccountListChanged=true;
                        }

                        mRec.AcSortCode = mAcSortCode.getEditText().getText().toString();
                        mRec.AcAccountNumber = mAcAccountNumber.getEditText().getText().toString();
                        mRec.AcDescription = mAcDescription.getEditText().getText().toString();
                        mRec.AcStartingBalance = Float.parseFloat(mAcStartingBalance.getEditText().getText().toString());
                        mRec.AcHidden=0;
                        if(mChkHidden.isChecked())
                            mRec.AcHidden=1;
                        mRec.AcUseCategory=mChkUseCategory.isChecked();
                        if(mActionType.compareTo("EDIT")==0)
                        {
                            MyDatabase.MyDB().updateAccount(mRec);
                        }
                        else
                        {
                            MyDatabase.MyDB().addAccount(mRec);
                        }

                        Intent intent = new Intent();
                        intent.putExtra("ACCOUNTLISTCHANGED", lAccountListChanged);
                        setResult(RESULT_OK, intent);

                    } catch (Exception e)
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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

}
