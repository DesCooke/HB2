package com.example.cooked.hb2.Adapters;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.cooked.hb2.MainActivity.context;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategoryBudget;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.DialogTransactionList;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyResources;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.Records.RecordBudgetClass;
import com.example.cooked.hb2.Records.RecordBudgetGroup;
import com.example.cooked.hb2.Records.RecordBudgetItem;
import com.example.cooked.hb2.Records.RecordBudgetListItem;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;
import com.example.cooked.hb2.ViewHolders.ViewHolderBudgetClass;
import com.example.cooked.hb2.ViewHolders.ViewHolderBudgetGroup;
import com.example.cooked.hb2.ViewHolders.ViewHolderBudgetItem;
import com.example.cooked.hb2.ViewHolders.ViewHolderSubCategoryByMonth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
                    DialogTransactionList dtl = new DialogTransactionList((Activity) _context);
                    dtl.budgetMonth = rscbm.BudgetMonth;
                    dtl.budgetYear = rscbm.BudgetYear;
                    dtl.subCategoryId = rscbm.SubCategoryId;
                    dtl.categoryId = rscbm.CategoryId;
                    dtl.GetTrans();
                    dtl.show();
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
