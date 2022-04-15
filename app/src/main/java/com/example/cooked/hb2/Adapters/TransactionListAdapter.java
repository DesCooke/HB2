package com.example.cooked.hb2.Adapters;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.cooked.hb2.GlobalUtils.IntUtils;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.Records.RecordTransactionListItem;
import com.example.cooked.hb2.ViewHolders.ViewHolderTransactionBalance;
import com.example.cooked.hb2.ViewHolders.ViewHolderTransactionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static com.example.cooked.hb2.MainActivity.context;

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context _context;
    private List<RecordTransactionListItem> _items;
    public OnItemClickListener mItemClickListener;
    RecordBudgetMonth _rbm;

    public interface OnItemClickListener
    {
        void onItemClick(View view, RecordTransaction obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }

    private void addTransactionToList(RecordTransaction argrt, List<RecordTransactionListItem> items)
    {
        MyLog.WriteLogMessage("TransactionListAdapter:addTransactionToList:Starting");

        Resources r = Objects.requireNonNull(MyResources.R());

        RecordTransactionListItem rtli = new RecordTransactionListItem();
        rtli.ItemType = r.getInteger(R.integer.item_type_transaction_item);
        rtli.Data = argrt;
        items.add(rtli);

        MyLog.WriteLogMessage("TransactionListAdapter:addTransactionToList:Ending");
    }

    private void addTransactionBalanceToList(RecordTransaction argrt, List<RecordTransactionListItem> items)
    {
        MyLog.WriteLogMessage("TransactionListAdapter:addTransactionBalanceToList:Starting");

        RecordTransactionListItem rtli = new RecordTransactionListItem();
        Resources r = Objects.requireNonNull(MyResources.R());

        rtli.ItemType = r.getInteger(R.integer.item_type_transaction_balance);
        rtli.Data = argrt;
        items.add(rtli);

        MyLog.WriteLogMessage("TransactionListAdapter:addTransactionBalanceToList:Ending");
    }

    private List<RecordTransactionListItem> createListFromTransactionList(List<RecordTransaction> rtl)
    {
        MyLog.WriteLogMessage("TransactionListAdapter:createListFromTransactionList:Starting");
        List<RecordTransactionListItem> items = new ArrayList<>();
        for (int i = 0; i < rtl.size(); i++)
        {
            RecordTransaction rt = rtl.get(i);
            if (rt.MarkerStartingBalance != null || rt.MarkerEndingBalance != null || rt.MarkerTotalOutstanding !=null )
            {
                addTransactionBalanceToList(rt, items);
            }
            else
            {
                addTransactionToList(rt, items);
            }
        }
        MyLog.WriteLogMessage("TransactionListAdapter:createListFromTransactionList:Starting");
        return (items);
    }

    public void UpdateList(List<RecordTransaction> rtl)
    {
        _items = createListFromTransactionList(rtl);
        notifyDataSetChanged();
    }

    public TransactionListAdapter(Context context, List<RecordTransaction> rtl, RecordBudgetMonth rbm)
    {
        _context = context;
        _items = createListFromTransactionList(rtl);
        _rbm = rbm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder vh;

        MyLog.WriteLogMessage("TransactionListAdapter:onCreateViewHolder:Starting");

        Resources r = Objects.requireNonNull(MyResources.R());

        if (viewType == r.getInteger(R.integer.item_type_transaction_item))
        {
            View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_transaction_item, parent, false);
            vh = new ViewHolderTransactionItem(v);
        }
        else
        {
            if (viewType == r.getInteger(R.integer.item_type_transaction_balance))
            {
                View v =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_balance, parent, false);
                vh = new ViewHolderTransactionBalance(v);
            }
            else
            {
                View v =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_balance, parent, false);
                vh = new ViewHolderTransactionBalance(v);
            }
        }

        MyLog.WriteLogMessage("TransactionListAdapter:onCreateViewHolder:Ending");

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        MyLog.WriteLogMessage("TransactionListAdapter:onBindViewHolder:Starting");

        final RecordTransactionListItem rtli = _items.get(position);
        final RecordTransaction rti = (RecordTransaction) rtli.Data;
        if (holder instanceof ViewHolderTransactionItem)
        {
            final ViewHolderTransactionItem view = (ViewHolderTransactionItem) holder;

            if(rti.BudgetYear==0)
            {
                view.mTxSeqNo.setVisibility(View.GONE);
                view.mTxAdded.setVisibility(View.GONE);
                view.mTxFilename.setVisibility(View.GONE);
                view.mTxLineNo.setVisibility(View.GONE);
                view.mTxType.setVisibility(View.GONE);
                view.mBankAccount.setVisibility(View.GONE);
                view.mComments.setVisibility(View.GONE);
                view.mTxBalance.setVisibility(View.GONE);
                view.mBudget.setVisibility(View.GONE);
                view.mTxAmount.setVisibility(View.GONE);


                view.mTxDate.setVisibility(View.VISIBLE);
                view.mTxDate.setText(rti.TxFilename);
                view.mTxDescription.setVisibility(View.VISIBLE);
                view.mTxDescription.setText(rti.Comments);
                view.mSubCategoryName.setVisibility(View.VISIBLE);
                view.mSubCategoryName.setText("Category: " + rti.CategoryName);
            }
            else
            {
                view.mTxSeqNo.setText(IntUtils.IntToStr(rti.TxSeqNo));
                view.mTxAdded.setText(rti.TxAdded.toString());
                view.mTxFilename.setText(rti.TxFilename);
                view.mTxLineNo.setText(IntUtils.IntToStr(rti.TxLineNo));
                view.mTxDate.setText(android.text.format.DateFormat.format("EEE, dd/MM/yyyy", rti.TxDate));
                view.mTxType.setText(rti.TxType);
                view.mBankAccount.setText(rti.TxSortCode + " " + rti.TxAccountNumber);
                if (rti.TxDescription.length() > 0)
                {
                    view.mTxDescription.setVisibility(View.VISIBLE);
                    view.mTxDescription.setText("Description: " + rti.TxDescription);
                }
                else
                {
                    view.mTxDescription.setVisibility(View.GONE);
                }
                view.mSubCategoryName.setText("Category: " + rti.GetFullyQualifiedName());
                if (!rti.UseCategory)
                    view.mSubCategoryName.setVisibility(View.GONE);

                if (rti.Comments.length() > 0)
                {
                    view.mComments.setVisibility(View.VISIBLE);
                    view.mComments.setText("Comments: " + rti.Comments);
                }
                else
                {
                    view.mComments.setVisibility(View.GONE);
                }
                view.mBudget.setText(dateUtils().BudgetAsString(rti.BudgetYear, rti.BudgetMonth));
                view.mTxAmount.setText("Amount " + Tools.moneyFormat(rti.TxAmount));
                if (rti.HideBalance)
                {
                    view.mTxBalance.setVisibility(View.INVISIBLE);
                }
                else
                {
                    view.mTxBalance.setVisibility(View.VISIBLE);
                    view.mTxBalance.setText("Balance " + Tools.moneyFormat(rti.TxBalance));
                }
                view.mFull.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (mItemClickListener != null)
                        {
                            mItemClickListener.onItemClick(view, rti);
                        }
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

                if (!rti.BalanceCorrect)
                {
                    //holder.mTxAmount.setTextColor(MainActivity.context.getResources().getColor(R.color.textError));
                    //holder.mTxBalance.setTextColor(MainActivity.context.getResources().getColor(R.color.textError));
                    if (view.mTxBalance.getVisibility() == View.VISIBLE)
                        view.mTxAmount.setText("Balance " + Tools.moneyFormat(rti.TxBalance));
                }
            }
        }

        if (holder instanceof ViewHolderTransactionBalance)
        {
            MyLog.WriteLogMessage("TransactionListAdapter:onBindViewHolder:Starting");
            final ViewHolderTransactionBalance view = (ViewHolderTransactionBalance) holder;

            if (rti.MarkerStartingBalance != null)
                view.mCellInfo.setText(context.getString(R.string.starting_balance_full,
                    Tools.moneyFormat(rti.MarkerStartingBalance)));

            if (rti.MarkerEndingBalance != null)
                view.mCellInfo.setText(context.getString(R.string.final_balance_full,
                    Tools.moneyFormat(rti.MarkerEndingBalance)));

            if (rti.MarkerTotalOutstanding != null)
            {
                String lLine = "Total " + Tools.moneyFormat(rti.MarkerTotal) + ", " +
                        "Spent " + Tools.moneyFormat(rti.MarkerTotalSpent) + ", " +
                        "O/S " + Tools.moneyFormat(rti.MarkerTotalOutstanding);
                view.mCellInfo.setText(lLine);
            }
        }

        MyLog.WriteLogMessage("TransactionListAdapter:onBindViewHolder:Ending");
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
