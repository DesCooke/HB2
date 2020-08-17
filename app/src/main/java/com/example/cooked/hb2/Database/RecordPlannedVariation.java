package com.example.cooked.hb2.Database;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.Date;

public class RecordPlannedVariation
{
    public Integer mVariationId;
    public Integer mPlannedId;
    public String mVariationName;
    public Date mEffDate;
    public Float mAmount;

    RecordPlannedVariation
            (
                    Integer pVariationId,
                    Integer pPlannedId,
                    String pVariationName,
                    Date pEffDate,
                    Float pAmount
            )
    {
        mVariationId = pVariationId;
        mPlannedId = pPlannedId;
        mVariationName = pVariationName;
        mEffDate = pEffDate;
        mAmount = pAmount;
    }

    public RecordPlannedVariation()
    {
        mVariationId = 0;
        mPlannedId = 0;
        mVariationName = "";
        mEffDate = new Date();
        mAmount = 0.00f;
    }

}
