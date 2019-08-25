package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.Budget.RecordBudgetListItem;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final int BUDGET_CLASS = 0;
    private final int BUDGET_GROUP = 1;
    private final int BUDGET_ITEM = 2;
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
            RecordBudgetGroup rbg=_rbm.budgetGroups.get(i);

            RecordBudgetListItem rbli=new RecordBudgetListItem();
            rbli.ItemType = BUDGET_GROUP;
            if(rbg.divider)
                rbli.ItemType = BUDGET_CLASS;
            rbli.ItemName = rbg.budgetGroupName;
            rbli.Data = rbg;
            rbli.Expanded = new Boolean(false);
            _items.add(rbli);
            for(int j=0;j<rbg.budgetItems.size();j++)
            {
                RecordBudgetItem rbi=rbg.budgetItems.get(j);
                rbli=new RecordBudgetListItem();
                rbli.ItemType = BUDGET_ITEM;
                rbli.ItemName = rbi.budgetItemName;
                rbli.Data = rbi;
                rbli.Expanded = new Boolean(false);
                _items.add(rbli);
            }
        }
    }

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public View titleParent;

        public BudgetItemViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            titleParent = v.findViewById(R.id.titleParent);
        }
    }

    public static class BudgetGroupViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton bt_expand;

        public BudgetGroupViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            bt_expand = v.findViewById(R.id.bt_expand);
        }
    }

    public static class BudgetClassViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton bt_expand;

        public BudgetClassViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            bt_expand = v.findViewById(R.id.bt_expand);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == BUDGET_CLASS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_class, parent, false);
            vh = new BudgetClassViewHolder(v);
        }
        else
        {
            if(viewType==BUDGET_GROUP)
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_group, parent, false);
                vh = new BudgetGroupViewHolder(v);
            }
            else
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_item, parent, false);
                vh = new BudgetItemViewHolder(v);
            }
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RecordBudgetListItem rbli = _items.get(position);
        if (holder instanceof BudgetItemViewHolder) {
            final BudgetItemViewHolder view = (BudgetItemViewHolder) holder;

            view.title.setText(rbli.ItemName);
            view.titleParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, _items.get(position), position);
                    }
                }
            });
        }
        else
        {
            if (holder instanceof BudgetGroupViewHolder)
            {
                BudgetGroupViewHolder view = (BudgetGroupViewHolder) holder;
                view.title.setText(rbli.ItemName);
                if(view.bt_expand!=null)
                {
                    view.bt_expand.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            boolean show = toggleLayoutExpand(!rbli.Expanded, v);
                            _items.get(position).Expanded = show;
                        }
                    });
                }
            }
            else
            {
                BudgetClassViewHolder view = (BudgetClassViewHolder) holder;
                view.title.setText(rbli.ItemName);
                if(view.bt_expand!=null)
                {
                    view.bt_expand.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            boolean show = toggleLayoutExpand(!rbli.Expanded, v);
                            _items.get(position).Expanded = show;
                        }
                    });
                }
            }
        }
    }

    private boolean toggleLayoutExpand(boolean show, View view) {
        Tools.toggleArrow(show, view);
//        if (show) {
//            ViewAnimation.expand(lyt_expand);
//        } else {
//            ViewAnimation.collapse(lyt_expand);
//        }
        return show;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return this._items.get(position).ItemType;
    }
/*
    public void insertItem(int index, People people){
        items.add(index, people);
        notifyItemInserted(index);

    }
*/
}
