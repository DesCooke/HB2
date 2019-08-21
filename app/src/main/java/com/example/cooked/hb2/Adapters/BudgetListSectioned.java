package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetListItem;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private RecordBudgetMonth _rbm;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private List<RecordBudgetListItem> _items;

    public interface OnItemClickListener {
        void onItemClick(View view, RecordBudgetListItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public BudgetListSectioned(Context context, RecordBudgetMonth recordBudgetMonth) {
        _rbm = recordBudgetMonth;
        ctx = context;
        _items = new ArrayList<>();
        for(int i=0;i<_rbm.budgetGroups.size();i++)
        {
            RecordBudgetListItem rbli=new RecordBudgetListItem();
            rbli.ItemType = VIEW_SECTION;
            rbli.ItemName = _rbm.budgetGroups.get(i).budgetGroupName;
            rbli.Data = _rbm.budgetGroups.get(i);
            _items.add(rbli);
            for(int j=0;j<_rbm.budgetGroups.get(i).budgetItems.size();j++)
            {
                rbli=new RecordBudgetListItem();
                rbli.ItemType = VIEW_ITEM;
                rbli.ItemName = _rbm.budgetGroups.get(i).budgetItems.get(j).budgetItemName;
                rbli.Data = _rbm.budgetGroups.get(i).budgetItems.get(j);
            }
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public View titleParent;

        public OriginalViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            titleParent = v.findViewById(R.id.titleParent);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public SectionViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_item, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_group, parent, false);
            vh = new SectionViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RecordBudgetListItem rbli = _items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.title.setText(rbli.ItemName);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, _items.get(position), position);
                    }
                }
            });
        } else {
            SectionViewHolder view = (SectionViewHolder) holder;
            view.title.setText(rbli.ItemName);
        }
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this._items.get(position).ItemType;
    }
/*
    public void insertItem(int index, People people){
        items.add(index, people);
        notifyItemInserted(index);

    }
*/
}
