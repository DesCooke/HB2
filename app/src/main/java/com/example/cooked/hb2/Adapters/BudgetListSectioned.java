package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cooked.hb2.Budget.RecordBudgetClass;
import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.Budget.RecordBudgetListItem;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.cooked.hb2.MainActivity.context;

public class BudgetListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

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
        for(int i=0;i<_rbm.budgetClasses.size();i++)
        {
            RecordBudgetClass rbc=_rbm.budgetClasses.get(i);

            RecordBudgetListItem rbli=new RecordBudgetListItem();
            rbli.ItemType = context.getResources().getInteger(R.integer.budget_class);
            rbli.Expanded = false;
            rbli.Visible = true;
            rbli.ItemName = rbc.budgetClassName;
            rbli.Data = rbc;
            _items.add(rbli);

            for(int j=0;j<rbc.budgetGroups.size();j++)
            {
                RecordBudgetGroup rbg=rbc.budgetGroups.get(j);
                rbli=new RecordBudgetListItem();
                rbli.ItemType = context.getResources().getInteger(R.integer.budget_group);
                rbli.Expanded = false;
                rbli.Visible = false;
                rbli.ItemName = rbg.budgetGroupName;
                rbli.Data = rbg;
                _items.add(rbli);

                for (int k = 0; k < rbg.budgetItems.size(); k++)
                {
                    RecordBudgetItem rbi = rbg.budgetItems.get(k);
                    rbli = new RecordBudgetListItem();
                    rbli.ItemType = context.getResources().getInteger(R.integer.budget_item);
                    rbli.ItemName = rbi.budgetItemName;
                    rbli.Data = rbi;
                    rbli.Expanded = false;
                    rbli.Visible = false;
                    _items.add(rbli);
                }
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
        public View titleParent;

        public BudgetGroupViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            bt_expand = v.findViewById(R.id.bt_expand);
            titleParent = v.findViewById(R.id.titleParent);
        }
    }

    public static class BudgetClassViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton bt_expand;
        public View titleParent;
        public TextView txtTotal;
        public TextView txtSpent;
        public TextView txtOutstanding;

        public BudgetClassViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            bt_expand = v.findViewById(R.id.bt_expand);
            titleParent = v.findViewById(R.id.titleParent);
            txtTotal = v.findViewById(R.id.txtTotal);
            txtSpent = v.findViewById(R.id.txtSpent);
            txtOutstanding = v.findViewById(R.id.txtOutstanding);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == context.getResources().getInteger(R.integer.budget_class)) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_class, parent, false);
            vh = new BudgetClassViewHolder(v);
        }
        else
        {
            if(viewType==context.getResources().getInteger(R.integer.budget_group))
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

            view.titleParent.setVisibility(View.VISIBLE);
            if(rbli.Visible==false)
                view.titleParent.setVisibility(View.GONE);

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

                view.titleParent.setVisibility(View.VISIBLE);
                if(rbli.Visible==false)
                    view.titleParent.setVisibility(View.GONE);

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
                RecordBudgetClass rbc = (RecordBudgetClass)rbli.Data;
                view.txtTotal.setText("Total: " + Tools.moneyFormat(rbc.total));
                view.txtSpent.setText("Spent: " + Tools.moneyFormat(rbc.spent));
                view.txtOutstanding.setText("Outstanding: " + Tools.moneyFormat(rbc.outstanding));

                view.titleParent.setVisibility(View.VISIBLE);
                if(rbli.Visible==false)
                    view.titleParent.setVisibility(View.GONE);

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
