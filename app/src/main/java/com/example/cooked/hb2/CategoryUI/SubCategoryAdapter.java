package com.example.cooked.hb2.CategoryUI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>
{
    private ArrayList<RecordSubCategory> mDataset;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordSubCategory obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mSubCategoryName;
        ImageButton btnEdit;

        ViewHolder(View v)
        {
            super(v);

            mSubCategoryName = v.findViewById(R.id.cellcat_SubCategoryName);

            btnEdit = v.findViewById(R.id.btnEdit);
        }
    }

    public SubCategoryAdapter(ArrayList<RecordSubCategory> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_subcategory, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordSubCategory rec;

        rec = mDataset.get(position);

        holder.mSubCategoryName.setText(rec.SubCategoryName);
        holder.btnEdit.setOnClickListener(new View.OnClickListener()
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
