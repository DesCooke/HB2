package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyString;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView mPlannedType;
        TextView mPlannedName;
        TextView mSubcategoryName;
        TextView mBankAccount;
        TextView mPlanned;
        TextView mStartDate;
        TextView mEndDate;
        TextView mMatchingType;
        TextView mMatchingDescription;
        TextView mMatchingAmount;
        ConstraintLayout mFull;

        ViewHolder(View v)
        {
            super(v);

            mPlannedType = v.findViewById(R.id.cell_PlannedType);
            mPlannedName = v.findViewById(R.id.cell_PlannedName);
            mSubcategoryName = v.findViewById(R.id.cell_SubcategoryName);
            mBankAccount = v.findViewById(R.id.cell_BankAccount);
            mPlanned = v.findViewById(R.id.cell_Planned);
            mStartDate = v.findViewById(R.id.cell_StartDate);
            mEndDate = v.findViewById(R.id.cell_EndDate);
            mMatchingType = v.findViewById(R.id.cell_MatchingType);
            mMatchingDescription = v.findViewById(R.id.cell_MatchingDescription);
            mMatchingAmount = v.findViewById(R.id.cell_MatchingAmount);
            mFull = v.findViewById(R.id.cell_full);
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
        return new ViewHolder(v);
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

        holder.mPlannedType.setText(DateUtils.dateUtils().PlannedTypeDescription(rec, new Date()));
        holder.mPlannedName.setText("Name: " + rec.mPlannedName);
        holder.mSubcategoryName.setText("Category: " + rec.mSubCategoryName);

        if (rec.mSortCode.compareTo("Cash") == 0)
            holder.mBankAccount.setText("Account: Cash");
        else
            holder.mBankAccount.setText("Account: Current");

        holder.mPlanned.setText(rec.mPlanned);

        MyString lMyString = new MyString();

        DateUtils.dateUtils().DateToStr(rec.mStartDate, lMyString);
        holder.mStartDate.setText("Start: " + lMyString.Value);

        DateUtils.dateUtils().DateToStr(rec.mEndDate, lMyString);
        if(lMyString.Value.compareTo("23/05/2069")==0)
        {
            holder.mEndDate.setText("End: ");
        }
        else
        {
            holder.mEndDate.setText("End: " + lMyString.Value);
        }

        holder.mMatchingType.setText("Matching Type: " + rec.mMatchingTxType);
        holder.mMatchingDescription.setText("Matching Description: " + rec.mMatchingTxDescription);
        Float lAmount = rec.GetAmountAt(Calendar.getInstance().getTime());
        holder.mMatchingAmount.setText("Amount: " + Tools.moneyFormat(lAmount * -1));
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
