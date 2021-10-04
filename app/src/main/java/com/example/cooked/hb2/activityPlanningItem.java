package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.CategoryPicker;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.GlobalUtils.DialogDayPicker;
import com.example.cooked.hb2.GlobalUtils.DialogMonthDayPicker;
import com.example.cooked.hb2.GlobalUtils.DialogPlannedTypePicker;
import com.example.cooked.hb2.GlobalUtils.DialogWeekDayPicker;
import com.example.cooked.hb2.GlobalUtils.MyBoolean;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;

import static com.example.cooked.hb2.Database.MyDatabase.MyDB;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static com.example.cooked.hb2.MainActivity.context;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activityPlanningItem extends AppCompatActivity
{
    public TextInputLayout edtPlannedName;
    public TextInputLayout tilPlanned;
    public TextInputLayout tilStartDate;
    public TextInputLayout tilEndDate;
    public TextInputLayout tilPlannedType;
    public TextInputLayout tilSubCategoryName;
    public RadioButton radCurrentAccount;
    public RadioButton radCashAccount;
    public TextInputLayout edtMatchType;
    public TextInputLayout edtMatchDescription;
    public TextInputLayout edtMatchAmount;
    public Switch swAutoMatchTransaction;
    public Button btnOk;
    public Button btnDelete;
    public Button btnCopyToNew;
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
    public CheckBox mPaidInParts;
    public CheckBox mHighlight30DaysBeforeAnniversary;
    public MyInt mFrequencyMultiplier;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_planning_item);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mRecordPlanned = new RecordPlanned();
            mPlannedType = new MyInt();
            mFrequencyMultiplier = new MyInt();
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
            tilPlanned = findViewById(R.id.tilPlanned);
            tilStartDate = findViewById(R.id.tilStartDate);
            tilEndDate = findViewById(R.id.tilEndDate);
            tilSubCategoryName = findViewById(R.id.tilSubCategoryName);
            tilPlannedType = findViewById(R.id.tilPlannedType);
            radCurrentAccount = findViewById(R.id.radCurrentAccount);
            radCashAccount = findViewById(R.id.radCashAccount);
            edtMatchType = findViewById(R.id.edtMatchType);
            edtMatchDescription = findViewById(R.id.edtMatchDescription);
            edtMatchAmount = findViewById(R.id.edtMatchAmount);
            btnOk = findViewById(R.id.btnOk);
            btnDelete = findViewById(R.id.btnDelete);
            btnCopyToNew = findViewById(R.id.btnCopyToNew);
            swAutoMatchTransaction = findViewById(R.id.swAutoMatchTransaction);
            mPaidInParts = findViewById(R.id.chkPaidInParts);
            mHighlight30DaysBeforeAnniversary = findViewById(R.id.chkHighlight30DaysBeforeAnniversary);

            cp = new CategoryPicker(this);
            cp.MySubCategoryId = MySubCategoryId;
            cp.tilSubCategoryName = tilSubCategoryName;

            ddpStart = new DialogDatePicker(this);
            ddpStart.tilStartDate = findViewById(R.id.tilStartDate);

            ddpEnd = new DialogDatePicker(this);
            ddpEnd.tilStartDate = findViewById(R.id.tilEndDate);

            ddpPlanned = new DialogDatePicker(this);
            ddpPlanned.tilStartDate = findViewById(R.id.tilPlanned);

            dmdp = new DialogMonthDayPicker(this);
            dmdp.tilText = findViewById(R.id.tilPlanned);
            dmdp.mMonth = MyMonth;
            dmdp.mDay = MyDay;

            ddayp = new DialogDayPicker(this);
            ddayp.tilText = findViewById(R.id.tilPlanned);
            ddayp.mDay = MyDay;

            dwdayp = new DialogWeekDayPicker(this);
            dwdayp.tilText = findViewById(R.id.tilPlanned);
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

                MyString myString = new MyString();
                if (!dateUtils().DateToStr(new Date(), myString))
                    return;
                tilStartDate.getEditText().setText(myString.Value);
                tilEndDate.getEditText().setText("");
                btnDelete.setVisibility(View.GONE);
                btnCopyToNew.setVisibility(View.GONE);
                swAutoMatchTransaction.setChecked(false);
                mPaidInParts.setChecked(false);
                mFrequencyMultiplier.Value=1;
                mHighlight30DaysBeforeAnniversary.setChecked(false);
            }
            if (mActionType.compareTo("EDIT") == 0)
            {
                setTitle("Modify a planned item");
                Integer lPlannedId = getIntent().getIntExtra("PlannedId", 0);
                if (lPlannedId == 0)
                {
                    MyString myString = new MyString();
                    if (!dateUtils().DateToStr(new Date(), myString))
                        return;
                    tilStartDate.getEditText().setText(myString.Value);
                    tilEndDate.getEditText().setText("");
                } else
                {
                    mRecordPlanned = MyDB().getSinglePlanned(lPlannedId);
                    mPlannedType.Value = mRecordPlanned.mPlannedType;
                    MyDay.Value = mRecordPlanned.mPlannedDay;
                    MySubCategoryId.Value = mRecordPlanned.mSubCategoryId;
                    tilPlannedType.getEditText().setText(RecordPlanned.mPlannedTypes[mRecordPlanned.mPlannedType]);
                    tilPlannedType.getEditText().setText(DateUtils.dateUtils().PlannedTypeDescription(mRecordPlanned, new Date()));
                    edtPlannedName.getEditText().setText(mRecordPlanned.mPlannedName);
                    tilSubCategoryName.getEditText().setText(mRecordPlanned.mSubCategoryName);

                    if (mRecordPlanned.mSortCode.compareTo("Cash") == 0)
                        radCashAccount.setChecked(TRUE);
                    else
                        radCurrentAccount.setChecked(TRUE);

                    tilPlanned.getEditText().setText(mRecordPlanned.mPlanned);

                    mPaidInParts.setChecked(mRecordPlanned.mPaidInParts);
                    mHighlight30DaysBeforeAnniversary.setChecked(mRecordPlanned.mHighlight30DaysBeforeAnniversary);

                    MyString lString = new MyString();

                    DateUtils.dateUtils().DateToStr(mRecordPlanned.mStartDate, lString);
                    tilStartDate.getEditText().setText(lString.Value);

                    String unknownDateString=getResources().getString(R.string.date_unknown_millisecs);
                    long unknownDateLong = Long.parseLong(unknownDateString);
                    Date unknownDate = new Date(unknownDateLong);
                    if(mRecordPlanned.mEndDate.getTime()==unknownDate.getTime())
                    {
                        tilEndDate.getEditText().setText("");
                    }
                    else
                    {
                        DateUtils.dateUtils().DateToStr(mRecordPlanned.mEndDate, lString);
                        tilEndDate.getEditText().setText(lString.Value);
                    }
                    edtMatchType.getEditText().setText(mRecordPlanned.mMatchingTxType);
                    edtMatchDescription.getEditText().setText(mRecordPlanned.mMatchingTxDescription);
                    String lText = String.format(Locale.UK, "%.2f", mRecordPlanned.mMatchingTxAmount);
                    edtMatchAmount.getEditText().setText(lText);

                    MyMonday.Value = mRecordPlanned.mMonday;
                    MyTuesday.Value = mRecordPlanned.mTuesday;
                    MyWednesday.Value = mRecordPlanned.mWednesday;
                    MyThursday.Value = mRecordPlanned.mThursday;
                    MyFriday.Value = mRecordPlanned.mFriday;
                    MySaturday.Value = mRecordPlanned.mSaturday;
                    MySunday.Value = mRecordPlanned.mSunday;

                    MyDay.Value = mRecordPlanned.mPlannedDay;
                    MyMonth.Value = mRecordPlanned.mPlannedMonth;

                    swAutoMatchTransaction.setChecked(mRecordPlanned.mAutoMatchTransaction);

                    mFrequencyMultiplier.Value = mRecordPlanned.mFrequencyMultiplier;
                }
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void pickCashAccount(View view)
    {
        try
        {
            radCurrentAccount.setChecked(FALSE);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void pickStartDateTime(View view)
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
            mRecordPlanned.mPlannedType = mPlannedType.Value;
            mRecordPlanned.mPlannedName = edtPlannedName.getEditText().getText().toString();
            mRecordPlanned.mSubCategoryId = MySubCategoryId.Value;
            mRecordPlanned.mPaidInParts = mPaidInParts.isChecked();
            mRecordPlanned.mHighlight30DaysBeforeAnniversary = mHighlight30DaysBeforeAnniversary.isChecked();
            mRecordPlanned.mFrequencyMultiplier = mFrequencyMultiplier.Value;
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
                mRecordPlanned.mPlannedDate = dateUtils().StrToDate(tilPlanned.getEditText().getText().toString());

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


            mRecordPlanned.mStartDate = dateUtils().StrToDate(tilStartDate.getEditText().getText().toString());

            if(tilEndDate.getEditText().getText().length()==0)
            {
                String lunknownDate=getResources().getString(R.string.date_unknown_millisecs);
                Long lunknownLong=Long.parseLong(lunknownDate);
                mRecordPlanned.mEndDate = new Date(lunknownLong);
            }
            else
            {
                mRecordPlanned.mEndDate = dateUtils().StrToDate(tilEndDate.getEditText().getText().toString());
            }

            mRecordPlanned.mMatchingTxType = edtMatchType.getEditText().getText().toString();
            mRecordPlanned.mMatchingTxDescription = edtMatchDescription.getEditText().getText().toString();
            mRecordPlanned.mMatchingTxAmount = 0.00f;
            if (edtMatchAmount.getEditText().getText().length() > 0)
                mRecordPlanned.mMatchingTxAmount = Float.valueOf(edtMatchAmount.getEditText().getText().toString());

            mRecordPlanned.mAutoMatchTransaction = swAutoMatchTransaction.isChecked();

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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void deleteItem(View view)
    {
        try
        {
            MyDB().deletePlanned(mRecordPlanned);
            finish();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void copyToNew(View view)
    {
        try
        {
            setTitle("Add a new planned item");
            mActionType = "ADD";
            mRecordPlanned.mPlannedId = 0;
            btnDelete.setVisibility(View.GONE);
            btnCopyToNew.setVisibility(View.GONE);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void showVariations(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), activityPlanningVariation.class);
            intent.putExtra("ACTIONTYPE", "VIEW");
            intent.putExtra("PLANNEDID", mRecordPlanned.mPlannedId);
            startActivity(intent);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    public void pickEndDateTime(View view)
    {
        try
        {
            Date date = dateUtils().StrToDate(ddpEnd.tilStartDate.getEditText().getText().toString());
            ddpEnd.setInitialDate(date);
            ddpEnd.show();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void pickPlanned(View view)
    {
        try
        {
            if (mPlannedType.Value == RecordPlanned.mPTOneOff)
            {
                Date date = new Date();
                ddpPlanned.setInitialDate(date);
                ddpPlanned.show();
            }

            if (mPlannedType.Value == RecordPlanned.mPTYearly)
            {
                dmdp.show();
            }

            if (mPlannedType.Value == RecordPlanned.mPTMonthly)
            {
                if(mRecordPlanned.mPlannedDay>0)
                    MyDay.Value = mRecordPlanned.mPlannedDay;
                ddayp.show();
            }

            if (mPlannedType.Value == RecordPlanned.mPTWeekly)
            {
                dwdayp.show();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void pickCategory(View view)
    {
        try
        {
            cp.show();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void pickCurrentAccount(View view)
    {
        try
        {
            radCashAccount.setChecked(FALSE);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void pickPlannedType(View view)
    {
        try
        {
            DialogPlannedTypePicker ppp = new DialogPlannedTypePicker(this);
            ppp.tilPlannedType = tilPlannedType;
            ppp.mPlannedType = mPlannedType;
            ppp.mFrequencyMultiplier = mFrequencyMultiplier;
            ppp.show();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

}

