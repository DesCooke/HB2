package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder>
{
    private ArrayList<RecordCommon> mDataset;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordCommon obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView mTxSeqNo;
        TextView mTxDate;
        TextView mTxDescription;
        TextView mTxAmount;
        TextView mSubCategoryName;
        TextView mComments;
        ConstraintLayout mFull;

        ViewHolder(View v)
        {
            super(v);

            mTxDate = v.findViewById(R.id.celltx_TxDate);
            mTxDescription = v.findViewById(R.id.celltx_TxDescription);
            mTxAmount = v.findViewById(R.id.celltx_TxAmount);
            mSubCategoryName = v.findViewById(R.id.celltx_SubCategoryName);
            mComments = v.findViewById(R.id.celltx_Comments);
            mFull = v.findViewById(R.id.celltx_full);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommonAdapter(ArrayList<RecordCommon> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_common, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordCommon rec;

         rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try
        {
    
    
            holder.mTxDate.setText(android.text.format.DateFormat.format("EEE, dd/MM/yyyy", rec.TxDate));
            if (rec.TxDescription.length() > 0)
            {
                holder.mTxDescription.setVisibility(View.VISIBLE);
                holder.mTxDescription.setText("Description: " + rec.TxDescription);
            } else
            {
                holder.mTxDescription.setVisibility(View.GONE);
            }
            holder.mSubCategoryName.setText("Category: " + rec.SubCategoryName);
    
            if (rec.Comments.length() > 0)
            {
                holder.mComments.setVisibility(View.VISIBLE);
                holder.mComments.setText("Comments: " + rec.Comments);
            } else
            {
                holder.mComments.setVisibility(View.GONE);
            }
            if (rec.TxAmount < 0.00)
            {
                holder.mTxAmount.setText("Amount -£" + String.format("%.2f", rec.TxAmount * -1));
            } else
            {
                holder.mTxAmount.setText("Amount £" + String.format("%.2f", rec.TxAmount));
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
            int lColor = MainActivity.context.getResources().getColor(R.color.textNormal);
    
            holder.mTxDate.setTextColor(lColor);
            holder.mTxDescription.setTextColor(lColor);
            holder.mTxAmount.setTextColor(lColor);
            holder.mSubCategoryName.setTextColor(lColor);
            holder.mComments.setTextColor(lColor);
            
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in CommonAdapter.onBindViewHolder", e.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
