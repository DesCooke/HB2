package com.example.cooked.hb2.Database;

import java.util.Date;

public class RecordPlanned
{
    public enum PlannedType {
        OneOff,
        Annual,
        Monthly,
        Weekly,
        Daily
    }

    public Integer mPlannedId;
    public PlannedType mPlannedType;
    public String mPlannedName;
    
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
    public String mMatchingTxAmount;
    

    RecordPlanned
        (
            Integer pPlannedId,
            PlannedType pPlannedType,
            String pPlannedName,
    
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
            String pMatchingTxAmount
        )
    {
            mPlannedId = pPlannedId;
            mPlannedType = pPlannedType;
            mPlannedName = pPlannedName;
    
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
    }
    
    public RecordPlanned()
    {
        mPlannedId = 0;
        mPlannedType = PlannedType.OneOff;
        mPlannedName = "";
    
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
        mMatchingTxAmount = "";
    }
    
}
