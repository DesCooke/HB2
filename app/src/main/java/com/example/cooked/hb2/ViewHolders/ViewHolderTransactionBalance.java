package com.example.cooked.hb2.ViewHolders;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
