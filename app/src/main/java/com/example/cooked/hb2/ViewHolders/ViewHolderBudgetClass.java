package com.example.cooked.hb2.ViewHolders;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.R;

public class ViewHolderBudgetClass extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageButton bt_expand;
    public View titleParent;
    public TextView txtTotal;
    public TextView txtSpent;
    public TextView txtOutstanding;

    public ViewHolderBudgetClass(View v) {
        super(v);
        title = v.findViewById(R.id.title);
        bt_expand = v.findViewById(R.id.bt_expand);
        titleParent = v.findViewById(R.id.titleParent);
        txtTotal = v.findViewById(R.id.txtTotal);
        txtSpent = v.findViewById(R.id.txtSpent);
        txtOutstanding = v.findViewById(R.id.txtOutstanding);
    }
}

