package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hb2.Budget.RecordBudgetClass;
import com.example.cooked.hb2.Budget.RecordBudgetGroup;
import com.example.cooked.hb2.Budget.RecordBudgetItem;
import com.example.cooked.hb2.Budget.RecordBudgetListItem;
import com.example.cooked.hb2.Budget.RecordBudgetMonth;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategoryBudget;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.cooked.hb2.MainActivity.context;

public class BudgetListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<RecordBudgetListItem> _items;
    public MainActivity lMainActivity;

    public interface OnItemClickListener {
        void onItemClick(View view, RecordBudgetListItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
    }

    public BudgetListSectioned(Context context, RecordBudgetMonth recordBudgetMonth) {
        _items = new ArrayList<>();
        for(int i = 0; i< recordBudgetMonth.budgetClasses.size(); i++)
        {
            RecordBudgetClass rbc= recordBudgetMonth.budgetClasses.get(i);

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
        View titleParent;
        TextView txtTotal;
        TextView txtSpent;
        TextView txtOutstanding;

        BudgetItemViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            titleParent = v.findViewById(R.id.titleParent);
            txtTotal = v.findViewById(R.id.txtTotal);
            txtSpent = v.findViewById(R.id.txtSpent);
            txtOutstanding = v.findViewById(R.id.txtOutstanding);
        }
    }

    public static class BudgetGroupViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageButton bt_expand;
        ImageButton bt_edit;
        View titleParent;
        TextView txtTotal;
        TextView txtSpent;
        TextView txtOutstanding;


        BudgetGroupViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            bt_expand = v.findViewById(R.id.bt_expand);
            bt_edit = v.findViewById(R.id.bt_edit);
            titleParent = v.findViewById(R.id.titleParent);
            txtTotal = v.findViewById(R.id.txtTotal);
            txtSpent = v.findViewById(R.id.txtSpent);
            txtOutstanding = v.findViewById(R.id.txtOutstanding);
        }
    }

    public static class BudgetClassViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageButton bt_expand;
        View titleParent;
        TextView txtTotal;
        TextView txtSpent;
        TextView txtOutstanding;

        BudgetClassViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            bt_expand = v.findViewById(R.id.bt_expand);
            titleParent = v.findViewById(R.id.titleParent);
            txtTotal = v.findViewById(R.id.txtTotal);
            txtSpent = v.findViewById(R.id.txtSpent);
            txtOutstanding = v.findViewById(R.id.txtOutstanding);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final RecordBudgetListItem rbli = _items.get(position);
        if (holder instanceof BudgetItemViewHolder) {
            final BudgetItemViewHolder view = (BudgetItemViewHolder) holder;

            view.titleParent.setVisibility(View.VISIBLE);
            view.title.setText(rbli.ItemName);

            final RecordBudgetItem rbi = (RecordBudgetItem)rbli.Data;
            view.txtTotal.setText(context.getString(R.string.total_line,Tools.moneyFormat(rbi.total)));
            view.txtSpent.setText(context.getString(R.string.spent_line,Tools.moneyFormat(rbi.spent)));
            view.txtOutstanding.setText(context.getString(R.string.outstanding_line,Tools.moneyFormat(rbi.outstanding)));
        }
        else
        {
            if (holder instanceof BudgetGroupViewHolder)
            {
                BudgetGroupViewHolder view = (BudgetGroupViewHolder) holder;
                view.title.setText(rbli.ItemName);
                final RecordBudgetGroup rbg = (RecordBudgetGroup)rbli.Data;
                view.bt_edit.setVisibility(View.GONE);
                if(rbg.groupedBudget)
                {
                    view.bt_edit.setTag(rbli);
                    view.bt_edit.setVisibility(View.VISIBLE);
                    view.bt_edit.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v)
                        {
                            RecordBudgetListItem rbli1 = (RecordBudgetListItem) v.getTag();
                            final int pos = _items.indexOf(rbli1);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Select a Budget for the Group");

                            final Float origAmount = rbg.total;
                            final EditText input = new EditText(context);
                            input.setText(String.format(Locale.ENGLISH, "%.2f", rbg.total));

                            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                    InputType.TYPE_NUMBER_FLAG_SIGNED);
                            builder.setView(input);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String m_Text = input.getText().toString();
                                    RecordCategoryBudget rcb = MyDatabase.MyDB().getCategoryBudget(
                                            rbg.CategoryId, rbg.BudgetMonth,
                                            rbg.BudgetYear);
                                    if(rcb.BudgetMonth==0)
                                    {
                                        Toast.makeText(context, "Budget for Category/Period Added", LENGTH_LONG).show();

                                        rcb.CategoryId = rbg.CategoryId;
                                        rcb.BudgetMonth = rbg.BudgetMonth;
                                        rcb.BudgetYear = rbg.BudgetYear;
                                        rcb.BudgetAmount = Float.valueOf(m_Text);

                                        MyDatabase.MyDB().addCategoryBudget(rcb);
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Budget for Category/Period Updated", LENGTH_LONG).show();
                                        rcb.BudgetAmount = Float.valueOf(m_Text);

                                        MyDatabase.MyDB().updateCategoryBudget(rcb);
                                    }
                                    rbg.total = Float.valueOf(m_Text);
                                    rbg.outstanding = rbg.total - rbg.spent;

                                    notifyItemChanged(pos);
                                    if(lMainActivity!=null)
                                        lMainActivity.fragmentsRefresh();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        }
                    });
                }
                view.txtTotal.setText(context.getString(R.string.total_line,Tools.moneyFormat(rbg.total)));
                view.txtSpent.setText(context.getString(R.string.spent_line,Tools.moneyFormat(rbg.spent)));
                view.txtOutstanding.setText(context.getString(R.string.outstanding_line,Tools.moneyFormat(rbg.outstanding)));
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

                            boolean show = toggleLayoutExpand(!rbg.Expanded, v);
                            if (!rbg.Expanded)
                            {
                                for (int i = rbg.budgetItems.size() - 1; i >= 0; i--)
                                {
                                    RecordBudgetItem rbi = rbg.budgetItems.get(i);
                                    RecordBudgetListItem rbli2 = new RecordBudgetListItem();
                                    rbli2.ItemType = context.getResources().getInteger(R.integer.budget_item);
                                    rbli2.ItemName = rbi.budgetItemName;
                                    rbli2.BudgetClassId = rbg.BudgetClassId;
                                    rbli2.BudgetGroupId = rbg.BudgetGroupId;
                                    rbli2.BudgetItemId = rbi.BudgetItemId;
                                    rbli2.Data = rbi;
                                    _items.add(pos + 1, rbli2);
                                    notifyItemInserted(pos + 1);
                                }
                            } else
                            {
                                int i=0;
                                while(i<_items.size())
                                {
                                    if(_items.get(i).BudgetClassId == rbli1.BudgetClassId &&
                                            _items.get(i).BudgetGroupId == rbli.BudgetGroupId &&
                                            _items.get(i).BudgetItemId > 0)
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
                view.txtTotal.setText(context.getString(R.string.total_line,Tools.moneyFormat(rbc.total)));
                view.txtSpent.setText(context.getString(R.string.spent_line,Tools.moneyFormat(rbc.spent)));
                view.txtOutstanding.setText(context.getString(R.string.outstanding_line,Tools.moneyFormat(rbc.outstanding)));
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
                            if (!rbc.Expanded)
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

    private boolean toggleLayoutExpand(boolean show, View view) {
        Tools.toggleArrow(show, view);
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
}
