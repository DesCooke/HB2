package com.example.cooked.hb2.CategoryUI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
{
    private ArrayList<RecordCategory> mDataset;
    private OnItemClickListener mOnItemClickListener;
    private OnItemClickListener mOnShowClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordCategory obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnShowClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnShowClickListener = mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mCategoryName;
        ImageButton btnEdit;
        ImageButton btnShow;

        ViewHolder(View v)
        {
            super(v);

            mCategoryName = v.findViewById(R.id.cellcat_CategoryName);

            btnEdit = v.findViewById(R.id.btnEdit);
            btnShow = v.findViewById(R.id.btnShowSubCategory);
        }
    }

    public CategoryAdapter(ArrayList<RecordCategory> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_category, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordCategory rec;

        rec = mDataset.get(position);

        holder.mCategoryName.setText(rec.CategoryName);
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
        holder.btnShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnShowClickListener != null)
                {
                    mOnShowClickListener.onItemClick(view, rec);
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
