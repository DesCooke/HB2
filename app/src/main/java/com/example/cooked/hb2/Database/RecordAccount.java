package com.example.cooked.hb2.Database;

import java.util.Date;

class RecordAccount
{
    public Integer AcSeqNo;
    public String AcSortCode;
    public String AcAccountNumber;
    public String AcDescription;
    public Float AcStartingBalance;

    RecordAccount
            (
                    Integer pSeqNo,
                    String pSortCode,
                    String pAccountNumber,
                    String pDescription,
                    Float pStartingBalance
            )
    {
        AcSeqNo = pSeqNo;
        AcSortCode = pSortCode;
        AcAccountNumber = pAccountNumber;
        AcDescription = pDescription;
        AcStartingBalance = pStartingBalance;
    }

}