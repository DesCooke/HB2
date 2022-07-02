package com.example.cooked.hb2.ViewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooked.hb2.R;

public class ViewHolderSubCategoryByMonth extends RecyclerView.ViewHolder
{
        public TextView txtYearPeriod;
        public TextView txtCount;
        public TextView txtAmount;
        public View titleParent;

        public ViewHolderSubCategoryByMonth(View v) {
            super(v);
            txtYearPeriod = v.findViewById(R.id.txtYearPeriod);
            txtCount = v.findViewById(R.id.txtCount);
            txtAmount = v.findViewById(R.id.txtAmount);
            titleParent = v.findViewById(R.id.scbmParent);
        }
}
