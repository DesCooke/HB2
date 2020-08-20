package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordBudgetProgress;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyString;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Math.abs;

public class BudgetProgressAdapter extends RecyclerView.Adapter<BudgetProgressAdapter.ViewHolder>
{
    private ArrayList<RecordBudgetProgress> mDataset;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordPlanned obj);
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
        TextView mTitle1;
        TextView mTitle2;
        TextView mTitle3;
        ProgressBar mProgressBarGreen;
        ProgressBar mProgressBarYellow;
        ProgressBar mProgressBarRed;
        CardView mFull;

        ViewHolder(View v)
        {
            super(v);

            mTitle1 = v.findViewById(R.id.title1);
            mTitle2 = v.findViewById(R.id.title2);
            mTitle3 = v.findViewById(R.id.title3);
            mProgressBarGreen = v.findViewById(R.id.progressBarGreen);
            mProgressBarYellow = v.findViewById(R.id.progressBarYellow);
            mProgressBarRed = v.findViewById(R.id.progressBarRed);
            mFull = v.findViewById(R.id.full);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BudgetProgressAdapter(ArrayList<RecordBudgetProgress> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BudgetProgressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_progress, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordBudgetProgress rec;

        rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle1.setText(rec.mTitle);

        String lString="£" + String.format("%.2f", abs(rec.mSpentAmount) ) +
                " of £" + String.format("%.2f", abs(rec.mTotalAmount)) +
                " spent, £" + String.format("%.2f", abs(rec.mLeftAmount)) + " left";
        holder.mTitle2.setText(lString);

        lString=rec.mSpentPerc + "% through budget, " +
                rec.mPercInMonth + "% through month";
        holder.mTitle3.setText(lString);

        int lPercInBudget = rec.mSpentPerc;
        if(lPercInBudget < 86)
        {
            holder.mProgressBarRed.setVisibility(View.GONE);
            holder.mProgressBarYellow.setVisibility(View.GONE);
            holder.mProgressBarGreen.setVisibility(View.VISIBLE);
            holder.mProgressBarGreen.setProgress(lPercInBudget);
        }
        else
        {
            if(lPercInBudget < 101)
            {
                holder.mProgressBarGreen.setVisibility(View.GONE);
                holder.mProgressBarRed.setVisibility(View.GONE);
                holder.mProgressBarYellow.setVisibility(View.VISIBLE);
                holder.mProgressBarYellow.setProgress(lPercInBudget);
            }
            else
            {
                holder.mProgressBarGreen.setVisibility(View.GONE);
                holder.mProgressBarYellow.setVisibility(View.GONE);
                holder.mProgressBarRed.setVisibility(View.VISIBLE);
                holder.mProgressBarRed.setProgress(lPercInBudget);
            }
        }

/*
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

 */
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
