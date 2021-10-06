package com.example.cooked.hb2.ViewHolders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.example.cooked.hb2.R;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class ViewHolderTransactionItem extends RecyclerView.ViewHolder
{
    // each data item is just a string in this case
    public TextView mTxSeqNo;
    public TextView mTxAdded;
    public TextView mTxFilename;
    public TextView mTxLineNo;
    public TextView mTxDate;
    public TextView mTxType;
    public TextView mBankAccount;
    public TextView mTxDescription;
    public TextView mTxAmount;
    public TextView mTxBalance;
    public TextView mSubCategoryName;
    public TextView mComments;
    public TextView mBudget;
    public ConstraintLayout mFull;

    public ViewHolderTransactionItem(View v)
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
