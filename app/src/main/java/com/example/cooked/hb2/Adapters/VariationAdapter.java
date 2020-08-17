package com.example.cooked.hb2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordPlannedVariation;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyString;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class VariationAdapter extends RecyclerView.Adapter<VariationAdapter.ViewHolder>
{
    private ArrayList<RecordPlannedVariation> mDataset;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordPlannedVariation obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mVariationName;
        TextView mEffDate;
        TextView mAmount;
        LinearLayout mFull;

        ViewHolder(View v)
        {
            super(v);

            mVariationName = v.findViewById(R.id.tv_variation_name);
            mEffDate = v.findViewById(R.id.tv_eff_date);
            mAmount = v.findViewById(R.id.tv_amount);
            mFull = v.findViewById(R.id.cell_variation_full);
        }
    }

    public VariationAdapter(ArrayList<RecordPlannedVariation> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VariationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_variation_item, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordPlannedVariation rec;

        rec = mDataset.get(position);

        holder.mVariationName.setText(rec.mVariationName);
        MyString myString = new MyString();
        if (!dateUtils().DateToStr(rec.mEffDate, myString))
            return;
        holder.mEffDate.setText(myString.Value);
        String lText = String.format(Locale.UK, "%.2f", rec.mAmount);
        holder.mAmount.setText(lText);

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
