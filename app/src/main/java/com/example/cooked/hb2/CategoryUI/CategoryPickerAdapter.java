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
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.activityCategory;
import com.example.cooked.hb2.activityCategoryItem;

import java.util.ArrayList;

public class CategoryPickerAdapter extends RecyclerView.Adapter<CategoryPickerAdapter.ViewHolder>
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

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mCategoryName;
        public TextView mSubCategoryName;
        public ImageButton btnEdit;

        public ViewHolder(View v)
        {
            super(v);

            try
            {
                mSubCategoryName = (TextView) v.findViewById(R.id.cellcat_SubCategoryName);
                mCategoryName = (TextView) v.findViewById(R.id.cellcat_CategoryName);

                btnEdit = (ImageButton) v.findViewById(R.id.btnEdit);
                btnEdit.setVisibility(View.GONE);

            }
            catch(Exception e)
            {
                ErrorDialog.Show("Error in CategoryPickerAdapter.ViewHolder::ViewHolder", e.getMessage());
            }
        }
    }

    public CategoryPickerAdapter(ArrayList<RecordSubCategory> lDataset)
    {
        mDataset = lDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType)
    {
        try
        {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_category_picker, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in CategoryPickerAdapter.ViewHolder::onCreateViewHolder", e.getMessage());
        }
        return(null);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordSubCategory rec;

        try
        {

            rec = mDataset.get(position);

            holder.mCategoryName.setText(rec.CategoryName);
            holder.mSubCategoryName.setText(rec.SubCategoryName);

            holder.mSubCategoryName.setOnClickListener(new View.OnClickListener()
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
            ErrorDialog.Show("Error in CategoryPickerAdapter.onBindViewHolder", e.getMessage());
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
            ErrorDialog.Show("Error in CategoryPickerAdapter.getItemCount", e.getMessage());
        }
        return(0);
    }
}
