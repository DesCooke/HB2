package com.example.cooked.hb2.TransactionUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordAccount;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.helper.DragItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>
        implements DragItemTouchHelper.MoveHelperAdapter
{
    private Context ctx;
    private ArrayList<RecordAccount> mDataset;
    private OnItemClickListener mOnItemClickListener;
    public OnStartDragListener mDragStartListener = null;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordAccount obj);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        try
        {
            this.mOnItemClickListener = mItemClickListener;
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void setDragListener(OnStartDragListener dragStartListener) {
        try
        {
            this.mDragStartListener = dragStartListener;
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder
            implements DragItemTouchHelper.TouchViewHolder
    {
        // each data item is just a string in this case
        TextView mAcSortCode;
        TextView mAcAccountNumber;
        TextView mAcDescription;
        LinearLayout mFull;
        ImageView mAcMove;

        ViewHolder(View v)
        {
            super(v);

            mAcSortCode = v.findViewById(R.id.cellac_sortcode);
            mAcAccountNumber = v.findViewById(R.id.cellac_accountnumber);
            mAcDescription = v.findViewById(R.id.cellacc_description);
            mAcMove = v.findViewById(R.id.cellacc_move);

            mFull = v.findViewById(R.id.celltx_full);
        }

        @Override
        public void onItemSelected()
        {
        }

        @Override
        public void onItemClear()
        {
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AccountAdapter(Context context, ArrayList<RecordAccount> lDataset)
    {
        mDataset = lDataset;
        ctx = context;
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
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        final RecordAccount rec;

        try
        {
            rec = mDataset.get(position);

            if (rec.AcSortCode.compareTo("Cash") == 0)
            {
                holder.mAcSortCode.setText("");
                holder.mAcAccountNumber.setText("");
                holder.mAcSortCode.setVisibility(View.GONE);
                holder.mAcAccountNumber.setVisibility(View.GONE);
            } else
            {
                holder.mAcSortCode.setVisibility(View.VISIBLE);
                holder.mAcAccountNumber.setVisibility(View.VISIBLE);
                holder.mAcSortCode.setText(rec.AcSortCode.toString());
                holder.mAcAccountNumber.setText(rec.AcAccountNumber.toString());
            }
            holder.mAcDescription.setText(rec.AcDescription.toString());

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
            holder.mAcMove.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && mDragStartListener != null)
                    {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
        try
        {
            Collections.swap(mDataset, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            MyDatabase.MyDB().accountResequence(mDataset);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

}
