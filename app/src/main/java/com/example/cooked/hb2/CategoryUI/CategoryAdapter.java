package com.example.cooked.hb2.CategoryUI;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.activityCategory;
import com.example.cooked.hb2.activityCategoryItem;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
{
    private ArrayList<RecordCategory> mDataset;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordCategory obj);
    }
    
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mCategoryName;
        public ImageButton btnEdit;
        
        public ViewHolder(View v)
        {
            super(v);

            try
            {
                mCategoryName = (TextView) v.findViewById(R.id.cellcat_CategoryName);
            
                btnEdit = (ImageButton) v.findViewById(R.id.btnEdit);
                
            }
            catch(Exception e)
            {
                ErrorDialog.Show("Error in CategoryAdapter.ViewHolder::ViewHolder", e.getMessage());
            }
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
        try
        {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_category, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryAdapter.ViewHolder::onCreateViewHolder", e.getMessage());
        }
        return(null);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordCategory rec;

        try
        {

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
            
            
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in CategoryAdapter.onBindViewHolder", e.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        try
        {
            return mDataset.size();
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in CategoryAdapter.getItemCount", e.getMessage());
        }
        return(0);
    }
}
