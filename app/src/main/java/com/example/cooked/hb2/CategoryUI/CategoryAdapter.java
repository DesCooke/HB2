package com.example.cooked.hb2.CategoryUI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
{
    private ArrayList<RecordCategory> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mCategoryName;

        public ViewHolder(View v)
        {
            super(v);

            mCategoryName = (TextView) v.findViewById(R.id.cellcat_CategoryName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
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
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        RecordCategory rec;

        rec = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {


            holder.mCategoryName.setText(rec.CategoryName);
        }
        catch (Exception e) {
            ErrorDialog.Show("Error in NounAdapter.onBindViewHolder", e.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
