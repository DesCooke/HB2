package com.example.cooked.hb2.Records;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RecordTransaction
{
    public enum Status {
        NEW
    }

    public Integer TxSeqNo;   // Unique number given when inserted into the table - primary key
    public Date TxAdded;
    public String TxFilename;
    public Integer TxLineNo; // From the original input file (line1=column headings, line2=first tx)
    public Date TxDate;
    public String TxType;
    public String TxSortCode;
    public String TxAccountNumber;
    public String TxDescription;
    public Float TxAmount;
    public Float TxBalance;
    public Status TxStatus;
    public Integer CategoryId;
    public String SubCategoryName;
    public String Comments;
    public Integer BudgetYear;
    public Integer BudgetMonth;
    public Boolean HideBalance;
    public Boolean BalanceCorrect;
    public Float TxBalanceShouldBe;
    public Float MarkerStartingBalance;
    public Float MarkerEndingBalance;
    public boolean CheckForChange;

    public RecordTransaction
            (
                     int pTxSeqNo,
                     Date pTxAdded,
                     String pTxFilename,
                     int pTxLineNo,
                     Date pTxDate,
                     String pTxType,
                     String pTxSortCode,
                     String pTxAccountNumber,
                     String pTxDescription,
                     Float pTxAmount,
                     Float pTxBalance,
                     Integer pCategoryId,
                     String pComments,
                     Integer pBudgetYear,
                     Integer pBudgetMonth,
                     Boolean pHideBalance
            )
    {
        TxSeqNo = pTxSeqNo;
        TxAdded = pTxAdded;
        TxFilename = pTxFilename;
        TxLineNo = pTxLineNo;
        TxDate = pTxDate;
        TxType = pTxType;
        TxSortCode = pTxSortCode;
        TxAccountNumber = pTxAccountNumber;
        TxDescription = pTxDescription;
        TxAmount = pTxAmount;
        TxBalance = pTxBalance;
        TxStatus = Status.NEW;
        CategoryId = pCategoryId;
        SubCategoryName = "";
        Comments = pComments;
        BudgetYear = pBudgetYear;
        BudgetMonth = pBudgetMonth;
        HideBalance = pHideBalance;
        BalanceCorrect = true;
        TxBalanceShouldBe = 0.00f;
        CheckForChange = false;
    }
    public RecordTransaction()
    {
        TxSeqNo = 0;
        TxAdded = new Date();
        TxFilename = "";
        TxLineNo = 0;
        TxDate = new Date();
        TxType = "";
        TxSortCode = "";
        TxAccountNumber = "";
        TxDescription = "";
        TxAmount = 0.00f;
        TxBalance = 0.00f;
        TxStatus = Status.NEW;
        CategoryId = 0;
        SubCategoryName = "";
        Comments = "";
        BudgetYear = 0;
        BudgetMonth = 0;
        HideBalance = false;
        BalanceCorrect = true;
        TxBalanceShouldBe = 0.00f;
    }
    public boolean Equals(RecordTransaction recTwo)
    {
        if(TxDate.compareTo(recTwo.TxDate)!=0)
            return(FALSE);
        if(TxType.compareTo(recTwo.TxType)!=0)
            return(FALSE);
        if(TxSortCode.compareTo(recTwo.TxSortCode)!=0)
            return(FALSE);
        if(TxAccountNumber.compareTo(recTwo.TxAccountNumber)!=0)
            return(FALSE);
        if(TxDescription.compareTo(recTwo.TxDescription)!=0)
            return(FALSE);
        if(TxAmount.compareTo(recTwo.TxAmount)!=0)
            return(FALSE);

        return(TRUE);
    }
}