package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordBudgetClass;
import com.example.cooked.hb2.Records.RecordBudgetGroup;
import com.example.cooked.hb2.Records.RecordBudgetItem;
import com.example.cooked.hb2.Records.RecordBudgetListItem;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategoryBudget;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.ViewHolders.ViewHolderBudgetClass;
import com.example.cooked.hb2.ViewHolders.ViewHolderBudgetGroup;
import com.example.cooked.hb2.ViewHolders.ViewHolderBudgetItem;
import com.example.cooked.hb2.activityTransactionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.cooked.hb2.MainActivity.context;

public class BudgetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context _context;
    private List<RecordBudgetListItem> _items;
    public MainActivity lMainActivity;
    public RecordBudgetMonth _rbm;

    public interface OnItemClickListener {
        void onItemClick(View view, RecordBudgetListItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
    }

    private void addClassToList(RecordBudgetClass argRbc, List<RecordBudgetListItem> items)
    {
        MyLog.WriteLogMessage("BudgetListAdapter:addClassToList:Starting");
        RecordBudgetListItem rbli=new RecordBudgetListItem();
        rbli.ItemType = context.getResources().getInteger(R.integer.item_type_budget_class);
        rbli.ItemName = argRbc.budgetClassName;
        rbli.Data = argRbc;
        rbli.BudgetClassId = argRbc.BudgetClassId;
        rbli.BudgetGroupId = 0;
        rbli.BudgetItemId = 0;
        items.add(rbli);
        MyLog.WriteLogMessage("BudgetListAdapter:addClassToList:Ending");
    }

    private void addGroupToList(int argIndex, RecordBudgetGroup argRbg, List<RecordBudgetListItem> items)
    {
        MyLog.WriteLogMessage("BudgetListAdapter:addGroupToList:Starting");
        RecordBudgetListItem rbli=new RecordBudgetListItem();
        rbli.ItemType = context.getResources().getInteger(R.integer.item_type_budget_group);
        rbli.ItemName = argRbg.budgetGroupName;
        rbli.Data = argRbg;
        rbli.BudgetClassId = argRbg.BudgetClassId;
        rbli.BudgetGroupId = argRbg.BudgetGroupId;
        rbli.BudgetItemId = 0;
        if(argIndex==-1)
        {
            items.add(rbli);
        }
        else
        {
            items.add(argIndex, rbli);
        }
        MyLog.WriteLogMessage("BudgetListAdapter:addGroupToList:Ending");
    }

    private void addItemToList(int argIndex, RecordBudgetItem argRbi, List<RecordBudgetListItem> items)
    {
        MyLog.WriteLogMessage("BudgetListAdapter:addItemToList:Starting");
        RecordBudgetListItem rbli=new RecordBudgetListItem();
        rbli.ItemType = context.getResources().getInteger(R.integer.item_type_budget_item);
        rbli.ItemName = argRbi.budgetItemName;
        rbli.Data = argRbi;
        rbli.BudgetClassId = argRbi.BudgetClassId;
        rbli.BudgetGroupId = argRbi.BudgetGroupId;
        rbli.BudgetItemId = argRbi.BudgetItemId;
        if(argIndex==-1)
        {
            items.add(rbli);
        }
        else
        {
            items.add(argIndex, rbli);
        }
        MyLog.WriteLogMessage("BudgetListAdapter:addItemToList:Ending");
    }

    private List<RecordBudgetListItem> createListFromRecordBudgetMonth(RecordBudgetMonth rbm)
    {
        MyLog.WriteLogMessage("BudgetListAdapter:createListFromRecordBudgetMonth:Starting");
        List<RecordBudgetListItem> items = new ArrayList<>();
        for(int i = 0; i< rbm.budgetClasses.size(); i++)
        {
            RecordBudgetClass rbc = rbm.budgetClasses.get(i);
            addClassToList(rbc, items);
            if(rbc.Expanded)
            {
                for(int j=0;j<rbc.budgetGroups.size();j++)
                {
                    RecordBudgetGroup rbg = rbc.budgetGroups.get(j);

                    addGroupToList(-1, rbg, items);

                    if(rbg.Expanded)
                    {
                        for(int k=0;k<rbg.budgetItems.size();k++)
                        {
                            RecordBudgetItem rbi = rbg.budgetItems.get(k);
                            rbi.BudgetGroupName = rbg.budgetGroupName;

                            addItemToList(-1, rbi, items);
                        }
                    }
                }
            }
        }
        MyLog.WriteLogMessage("BudgetListAdapter:createListFromRecordBudgetMonth:Ending");
        return(items);
    }

    public BudgetListAdapter(Context context, RecordBudgetMonth rbm) {
        _context = context;
        _items = createListFromRecordBudgetMonth(rbm);
        _rbm = rbm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder vh;
        Resources r = Objects.requireNonNull(MyResources.R());

        MyLog.WriteLogMessage("BudgetListAdapter:onCreateViewHolder:Starting");

        if (viewType == r.getInteger(R.integer.item_type_budget_class))
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_class, parent, false);
            vh = new ViewHolderBudgetClass(v);
        }
        else
        {
            if(viewType==r.getInteger(R.integer.item_type_budget_group))
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_group, parent, false);
                vh = new ViewHolderBudgetGroup(v);
            }
            else
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_budget_item, parent, false);
                vh = new ViewHolderBudgetItem(v);
            }
        }
        MyLog.WriteLogMessage("BudgetListAdapter:onCreateViewHolder:Ending");
        return vh;
    }

    private void handleDropDown(View v)
    {
        MyLog.WriteLogMessage("BudgetListAdapter:handleDropDown:Starting");

        RecordBudgetListItem rbli = (RecordBudgetListItem) v.getTag();
        int pos = _items.indexOf(rbli);

        if(rbli.Data instanceof RecordBudgetGroup)
        {
            RecordBudgetGroup rbg = (RecordBudgetGroup) rbli.Data;

            boolean show = toggleLayoutExpand(!rbg.Expanded, v);
            if (!rbg.Expanded)
            {
                for (int i = rbg.budgetItems.size() - 1; i >= 0; i--)
                {
                    RecordBudgetItem rbi = rbg.budgetItems.get(i);
                    rbi.BudgetGroupName = rbg.budgetGroupName;

                    addItemToList(pos + 1, rbi, _items);
                    notifyItemInserted(pos + 1);
                }
            }
            else
            {
                int i = 0;
                while (i < _items.size())
                {
                    if (_items.get(i).BudgetClassId == rbli.BudgetClassId &&
                            _items.get(i).BudgetGroupId == rbli.BudgetGroupId &&
                            _items.get(i).BudgetItemId > 0)
                    {
                        _items.remove(i);
                        notifyItemRemoved(i);
                    } else
                    {
                        i++;
                    }
                }
            }

            rbg.Expanded = show;
        }

        if(rbli.Data instanceof RecordBudgetClass)
        {
            RecordBudgetClass rbc = (RecordBudgetClass)rbli.Data;

            boolean show = toggleLayoutExpand(!rbc.Expanded, v);
            if (!rbc.Expanded)
            {
                for (int i = rbc.budgetGroups.size() - 1; i >= 0; i--)
                {
                    RecordBudgetGroup rbg = rbc.budgetGroups.get(i);
                    addGroupToList(pos+1, rbg, _items);
                    notifyItemInserted(pos + 1);
                }
            } else
            {
                int i=0;
                while(i<_items.size())
                {
                    if(_items.get(i).BudgetClassId == rbli.BudgetClassId &&
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
                for (int j = 0; j< rbc.budgetGroups.size(); j++)
                {
                    RecordBudgetGroup rbg = rbc.budgetGroups.get(j);
                    rbg.Expanded = false;
                }
            }

            rbc.Expanded = show;
        }
        MyLog.WriteLogMessage("BudgetListAdapter:handleDropDown:Ending");
    }

    private void handleEditGroupedBudget(View v)
    {
        MyLog.WriteLogMessage("BudgetListAdapter:handleEditGroupedBudget:Starting");

        RecordBudgetListItem rbli1 = (RecordBudgetListItem) v.getTag();
        final int pos = _items.indexOf(rbli1);
        if(pos<0)
            return;

        final RecordBudgetGroup rbg;
        rbg = (RecordBudgetGroup)_items.get(pos).Data;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a Budget for the Group");

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
                    lMainActivity.RefreshFragments(rbg.BudgetYear, rbg.BudgetMonth);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        MyLog.WriteLogMessage("BudgetListAdapter:handleEditGroupedBudget:Ending");
    }

    public void refreshUI()
    {
        MyLog.WriteLogMessage("BudgetListAdapter:refreshUI:Starting");
        MyLog.WriteLogMessage("BudgetListAdapter:refreshUI:Ending");
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyLog.WriteLogMessage("BudgetListAdapter:onBindViewHolder:Starting");
        final RecordBudgetListItem rbli = _items.get(position);
        if (holder instanceof ViewHolderBudgetItem) {
            final ViewHolderBudgetItem view = (ViewHolderBudgetItem) holder;

            view.titleParent.setVisibility(View.VISIBLE);
            view.title.setText(rbli.ItemName);

            final RecordBudgetItem rbi = (RecordBudgetItem)rbli.Data;
            view.txtTotal.setText(context.getString(R.string.total_line,Tools.moneyFormat(rbi.total)));
            view.txtSpent.setText(context.getString(R.string.spent_line,Tools.moneyFormat(rbi.spent)));
            view.txtOutstanding.setText(context.getString(R.string.outstanding_line,Tools.moneyFormat(rbi.outstanding)));
            view.titleParent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(_context, activityTransactionList.class);
                    intent.putExtra("CAPTION", "Transactions");
                    intent.putExtra("LINE1", "For Category " + rbi.CategoryName);
                    intent.putExtra("LINE2", "For Sub Category " + rbi.SubCategoryName);
                    intent.putExtra("LINE3", "For Budget " + _rbm.budgetMonth + "/" + _rbm.budgetYear);
                    intent.putExtra("BUDGETYEAR", _rbm.budgetYear);
                    intent.putExtra("BUDGETMONTH", _rbm.budgetMonth);
                    intent.putExtra("CATEGORYID", 0);
                    intent.putExtra("SUBCATEGORYID", rbi.SubCategoryId);
                    _context.startActivity(intent);
                }
            });
            if(rbi.groupedBudget)
            {
                view.txtTotal.setVisibility(View.GONE);
                view.txtOutstanding.setVisibility(View.GONE);
            }
        }
        else
        {
            if (holder instanceof ViewHolderBudgetGroup)
            {
                ViewHolderBudgetGroup view = (ViewHolderBudgetGroup) holder;
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
                            handleEditGroupedBudget(v);
                        }
                    });
                }
                view.txtTotal.setText(context.getString(R.string.total_line,Tools.moneyFormat(rbg.total)));
                view.txtSpent.setText(context.getString(R.string.spent_line,Tools.moneyFormat(rbg.spent)));
                view.txtOutstanding.setText(context.getString(R.string.outstanding_line,Tools.moneyFormat(rbg.outstanding)));
                view.titleParent.setVisibility(View.VISIBLE);

                if(view.bt_expand!=null)
                {
                    if(rbg.Expanded)
                    {
                        Tools.setAsExpanded(view.bt_expand, false);
                    }
                    else
                    {
                        Tools.setAsClosed(view.bt_expand, false);
                    }
                    view.bt_expand.setTag(rbli);
                    view.bt_expand.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            handleDropDown(v);
                        }
                    });
                }
            }
            else
            {
                ViewHolderBudgetClass view = (ViewHolderBudgetClass) holder;
                view.title.setText(rbli.ItemName);
                final RecordBudgetClass rbc = (RecordBudgetClass)rbli.Data;
                view.txtTotal.setText(context.getString(R.string.total_line,Tools.moneyFormat(rbc.total)));
                view.txtSpent.setText(context.getString(R.string.spent_line,Tools.moneyFormat(rbc.spent)));
                view.txtOutstanding.setText(context.getString(R.string.outstanding_line,Tools.moneyFormat(rbc.outstanding)));
                view.titleParent.setVisibility(View.VISIBLE);
                if(view.bt_expand!=null)
                {
                    if(rbc.Expanded)
                    {
                        Tools.setAsExpanded(view.bt_expand, false);
                    }
                    else
                    {
                        Tools.setAsClosed(view.bt_expand, false);
                    }

                    view.bt_expand.setTag(rbli);
                    view.bt_expand.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            handleDropDown(v);
                        }
                    });
                }
            }
        }
        MyLog.WriteLogMessage("BudgetListAdapter:onBindViewHolder:Ending");
    }

    private boolean toggleLayoutExpand(boolean show, View view)
    {
        Tools.toggleArrow(show, view);
        return show;
    }

    @Override
    public int getItemCount()
    {
        return _items.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return this._items.get(position).ItemType;
    }
}
