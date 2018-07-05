package com.example.cooked.hb2.Database;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RecordCommon
{
    public Integer TxSeqNo;   // Unique number given when inserted into the table - primary key
    public Date TxDate;
    public String TxDescription;
    public Float TxAmount;
    public Integer CategoryId;
    public String SubCategoryName;
    public String Comments;

    RecordCommon
            (
                     int pTxSeqNo,
                     Date pTxDate,
                     String pTxDescription,
                     Float pTxAmount,
                     Integer pCategoryId,
                     String pComments
            )
    {
        TxSeqNo = pTxSeqNo;
        TxDate = pTxDate;
        TxDescription = pTxDescription;
        TxAmount = pTxAmount;
        CategoryId = pCategoryId;
        SubCategoryName = "";
        Comments = pComments;
    }
    public RecordCommon()
    {
        TxSeqNo = 0;
        TxDate = new Date();
        TxDescription = "";
        TxAmount = 0.00f;
        CategoryId = 0;
        SubCategoryName = "";
        Comments = "";
    }
}