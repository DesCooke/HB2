package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.Records.RecordTransactionListItem;
import com.example.cooked.hb2.ViewHolders.ViewHolderTransactionItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static com.example.cooked.hb2.MainActivity.context;

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<RecordTransactionListItem> _items;
    public MainActivity lMainActivity;

    public interface OnItemClickListener {
        void onItemClick(View view, RecordTransactionListItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
    }

    private void addTransactionToList(RecordTransaction argrt, List<RecordTransactionListItem> items)
    {
        RecordTransactionListItem rtli=new RecordTransactionListItem();
        rtli.ItemType = MyResources.R().getInteger(R.integer.item_type_transaction_item);
        rtli.Data = argrt;
        items.add(rtli);
    }

    private List<RecordTransactionListItem> createListFromTransactionList(List<RecordTransaction> rtl)
    {
        List<RecordTransactionListItem> items = new ArrayList<>();
        for(int i = 0; i< rtl.size(); i++)
        {
            RecordTransaction rt = rtl.get(i);
            addTransactionToList(rt, items);
        }
        return(items);
    }

    public TransactionListAdapter(Context context, List<RecordTransaction> rtl) {
        _items = createListFromTransactionList(rtl);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder vh=null;

        if (viewType == MyResources.R().getInteger(R.integer.item_type_transaction_item)) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_transaction_item, parent, false);
            vh = new ViewHolderTransactionItem(v);
        }
        return vh;
    }
/*
    private void handleDropDown(View v)
    {
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
    }

 */

/*
    private void handleEditGroupedBudget(View v)
    {
        RecordBudgetListItem rbli1 = (RecordBudgetListItem) v.getTag();
        final int pos = _items.indexOf(rbli1);
        if(pos<0)
            return;

        final RecordBudgetGroup rbg;
        rbg = (RecordBudgetGroup)_items.get(pos).Data;

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

 */
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        final RecordTransactionListItem rtli = _items.get(position);
        final RecordTransaction rti = (RecordTransaction)rtli.Data;
        if (holder instanceof ViewHolderTransactionItem) {
            final ViewHolderTransactionItem view = (ViewHolderTransactionItem) holder;

            view.mTxSeqNo.setText(rti.TxSeqNo.toString());
            view.mTxAdded.setText(rti.TxAdded.toString());
            view.mTxFilename.setText(rti.TxFilename);
            view.mTxLineNo.setText(rti.TxLineNo.toString());
            view.mTxDate.setText(android.text.format.DateFormat.format("EEE, dd/MM/yyyy", rti.TxDate));
            view.mTxType.setText(rti.TxType);
            view.mBankAccount.setText(rti.TxSortCode + " " + rti.TxAccountNumber);
            if (rti.TxDescription.length() > 0) {
                view.mTxDescription.setVisibility(View.VISIBLE);
                view.mTxDescription.setText("Description: " + rti.TxDescription);
            } else {
                view.mTxDescription.setVisibility(View.GONE);
            }
            view.mSubCategoryName.setText("Category: " + rti.SubCategoryName);

            if (rti.Comments.length() > 0) {
                view.mComments.setVisibility(View.VISIBLE);
                view.mComments.setText("Comments: " + rti.Comments);
            } else {
                view.mComments.setVisibility(View.GONE);
            }
            view.mBudget.setText(dateUtils().BudgetAsString(rti.BudgetYear, rti.BudgetMonth));
            if (rti.TxAmount < 0.00) {
                view.mTxAmount.setText("Amount -£" + String.format("%.2f", rti.TxAmount * -1));
            } else {
                view.mTxAmount.setText("Amount £" + String.format("%.2f", rti.TxAmount));
            }
            if (rti.HideBalance) {
                view.mTxBalance.setVisibility(View.INVISIBLE);
            } else {
                view.mTxBalance.setVisibility(View.VISIBLE);
                if (rti.TxBalance < 0.00) {
                    view.mTxBalance.setText("Balance -£" + String.format("%.2f", rti.TxBalance * -1));
                } else {
                    view.mTxBalance.setText("Balance £" + String.format("%.2f", rti.TxBalance));
                }
            }
            view.mFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, rti);
                    }

                     */
                }
            });
            int lColor = MainActivity.context.getResources().getColor(R.color.textNormal);
            if (rti.TxType.compareTo("Planned") == 0)
                lColor = MainActivity.context.getResources().getColor(R.color.textLowLight);

            view.mTxSeqNo.setTextColor(lColor);
            view.mTxAdded.setTextColor(lColor);
            view.mTxFilename.setTextColor(lColor);
            view.mTxLineNo.setTextColor(lColor);
            view.mTxDate.setTextColor(lColor);
            view.mTxType.setTextColor(lColor);
            view.mBankAccount.setTextColor(lColor);
            view.mTxDescription.setTextColor(lColor);
            view.mTxAmount.setTextColor(lColor);
            view.mTxBalance.setTextColor(lColor);
            view.mSubCategoryName.setTextColor(lColor);
            view.mComments.setTextColor(lColor);
            view.mBudget.setTextColor(lColor);

            if (rti.BalanceCorrect == false) {
                //holder.mTxAmount.setTextColor(MainActivity.context.getResources().getColor(R.color.textError));
                //holder.mTxBalance.setTextColor(MainActivity.context.getResources().getColor(R.color.textError));
                if (view.mTxBalance.getVisibility() == View.VISIBLE) {
                    if (rti.TxBalance < 0.00) {
//                      holder.mTxBalance.setText("Balance -£" + String.format("%.2f", rec.TxBalance * -1) + " -> " +
//                        ", -£" + String.format("%.2f", rec.TxBalanceShouldBe * -1));
                        view.mTxBalance.setText("Balance -£" + String.format("%.2f", rti.TxBalance * -1));
                    } else {
//                      holder.mTxBalance.setText("Balance £" + String.format("%.2f", rec.TxBalance) + " -> " +
//                        "£" + String.format("%.2f", rec.TxBalanceShouldBe));
                        view.mTxBalance.setText("Balance £" + String.format("%.2f", rti.TxBalance));
                    }
                }
            }
        }
    }
/*
    private boolean toggleLayoutExpand(boolean show, View view)
    {
        Tools.toggleArrow(show, view);
        return show;
    }


 */
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
