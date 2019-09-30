package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
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
    static class ViewHolder1 extends RecyclerView.ViewHolder
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

        ViewHolder1(View v)
        {
            super(v);

            mTxSeqNo = v.findViewById(R.id.celltx_TxSeqNo);
            mTxAdded = v.findViewById(R.id.celltx_TxAdded);
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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder2 extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView mTxDescription;
        ConstraintLayout mFull;

        ViewHolder2(View v)
        {
            super(v);

            mTxDescription = v.findViewById(R.id.cell_info);
            mFull = v.findViewById(R.id.celltx_full);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(ArrayList<RecordTransaction> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType)
    {
        if (viewType == 1)
        {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_transaction, parent, false);
            // set the view's size, margins, paddings and layout parameters
            return new ViewHolder1(v);
        }
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_balance, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder2(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final RecordTransaction rec;

        rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder.getItemViewType() == 1)
        {
            ViewHolder1 vh1 = (ViewHolder1) holder;
            vh1.mTxSeqNo.setText(rec.TxSeqNo.toString());
            vh1.mTxAdded.setText(rec.TxAdded.toString());
            vh1.mTxFilename.setText(rec.TxFilename);
            vh1.mTxLineNo.setText(rec.TxLineNo.toString());
            vh1.mTxDate.setText(android.text.format.DateFormat.format("EEE, dd/MM/yyyy", rec.TxDate));
            vh1.mTxType.setText(rec.TxType);
            vh1.mBankAccount.setText(rec.TxSortCode + " " + rec.TxAccountNumber);
            if (rec.TxDescription.length() > 0)
            {
                vh1.mTxDescription.setVisibility(View.VISIBLE);
                vh1.mTxDescription.setText("Description: " + rec.TxDescription);
            } else
            {
                vh1.mTxDescription.setVisibility(View.GONE);
            }
            vh1.mSubCategoryName.setText("Category: " + rec.SubCategoryName);

            if (rec.Comments.length() > 0)
            {
                vh1.mComments.setVisibility(View.VISIBLE);
                vh1.mComments.setText("Comments: " + rec.Comments);
            } else
            {
                vh1.mComments.setVisibility(View.GONE);
            }
            vh1.mBudget.setText(dateUtils().BudgetAsString(rec.BudgetYear, rec.BudgetMonth));
            if (rec.TxAmount < 0.00)
            {
                vh1.mTxAmount.setText("Amount -£" + String.format("%.2f", rec.TxAmount * -1));
            } else
            {
                vh1.mTxAmount.setText("Amount £" + String.format("%.2f", rec.TxAmount));
            }
            if (rec.HideBalance)
            {
                vh1.mTxBalance.setVisibility(View.INVISIBLE);
            } else
            {
                vh1.mTxBalance.setVisibility(View.VISIBLE);
                if (rec.TxBalance < 0.00)
                {
                    vh1.mTxBalance.setText("Balance -£" + String.format("%.2f", rec.TxBalance * -1));
                } else
                {
                    vh1.mTxBalance.setText("Balance £" + String.format("%.2f", rec.TxBalance));
                }
            }
            vh1.mFull.setOnClickListener(new View.OnClickListener()
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
            int lColor = MainActivity.context.getResources().getColor(R.color.textNormal);
            if (rec.TxType.compareTo("Planned") == 0)
                lColor = MainActivity.context.getResources().getColor(R.color.textLowLight);

            vh1.mTxSeqNo.setTextColor(lColor);
            vh1.mTxAdded.setTextColor(lColor);
            vh1.mTxFilename.setTextColor(lColor);
            vh1.mTxLineNo.setTextColor(lColor);
            vh1.mTxDate.setTextColor(lColor);
            vh1.mTxType.setTextColor(lColor);
            vh1.mBankAccount.setTextColor(lColor);
            vh1.mTxDescription.setTextColor(lColor);
            vh1.mTxAmount.setTextColor(lColor);
            vh1.mTxBalance.setTextColor(lColor);
            vh1.mSubCategoryName.setTextColor(lColor);
            vh1.mComments.setTextColor(lColor);
            vh1.mBudget.setTextColor(lColor);

            if (rec.BalanceCorrect == false)
            {
                //holder.mTxAmount.setTextColor(MainActivity.context.getResources().getColor(R.color.textError));
                //holder.mTxBalance.setTextColor(MainActivity.context.getResources().getColor(R.color.textError));
                if (vh1.mTxBalance.getVisibility() == View.VISIBLE)
                {
                    if (rec.TxBalance < 0.00)
                    {
//                      holder.mTxBalance.setText("Balance -£" + String.format("%.2f", rec.TxBalance * -1) + " -> " +
//                        ", -£" + String.format("%.2f", rec.TxBalanceShouldBe * -1));
                        vh1.mTxBalance.setText("Balance -£" + String.format("%.2f", rec.TxBalance * -1));
                    } else
                    {
//                      holder.mTxBalance.setText("Balance £" + String.format("%.2f", rec.TxBalance) + " -> " +
//                        "£" + String.format("%.2f", rec.TxBalanceShouldBe));
                        vh1.mTxBalance.setText("Balance £" + String.format("%.2f", rec.TxBalance));
                    }
                }
            }
        }
        if (holder.getItemViewType() == 2)
        {
            ViewHolder2 vh2 = (ViewHolder2) holder;

            Float lBalance = Float.parseFloat(rec.TxDescription);
            String lStartingOrEnding = "Starting";
            if (position == 0)
                lStartingOrEnding = "Ending";

            if (lBalance < 0.00f)
            {
                vh2.mTxDescription.setText(lStartingOrEnding + " Balance -£" + String.format("%.2f", lBalance * -1));
            } else
            {
                vh2.mTxDescription.setText(lStartingOrEnding + " Balance £" + String.format("%.2f", lBalance));
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (mDataset.get(position).TxSeqNo != 0)
        {
            return (1);
        }
        return (2);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
