package com.example.cooked.hb2.Adapters;

import static com.example.cooked.hb2.MainActivity.context;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;
import com.example.cooked.hb2.ViewHolders.ViewHolderSubCategoryByMonth;
import com.example.cooked.hb2.activityTransactionList;

import java.util.List;
import java.util.Objects;

public class SubCategoryByMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context _context;
    private List<RecordSubCategoryByMonth> _items;

    public interface OnItemClickListener {
        void onItemClick(View view, RecordSubCategoryByMonth obj, int position);
    }

    public void setOnItemClickListener(final SubCategoryByMonthAdapter.OnItemClickListener mItemClickListener) {
    }

    public SubCategoryByMonthAdapter(Context context, List<RecordSubCategoryByMonth> items) {
        _context = context;
        _items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder vh;
        Resources r = Objects.requireNonNull(MyResources.R());

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_subcategory3, parent, false);
        vh = new ViewHolderSubCategoryByMonth(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final RecordSubCategoryByMonth rscbm = _items.get(position);
        if (holder instanceof ViewHolderSubCategoryByMonth) {
            final ViewHolderSubCategoryByMonth view = (ViewHolderSubCategoryByMonth) holder;

            view.txtYearPeriod.setText("Period: " + rscbm.BudgetYear + "/" + rscbm.BudgetMonth + ", " +
                DateUtils.BudgetStartAsStr(rscbm.BudgetMonth, rscbm.BudgetYear) + " -> " +
                DateUtils.BudgetEndAsStr(rscbm.BudgetMonth, rscbm.BudgetYear));
            view.txtCount.setText("Number of transactions: " + rscbm.TransactionCount);
            view.txtAmount.setText(context.getString(R.string.total_line, Tools.moneyFormat(rscbm.Total)));
            view.titleParent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(_context, activityTransactionList.class);
                    intent.putExtra("CAPTION", "Transactions");
                    intent.putExtra("LINE1", "For Category " + rscbm.CategoryName);
                    intent.putExtra("LINE2", "For SubCategory " + rscbm.SubCategoryName);
                    intent.putExtra("LINE3", "For Budget Period " + rscbm.BudgetMonth + "/" + rscbm.BudgetYear);
                    intent.putExtra("BUDGETYEAR", rscbm.BudgetYear);
                    intent.putExtra("BUDGETMONTH", rscbm.BudgetMonth);
                    intent.putExtra("CATEGORYID", 0);
                    intent.putExtra("SUBCATEGORYID", rscbm.SubCategoryId);
                    _context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return _items.size();
    }

}
