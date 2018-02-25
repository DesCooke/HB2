package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.CategoryPicker;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.GlobalUtils.DialogDayPicker;
import com.example.cooked.hb2.GlobalUtils.DialogMonthDayPicker;
import com.example.cooked.hb2.GlobalUtils.DialogPlannedTypePicker;
import com.example.cooked.hb2.GlobalUtils.DialogWeekDayPicker;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyBoolean;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.Date;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activityPlanningItem extends AppCompatActivity
{
    public TextInputLayout edtPlannedName;
    public TextView edtPlanned;
    public TextView edtStartDate;
    public TextView edtEndDate;
    public TextView edtPlannedType;
    public TextView edtSubCategoryName;
    public RadioButton radCurrentAccount;
    public RadioButton radCashAccount;
    public TextInputLayout edtMatchType;
    public TextInputLayout edtMatchDescription;
    public TextInputLayout edtMatchAmount;
    public Button btnOk;
    public Button btnDelete;
    public MyInt MySubCategoryId;
    public MyInt MyDay;
    public MyInt MyMonth;
    public MyBoolean MyMonday;
    public MyBoolean MyTuesday;
    public MyBoolean MyWednesday;
    public MyBoolean MyThursday;
    public MyBoolean MyFriday;
    public MyBoolean MySaturday;
    public MyBoolean MySunday;
    
    public CategoryPicker cp;
    public DialogDatePicker ddpStart;
    public DialogDatePicker ddpEnd;
    public DialogDatePicker ddpPlanned;
    public DialogMonthDayPicker dmdp;
    public DialogDayPicker ddayp;
    public DialogWeekDayPicker dwdayp;
    public MyInt mPlannedType;
    public String mActionType;
    public RecordPlanned mRecordPlanned;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        mRecordPlanned = new RecordPlanned();
        mPlannedType = new MyInt();
        MySubCategoryId = new MyInt();
        MyMonth = new MyInt();
        MyDay = new MyInt();
        MyMonday = new MyBoolean();
        MyTuesday = new MyBoolean();
        MyWednesday = new MyBoolean();
        MyThursday = new MyBoolean();
        MyFriday = new MyBoolean();
        MySaturday = new MyBoolean();
        MySunday = new MyBoolean();
        
        edtPlannedName = findViewById(R.id.edtPlannedName);
        edtPlanned = findViewById(R.id.edtPlanned);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        edtSubCategoryName = findViewById(R.id.edtSubCategoryName);
        edtPlannedType = findViewById(R.id.edtPlannedType);
        radCurrentAccount = findViewById(R.id.radCurrentAccount);
        radCashAccount = findViewById(R.id.radCashAccount);
        edtMatchType = findViewById(R.id.edtMatchType);
        edtMatchDescription = findViewById(R.id.edtMatchDescription);
        edtMatchAmount = findViewById(R.id.edtMatchAmount);
        btnOk = findViewById(R.id.btnOk);
        btnDelete = findViewById(R.id.btnDelete);





        cp = new CategoryPicker(this);
        cp.MySubCategoryId = MySubCategoryId;
        cp.edtSubCategoryName = edtSubCategoryName;
        
        ddpStart=new DialogDatePicker(this);
        ddpStart.txtStartDate=findViewById(R.id.edtStartDate);
        
        ddpEnd=new DialogDatePicker(this);
        ddpEnd.txtStartDate=findViewById(R.id.edtEndDate);

        ddpPlanned=new DialogDatePicker(this);
        ddpPlanned.txtStartDate=findViewById(R.id.edtPlanned);

        dmdp = new DialogMonthDayPicker(this);
        dmdp.edtText=findViewById(R.id.edtPlanned);
        dmdp.mMonth = MyMonth;
        dmdp.mDay = MyDay;
        
        ddayp = new DialogDayPicker(this);
        ddayp.edtText=findViewById(R.id.edtPlanned);
        ddayp.mDay = MyDay;

        dwdayp= new DialogWeekDayPicker(this);
        dwdayp.edtText = findViewById(R.id.edtPlanned);
        dwdayp.mMonday = MyMonday;
        dwdayp.mTuesday = MyTuesday;
        dwdayp.mWednesday = MyWednesday;
        dwdayp.mThursday = MyThursday;
        dwdayp.mFriday = MyFriday;
        dwdayp.mSaturday = MySaturday;
        dwdayp.mSunday = MySunday;

        mActionType = getIntent().getStringExtra("ACTIONTYPE");
        if (mActionType.compareTo("ADD") == 0)
        {
            setTitle("Add a new planned item");
            edtPlanned.setText("<pick>");
            edtSubCategoryName.setText("<pick>");
            edtPlannedType.setText("<pick>");
    
            MyString myString=new MyString();
            if(!dateUtils().DateToStr(new Date(), myString))
                return;
            edtStartDate.setText(myString.Value);
            edtEndDate.setText("31 Dec 2099");
            btnDelete.setVisibility(View.GONE);
        }
        if (mActionType.compareTo("EDIT") == 0)
        {
            setTitle("Modify a planned item");
            Integer lPlannedId=getIntent().getIntExtra("PlannedId", 0);
            if(lPlannedId==0)
            {
                edtPlanned.setText("<pick>");
                edtSubCategoryName.setText("<pick>");
                edtPlannedType.setText("<pick>");

                MyString myString=new MyString();
                if(!dateUtils().DateToStr(new Date(), myString))
                    return;
                edtStartDate.setText(myString.Value);
                edtEndDate.setText("31 Dec 2099");
            }
            else
            {
                mRecordPlanned = MyDB().getSinglePlanned(lPlannedId);
                mPlannedType.Value = mRecordPlanned.mPlannedType;
                MyDay.Value = mRecordPlanned.mPlannedDay;
                MySubCategoryId.Value = mRecordPlanned.mSubCategoryId;
                edtPlannedType.setText(RecordPlanned.mPlannedTypes[mRecordPlanned.mPlannedType]);
                edtPlannedName.getEditText().setText(mRecordPlanned.mPlannedName);
                edtSubCategoryName.setText(mRecordPlanned.mSubCategoryName);

                if(mRecordPlanned.mSortCode.compareTo("Cash")==0)
                    radCashAccount.setChecked(TRUE);
                else
                    radCurrentAccount.setChecked(TRUE);

                edtPlanned.setText(mRecordPlanned.mPlanned);

                MyString lString = new MyString();

                DateUtils.dateUtils().DateTo_ddmmyyyy(mRecordPlanned.mStartDate, lString);
                edtStartDate.setText(lString.Value);

                DateUtils.dateUtils().DateTo_ddmmyyyy(mRecordPlanned.mEndDate, lString);
                edtEndDate.setText(lString.Value);

                edtMatchType.getEditText().setText(mRecordPlanned.mMatchingTxType);
                edtMatchDescription.getEditText().setText(mRecordPlanned.mMatchingTxDescription);
                if(mRecordPlanned.mMatchingTxAmount < 0.00)
                {
                    edtMatchAmount.getEditText().setText("-" + String.format("%.2f", mRecordPlanned.mMatchingTxAmount*-1));
                }
                else
                {
                    edtMatchAmount.getEditText().setText(String.format("%.2f", mRecordPlanned.mMatchingTxAmount));
                }


                MyMonday.Value = mRecordPlanned.mMonday;
                MyTuesday.Value = mRecordPlanned.mTuesday;
                MyWednesday.Value = mRecordPlanned.mWednesday;
                MyThursday.Value = mRecordPlanned.mThursday;
                MyFriday.Value = mRecordPlanned.mFriday;
                MySaturday.Value = mRecordPlanned.mSaturday;
                MySunday.Value = mRecordPlanned.mSunday;

                MyDay.Value = mRecordPlanned.mPlannedDay;
                MyMonth.Value = mRecordPlanned.mPlannedMonth;
            }
        }

    }

    public void pickCashAccount(View view)
    {
        radCurrentAccount.setChecked(FALSE);
    }
    
    public void pickStartDateTime(View view)
    {
        try
        {
            Date date=dateUtils().StrToDate(ddpStart.txtStartDate.getText().toString());
            ddpStart.setInitialDate(date);
            ddpStart.show();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickStartDateTime", e.getMessage());
        }
    }

    public void saveItem(View view)
    {
        try
        {
            mRecordPlanned.mPlannedType = mPlannedType.Value;
            mRecordPlanned.mPlannedName = edtPlannedName.getEditText().getText().toString();
            mRecordPlanned.mSubCategoryId = MySubCategoryId.Value;
            if (radCashAccount.isChecked())
            {
                mRecordPlanned.mSortCode = "Cash";
                mRecordPlanned.mAccountNo = "Cash";
            } else
            {
                mRecordPlanned.mSortCode = "11-03-95";
                mRecordPlanned.mAccountNo = "00038840";
            }

            if (mPlannedType.Value == RecordPlanned.mPTOneOff)
            {
                mRecordPlanned.mPlannedDate = dateUtils().StrToDate(edtPlanned.getText().toString());

                mRecordPlanned.mPlannedMonth = 0;
                mRecordPlanned.mPlannedDay = 0;
                mRecordPlanned.mMonday = FALSE;
                mRecordPlanned.mTuesday = FALSE;
                mRecordPlanned.mWednesday = FALSE;
                mRecordPlanned.mThursday = FALSE;
                mRecordPlanned.mFriday = FALSE;
                mRecordPlanned.mSaturday = FALSE;
                mRecordPlanned.mSunday = FALSE;
            }

            if (mPlannedType.Value == RecordPlanned.mPTYearly)
            {
                mRecordPlanned.mPlannedMonth = MyMonth.Value;
                mRecordPlanned.mPlannedDay = MyDay.Value;

                mRecordPlanned.mPlannedDate = new Date();
                mRecordPlanned.mMonday = FALSE;
                mRecordPlanned.mTuesday = FALSE;
                mRecordPlanned.mWednesday = FALSE;
                mRecordPlanned.mThursday = FALSE;
                mRecordPlanned.mFriday = FALSE;
                mRecordPlanned.mSaturday = FALSE;
                mRecordPlanned.mSunday = FALSE;
            }

            if (mPlannedType.Value == RecordPlanned.mPTMonthly)
            {
                mRecordPlanned.mPlannedDay = MyDay.Value;

                mRecordPlanned.mPlannedMonth = 0;
                mRecordPlanned.mPlannedDate = new Date();
                mRecordPlanned.mMonday = FALSE;
                mRecordPlanned.mTuesday = FALSE;
                mRecordPlanned.mWednesday = FALSE;
                mRecordPlanned.mThursday = FALSE;
                mRecordPlanned.mFriday = FALSE;
                mRecordPlanned.mSaturday = FALSE;
                mRecordPlanned.mSunday = FALSE;
            }

            if (mPlannedType.Value == RecordPlanned.mPTWeekly)
            {
                mRecordPlanned.mMonday = MyMonday.Value;
                mRecordPlanned.mTuesday = MyTuesday.Value;
                mRecordPlanned.mWednesday = MyWednesday.Value;
                mRecordPlanned.mThursday = MyThursday.Value;
                mRecordPlanned.mFriday = MyFriday.Value;
                mRecordPlanned.mSaturday = MySaturday.Value;
                mRecordPlanned.mSunday = MySunday.Value;

                mRecordPlanned.mPlannedDay = 0;
                mRecordPlanned.mPlannedMonth = 0;
                mRecordPlanned.mPlannedDate = new Date();
            }


            mRecordPlanned.mStartDate = dateUtils().StrToDate(edtStartDate.getText().toString());

            mRecordPlanned.mEndDate = dateUtils().StrToDate(edtEndDate.getText().toString());

            mRecordPlanned.mMatchingTxType = edtMatchType.getEditText().getText().toString();
            mRecordPlanned.mMatchingTxDescription = edtMatchDescription.getEditText().getText().toString();
            mRecordPlanned.mMatchingTxAmount = 0.00f;
            if(edtMatchAmount.getEditText().getText().length() > 0)
                mRecordPlanned.mMatchingTxAmount = Float.valueOf(edtMatchAmount.getEditText().getText().toString());


            if (mActionType.compareTo("ADD") == 0)
            {
                mRecordPlanned.mPlannedId = 0;
                MyDB().addPlanned(mRecordPlanned);
            }

            if (mActionType.compareTo("EDIT") == 0)
            {
                MyDB().updatePlanned(mRecordPlanned);
            }
            finish();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("saveItem", e.getMessage());
        }
    }

    public void deleteItem(View view)
    {
        try
        {
            MyDB().deletePlanned(mRecordPlanned);
            finish();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("deleteItem", e.getMessage());
        }
    }

    public void pickEndDateTime(View view)
    {
        try
        {
            Date date=new Date();
            ddpEnd.setInitialDate(date);
            ddpEnd.show();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickEndDateTime", e.getMessage());
        }
    }

    public void pickPlanned(View view)
    {
        try
        {
            if(mPlannedType.Value==RecordPlanned.mPTOneOff)
            {
                Date date = new Date();
                ddpPlanned.setInitialDate(date);
                ddpPlanned.show();
            }

            if(mPlannedType.Value==RecordPlanned.mPTYearly)
            {
                dmdp.show();
            }
            
            if(mPlannedType.Value==RecordPlanned.mPTMonthly)
            {
                ddayp.show();
            }

            if(mPlannedType.Value==RecordPlanned.mPTWeekly)
            {
                dwdayp.show();
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickEndDateTime", e.getMessage());
        }
    }
    
    public void pickCategory(View view)
    {
        try
        {
            cp.show();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickCategory", e.getMessage());
        }
    }
    public void pickCurrentAccount(View view)
    {
        radCashAccount.setChecked(FALSE);
    }
    
    public void pickPlannedType(View view)
    {
        edtPlanned.setText("<pick>");
        DialogPlannedTypePicker ppp = new DialogPlannedTypePicker(this);
        ppp.edtPlannedType = edtPlannedType;
        ppp.mPlannedType = mPlannedType;
        ppp.show();
    }
}
