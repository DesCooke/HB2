package com.example.cooked.hb2;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordButton;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.R;

import java.util.ArrayList;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
    private ArrayList<RecordButton> mDataset;
    private OnItemClickListener mOnItemClickListener;
    
    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordButton obj);
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
        public ImageButton mImageButton;
        public TextView mTextView;
        
        public ViewHolder(View v)
        {
            super(v);
            
            mImageButton = v.findViewById(R.id.imageButton);
            mTextView = v.findViewById(R.id.buttonText);
        }
    }
    
    // Provide a suitable constructor (depends on the kind of dataset)
    public ImageAdapter(ArrayList<RecordButton> lDataset)
    {
        mDataset = lDataset;
    }
    
    // Create new views (invoked by the layout manager)
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_button, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    
    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RecordButton rec;
        
        rec = mDataset.get(position);
        
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try
        {
            holder.mImageButton.setImageResource(rec.button);
            holder.mImageButton.setOnClickListener(new View.OnClickListener()
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
            holder.mTextView.setText(rec.buttonText);
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in TransactionAdapter.onBindViewHolder", e.getMessage());
        }
    }
    
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
