package com.example.cooked.hb2.Database;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.Date;

public class RecordPlanned
{
    public static String[] mPlannedTypes = {"Oneoff", "Yearly", "Monthly", "Weekly"};
    public static int mPTOneOff = 0;
    public static int mPTYearly = 1;
    public static int mPTMonthly = 2;
    public static int mPTWeekly = 3;
    
    public Integer mPlannedId;
    public Integer mPlannedType;
    public String mPlannedName;
    public Integer mSubCategoryId;
    public String mSortCode;
    public String mAccountNo;
    
    public Date mPlannedDate;
    public Integer mPlannedMonth;
    public Integer mPlannedDay;
    public Boolean mMonday;
    public Boolean mTuesday;
    public Boolean mWednesday;
    public Boolean mThursday;
    public Boolean mFriday;
    public Boolean mSaturday;
    public Boolean mSunday;
    
    public Date mStartDate;
    public Date mEndDate;
    
    public String mMatchingTxType;
    public String mMatchingTxDescription;
    public Float mMatchingTxAmount;

    public String mPlanned;
    public String mSubCategoryName;

    public Boolean mAutoMatchTransaction;

    Date mNextDueDate;
    

    RecordPlanned
        (
            Integer pPlannedId,
            Integer pPlannedType,
            String pPlannedName,
            Integer pSubCategoryId,
            String pSortCode,
            String pAccountNo,
    
            Date pPlannedDate,
            Integer pPlannedMonth,
            Integer pPlannedDay,
            Boolean pMonday,
            Boolean pTuesday,
            Boolean pWednesday,
            Boolean pThursday,
            Boolean pFriday,
            Boolean pSaturday,
            Boolean pSunday,
    
            Date pStartDate,
            Date pEndDate,
    
            String pMatchingTxType,
            String pMatchingTxDescription,
            Float pMatchingTxAmount,
            Boolean pAutoMatchTransaction

        ) {
        mPlannedId = pPlannedId;
        mPlannedType = pPlannedType;
        mPlannedName = pPlannedName;
        mSubCategoryId = pSubCategoryId;
        mSortCode = pSortCode;
        mAccountNo = pAccountNo;

        mPlannedDate = pPlannedDate;
        mPlannedMonth = pPlannedMonth;
        mPlannedDay = pPlannedDay;
        mMonday = pMonday;
        mTuesday = pTuesday;
        mWednesday = pWednesday;
        mThursday = pThursday;
        mFriday = pFriday;
        mSaturday = pSaturday;
        mSunday = pSunday;

        mStartDate = pStartDate;
        mEndDate = pEndDate;

        mMatchingTxType = pMatchingTxType;
        mMatchingTxDescription = pMatchingTxDescription;
        mMatchingTxAmount = pMatchingTxAmount;

        mPlanned = "";
        if (mPlannedType == RecordPlanned.mPTOneOff) {
            MyString ms=new MyString();
            if(DateUtils.dateUtils().DateTo_ddd_ddmmyyyy(mPlannedDate, ms))
              mPlanned = ms.Value;
        }

        if (mPlannedType == RecordPlanned.mPTYearly) {
            mPlanned = "Day: " + mPlannedDay.toString() + ", Month " + mPlannedMonth.toString();
        }

        if (mPlannedType == RecordPlanned.mPTMonthly) {
            mPlanned = "Day: " + mPlannedDay.toString();
        }

        if (mPlannedType == RecordPlanned.mPTWeekly) {
            String lString="";
            if(mMonday)
                lString = lString + "Mon,";
            if(mTuesday)
                lString = lString + "Tue,";
            if(mWednesday)
                lString = lString + "Wed,";
            if(mThursday)
                lString = lString + "Thu,";
            if(mFriday)
                lString = lString + "Fri,";
            if(mSaturday)
                lString = lString + "Sat,";
            if(mSunday)
                lString = lString + "Sun,";

            mPlanned = lString;
        }
        mSubCategoryName = "";
        mNextDueDate = null;
        mAutoMatchTransaction = pAutoMatchTransaction;

    }
    
    public RecordPlanned()
    {
        mPlannedId = 0;
        mPlannedType = 0; // oneoff
        mPlannedName = "";
        mSubCategoryId = 0;
        mSortCode = "";
        mAccountNo = "";
    
        mPlannedDate = new Date();
        mPlannedMonth = 0;
        mPlannedDay = 0;
        mMonday = Boolean.FALSE;
        mTuesday = Boolean.FALSE;
        mWednesday = Boolean.FALSE;
        mThursday = Boolean.FALSE;
        mFriday = Boolean.FALSE;
        mSaturday = Boolean.FALSE;
        mSunday = Boolean.FALSE;
    
        mStartDate = new Date();
        mEndDate = new Date();
    
        mMatchingTxType = "";
        mMatchingTxDescription = "";
        mMatchingTxAmount = 0.0f;

        mPlanned = "";
        mSubCategoryName = "";
        mNextDueDate = null;
        mAutoMatchTransaction = false;
    }
    
}
