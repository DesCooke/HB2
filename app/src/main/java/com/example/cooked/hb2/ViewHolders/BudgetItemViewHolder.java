package com.example.cooked.hb2.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.cooked.hb2.R;

public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public View titleParent;
    public TextView txtTotal;
    public TextView txtSpent;
    public TextView txtOutstanding;

    public BudgetItemViewHolder(View v) {
        super(v);
        title = v.findViewById(R.id.title);
        titleParent = v.findViewById(R.id.titleParent);
        txtTotal = v.findViewById(R.id.txtTotal);
        txtSpent = v.findViewById(R.id.txtSpent);
        txtOutstanding = v.findViewById(R.id.txtOutstanding);
    }
}
