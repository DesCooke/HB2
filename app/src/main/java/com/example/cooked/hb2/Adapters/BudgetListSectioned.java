package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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
            rbli.ItemName = rbc.budgetClassName;
            rbli.Data = rbc;
            rbli.BudgetClassId = rbc.BudgetClassId;
            rbli.BudgetGroupId = 0;
            rbli.BudgetItemId = 0;
            _items.add(rbli);
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
        public int index;

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
                final RecordBudgetGroup rbg= (RecordBudgetGroup)rbli.Data;

                view.titleParent.setVisibility(View.VISIBLE);

                view.title.setText(rbli.ItemName);
                if(view.bt_expand!=null)
                {
                    view.bt_expand.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            boolean show = toggleLayoutExpand(!rbg.Expanded, v);
                            rbg.Expanded = show;
                        }
                    });
                }
            }
            else
            {
                BudgetClassViewHolder view = (BudgetClassViewHolder) holder;
                view.title.setText(rbli.ItemName);
                final RecordBudgetClass rbc = (RecordBudgetClass)rbli.Data;
                view.txtTotal.setText("Total: " + Tools.moneyFormat(rbc.total));
                view.txtSpent.setText("Spent: " + Tools.moneyFormat(rbc.spent));
                view.txtOutstanding.setText("Outstanding: " + Tools.moneyFormat(rbc.outstanding));
                view.titleParent.setVisibility(View.VISIBLE);
                if(view.bt_expand!=null)
                {
                    view.bt_expand.setTag(rbli);
                    view.bt_expand.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            RecordBudgetListItem rbli1 = (RecordBudgetListItem) v.getTag();
                            int pos = _items.indexOf(rbli1);

                            boolean show = toggleLayoutExpand(!rbc.Expanded, v);
                            if (rbc.Expanded == false)
                            {
                                for (int i = rbc.budgetGroups.size() - 1; i >= 0; i--)
                                {
                                    RecordBudgetGroup rbg = rbc.budgetGroups.get(i);
                                    RecordBudgetListItem rbli2 = new RecordBudgetListItem();
                                    rbli2.ItemType = context.getResources().getInteger(R.integer.budget_group);
                                    rbli2.ItemName = rbg.budgetGroupName;
                                    rbli2.BudgetClassId = rbc.BudgetClassId;
                                    rbli2.BudgetGroupId = rbg.BudgetGroupId;
                                    rbli2.BudgetItemId = 0;
                                    rbli2.Data = rbg;
                                    _items.add(pos + 1, rbli2);
                                    notifyItemInserted(pos + 1);
                                }
                            } else
                            {
                                int i=0;
                                while(i<_items.size())
                                {
                                    if(_items.get(i).BudgetClassId == rbli1.BudgetClassId &&
                                            _items.get(i).BudgetGroupId > 0)
                                    {
                                        _items.remove(i);
                                        notifyItemRemoved(i);
                                    }
                                    else
                                    {
                                        i++;
                                    }
                                }
                            }

                            rbc.Expanded = show;
                        }
                    });
                }
            }
        }
    }

    private RecyclerView getRecyclerView(View v)
    {
        ViewParent p=v.getParent();
        if(p instanceof RecyclerView)
            return((RecyclerView)p);
        while(p!=null && ! (p instanceof RecyclerView))
        {
            p=p.getParent();
            if(p==null)
                return(null);
            if(p instanceof RecyclerView)
                return((RecyclerView)p);
        }
        return(null);
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
