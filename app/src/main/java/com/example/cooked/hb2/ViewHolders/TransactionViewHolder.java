package com.example.cooked.hb2.ViewHolders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.cooked.hb2.R;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class TransactionViewHolder extends RecyclerView.ViewHolder
{
    // each data item is just a string in this case
    TextView mTxSeqNo;
    TextView mTxAdded;
    TextView mTxFilename;
    TextView mTxLineNo;
    TextView mTxDate;
    TextView mTxType;
    TextView mBankAccount;
    TextView mTxDescription;
    TextView mTxAmount;
    TextView mTxBalance;
    TextView mSubCategoryName;
    TextView mComments;
    TextView mBudget;
    ConstraintLayout mFull;

    TransactionViewHolder(View v)
    {
        super(v);

        mTxSeqNo =  v.findViewById(R.id.celltx_TxSeqNo);
        mTxAdded =  v.findViewById(R.id.celltx_TxAdded);
        mTxFilename = v.findViewById(R.id.celltx_TxFilename);
        mTxLineNo = v.findViewById(R.id.celltx_TxLineNo);
        mTxDate = v.findViewById(R.id.celltx_TxDate);
        mTxType = v.findViewById(R.id.celltx_TxType);
        mBankAccount = v.findViewById(R.id.celltx_BankAccount);
        mTxDescription = v.findViewById(R.id.celltx_TxDescription);
        mTxAmount = v.findViewById(R.id.celltx_TxAmount);
        mTxBalance = v.findViewById(R.id.celltx_TxBalance);
        mSubCategoryName = v.findViewById(R.id.celltx_SubCategoryName);
        mComments = v.findViewById(R.id.celltx_Comments);
        mBudget = v.findViewById(R.id.celltx_Budget);
        mFull = v.findViewById(R.id.celltx_full);
    }
}
