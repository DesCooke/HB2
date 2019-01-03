package com.example.cooked.hb2.Database;

import java.util.Date;

public class RecordTransactionDate
{
    public Integer TxSeqNo;   // Unique number given when inserted into the table - primary key
    public Date TxDate;

    RecordTransactionDate
            (
                    int pTxSeqNo,
                    Date pTxDate
            )
    {
        TxSeqNo = pTxSeqNo;
        TxDate = pTxDate;
    }
    public RecordTransactionDate()
    {
        TxSeqNo = 0;
        TxDate = new Date();
    }
}
