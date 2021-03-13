package com.example.cooked.hb2.Records;

import com.example.cooked.hb2.Records.RecordTransaction;

import java.util.ArrayList;
import java.util.Date;

public class RecordAccount
{
    public Integer AcSeqNo;
    public String AcSortCode;
    public String AcAccountNumber;
    public String AcDescription;
    public Float AcStartingBalance;
    public Integer AcOrderSeqNo;
    public Integer AcHidden;

    public ArrayList<RecordTransaction> RecordTransactions;

    public RecordAccount
            (
                    Integer pSeqNo,
                    String pSortCode,
                    String pAccountNumber,
                    String pDescription,
                    Float pStartingBalance,
                    Integer pOrderSeqNo,
                    Integer pHidden
            )
    {
        AcSeqNo = pSeqNo;
        AcSortCode = pSortCode;
        AcAccountNumber = pAccountNumber;
        AcDescription = pDescription;
        AcStartingBalance = pStartingBalance;
        AcOrderSeqNo = pOrderSeqNo;
        AcHidden = pHidden;
        RecordTransactions = new ArrayList<RecordTransaction>();
    }

    public boolean IsSameAs(RecordAccount Other)
    {
        if(AcSeqNo!=Other.AcSeqNo)
            return(false);
        if(AcSortCode.compareTo(Other.AcSortCode)!=0)
            return(false);
        if(AcAccountNumber.compareTo(Other.AcAccountNumber)!=0)
            return(false);
        if(AcDescription.compareTo(Other.AcDescription)!=0)
            return(false);
        if(AcOrderSeqNo!=Other.AcOrderSeqNo)
            return(false);
        if(AcHidden!=Other.AcHidden)
            return(false);
        return(true);
    }
}