package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyString;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class PlannedAdapter extends RecyclerView.Adapter<PlannedAdapter.ViewHolder>
{
    private ArrayList<RecordPlanned> mDataset;
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
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mPlannedType;
        public TextView mPlannedName;
        public TextView mSubcategoryName;
        public TextView mBankAccount;
        public TextView mPlanned;
        public TextView mStartDate;
        public TextView mEndDate;
        public TextView mMatchingType;
        public TextView mMatchingDescription;
        public TextView mMatchingAmount;
        public ConstraintLayout mFull;

        public ViewHolder(View v)
        {
            super(v);

            mPlannedType = (TextView) v.findViewById(R.id.cell_PlannedType);
            mPlannedName = (TextView) v.findViewById(R.id.cell_PlannedName);
            mSubcategoryName = (TextView) v.findViewById(R.id.cell_SubcategoryName);
            mBankAccount = (TextView) v.findViewById(R.id.cell_BankAccount);
            mPlanned = (TextView) v.findViewById(R.id.cell_Planned);
            mStartDate = (TextView) v.findViewById(R.id.cell_StartDate);
            mEndDate = (TextView) v.findViewById(R.id.cell_EndDate);
            mMatchingType = (TextView) v.findViewById(R.id.cell_MatchingType);
            mMatchingDescription = (TextView) v.findViewById(R.id.cell_MatchingDescription);
            mMatchingAmount = (TextView) v.findViewById(R.id.cell_MatchingAmount);
            mFull = (ConstraintLayout) v.findViewById(R.id.cell_full);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlannedAdapter(ArrayList<RecordPlanned> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlannedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_planned, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordPlanned rec;

        rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {


            holder.mPlannedType.setText(RecordPlanned.mPlannedTypes[rec.mPlannedType]);
            holder.mPlannedName.setText("Name: " + rec.mPlannedName);
            holder.mSubcategoryName.setText("Category: " + rec.mSubCategoryName);

            if(rec.mSortCode.compareTo("Cash")==0)
                holder.mBankAccount.setText("Account: Cash");
            else
                holder.mBankAccount.setText("Account: Current");

            holder.mPlanned.setText(rec.mPlanned);

            MyString lMyString = new MyString();

            DateUtils.dateUtils().DateToStr(rec.mStartDate, lMyString);
            holder.mStartDate.setText("Start: " + lMyString.Value);

            DateUtils.dateUtils().DateToStr(rec.mEndDate, lMyString);
            holder.mEndDate.setText("End: " + lMyString.Value);

            holder.mMatchingType.setText("Matching Type: " + rec.mMatchingTxType);
            holder.mMatchingDescription.setText("Matching Description: " + rec.mMatchingTxDescription);
            if(rec.mMatchingTxAmount < 0.00)
            {
                holder.mMatchingAmount.setText("Amount: -£" + String.format("%.2f", rec.mMatchingTxAmount*-1));
            }
            else
            {
                holder.mMatchingAmount.setText("Amount: £" + String.format("%.2f", rec.mMatchingTxAmount));
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
            ErrorDialog.Show("Error in PlannedAdapter.onBindViewHolder", e.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
