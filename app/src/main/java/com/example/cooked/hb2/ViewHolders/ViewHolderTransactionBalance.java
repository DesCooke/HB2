package com.example.cooked.hb2.ViewHolders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.cooked.hb2.R;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class ViewHolderTransactionBalance extends RecyclerView.ViewHolder
{
    // each data item is just a string in this case
    public TextView mCellInfo;

    public ViewHolderTransactionBalance(View v)
    {
        super(v);

        mCellInfo =  v.findViewById(R.id.cell_info);
    }
}
