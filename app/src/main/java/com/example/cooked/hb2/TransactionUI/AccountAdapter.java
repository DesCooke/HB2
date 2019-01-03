package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordAccount;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>
{
    private ArrayList<RecordAccount> mDataset;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordAccount obj);
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
        TextView mAcSortCode;
        TextView mAcAccountNumber;
        ConstraintLayout mFull;

        ViewHolder(View v)
        {
            super(v);

            mAcSortCode =  v.findViewById(R.id.cellac_sortcode);
            mAcAccountNumber = v.findViewById(R.id.cellac_accountnumber);
            mFull = v.findViewById(R.id.celltx_full);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AccountAdapter(ArrayList<RecordAccount> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_account, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordAccount rec;

        rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try
        {


            holder.mAcSortCode.setText(rec.AcSortCode.toString());
            holder.mAcAccountNumber.setText(rec.AcAccountNumber.toString());

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
