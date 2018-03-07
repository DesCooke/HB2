package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>
{
    private ArrayList<RecordTransaction> mDataset;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordTransaction obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
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

        public ViewHolder(View v)
        {
            super(v);

            mTxSeqNo = (TextView) v.findViewById(R.id.celltx_TxSeqNo);
            mTxAdded = (TextView) v.findViewById(R.id.celltx_TxAdded);
            mTxFilename = (TextView) v.findViewById(R.id.celltx_TxFilename);
            mTxLineNo = (TextView) v.findViewById(R.id.celltx_TxLineNo);
            mTxDate = (TextView) v.findViewById(R.id.celltx_TxDate);
            mTxType = (TextView) v.findViewById(R.id.celltx_TxType);
            mBankAccount = (TextView) v.findViewById(R.id.celltx_BankAccount);
            mTxDescription = (TextView) v.findViewById(R.id.celltx_TxDescription);
            mTxAmount = (TextView) v.findViewById(R.id.celltx_TxAmount);
            mTxBalance = (TextView) v.findViewById(R.id.celltx_TxBalance);
            mSubCategoryName = (TextView) v.findViewById(R.id.celltx_SubCategoryName);
            mComments = (TextView) v.findViewById(R.id.celltx_Comments);
            mBudget = (TextView) v.findViewById(R.id.celltx_Budget);
            mFull = (ConstraintLayout) v.findViewById(R.id.celltx_full);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(ArrayList<RecordTransaction> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_transaction, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordTransaction rec;

         rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            
            
            holder.mTxSeqNo.setText(rec.TxSeqNo.toString());
            holder.mTxAdded.setText(rec.TxAdded.toString());
            holder.mTxFilename.setText(rec.TxFilename);
            holder.mTxLineNo.setText(rec.TxLineNo.toString());
            holder.mTxDate.setText(android.text.format.DateFormat.format("EEE, dd/MM/yyyy", rec.TxDate));
            holder.mTxType.setText(rec.TxType);
            holder.mBankAccount.setText(rec.TxSortCode + " " + rec.TxAccountNumber);
            if(rec.TxDescription.length() > 0)
            {
                holder.mTxDescription.setVisibility(View.VISIBLE);
                holder.mTxDescription.setText("Description: " + rec.TxDescription);
            }
            else
            {
                holder.mTxDescription.setVisibility(View.GONE);
            }
            holder.mSubCategoryName.setText("Category: " + rec.SubCategoryName);

            if(rec.Comments.length() > 0)
            {
                holder.mComments.setVisibility(View.VISIBLE);
                holder.mComments.setText("Comments: " + rec.Comments);
            }
            else
            {
                holder.mComments.setVisibility(View.GONE);
            }
            holder.mBudget.setText(dateUtils().BudgetAsString(rec.BudgetYear, rec.BudgetMonth));
            if(rec.TxAmount < 0.00)
            {
                holder.mTxAmount.setText("Amount -£" + String.format("%.2f", rec.TxAmount*-1));
            }
            else
            {
                holder.mTxAmount.setText("Amount £" + String.format("%.2f", rec.TxAmount));
            }
            if(rec.HideBalance.booleanValue()==true)
            {
                holder.mTxBalance.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.mTxBalance.setVisibility(View.VISIBLE);
                if (rec.TxBalance < 0.00)
                {
                    holder.mTxBalance.setText("Balance -£" + String.format("%.2f", rec.TxBalance * -1));
                } else
                {
                    holder.mTxBalance.setText("Balance £" + String.format("%.2f", rec.TxBalance));
                }
            }
            holder.mFull.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mOnItemClickListener != null)
                    {
                        mOnItemClickListener.onItemClick(view, rec);
                    }
                }
            });

        }
        catch (Exception e) {
            ErrorDialog.Show("Error in TransactionAdapter.onBindViewHolder", e.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
