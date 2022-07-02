package com.example.cooked.hb2;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.cooked.hb2.Adapters.SubCategoryByMonthAdapter;
import com.example.cooked.hb2.CategoryUI.SubCategoryAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;
import com.google.android.material.textfield.TextInputLayout;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;

import java.util.ArrayList;
import java.util.List;

public class activityCategoryItem extends AppCompatActivity
{
    public String actionType;
    public Integer categoryId;
    public TextInputLayout edtCategoryName;
    public Switch swGroupedBudget;
    public Spinner spDefaultBudgetType;
    public CheckBox chkMonitor;
    public ArrayList<RecordSubCategory> mDataset;
    public RecyclerView SubCategoryByMonthList;
    public LinearLayoutManager SubCategoryByMonthLayoutManager;
    public SubCategoryByMonthAdapter subCategoryByMonthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_category_item);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            edtCategoryName = findViewById(R.id.edtCategoryName);
            Button btnDelete = findViewById(R.id.btnDelete);
            swGroupedBudget = findViewById(R.id.swGroupedBudget);
            spDefaultBudgetType = findViewById(R.id.spDefaultBudgetType);
            chkMonitor = findViewById(R.id.chkMonitor);

            swGroupedBudget.setChecked(false);
            spDefaultBudgetType.setVisibility(View.GONE);

            setTitle("<Unknown>");
            actionType = getIntent().getStringExtra("ACTIONTYPE");
            if (actionType.compareTo("ADD") == 0)
            {
                setTitle("Add a new Category");
                btnDelete.setVisibility(GONE);
            }
            if (actionType.compareTo("EDIT") == 0)
            {
                setTitle("Amend Category");
                String lCategoryName = getIntent().getStringExtra("CATEGORYNAME");
                categoryId = getIntent().getIntExtra("CATEGORYID", -1);
                if (lCategoryName.length() > 0)
                {
                    edtCategoryName.getEditText().setText(lCategoryName);
                }
                btnDelete.setVisibility(View.VISIBLE);

                RecordCategory rc = MyDB().getCategory(categoryId);
                swGroupedBudget.setChecked(rc.GroupedBudget);
                if (rc.GroupedBudget)
                {
                    spDefaultBudgetType.setVisibility(View.VISIBLE);
                    spDefaultBudgetType.setSelection(rc.DefaultBudgetType);
                }
                if(rc.Monitor)
                    chkMonitor.setChecked(true);
                List<RecordSubCategoryByMonth> list = MyDatabase.MyDB().getCategoryTotalByMonth(categoryId);
                SubCategoryByMonthList = (RecyclerView) findViewById(R.id.SubCategoryByMonthList);
                SubCategoryByMonthList.setHasFixedSize(true);
                SubCategoryByMonthLayoutManager = new LinearLayoutManager(this);
                SubCategoryByMonthList.setLayoutManager(SubCategoryByMonthLayoutManager);
                subCategoryByMonthAdapter = new SubCategoryByMonthAdapter(this, list);
                SubCategoryByMonthList.setAdapter(subCategoryByMonthAdapter);

            }

            btnDelete.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        RecordCategory rc;
                        rc = new RecordCategory();
                        rc.CategoryName = edtCategoryName.getEditText().getText().toString();
                        if (actionType.compareTo("EDIT") == 0)
                        {
                            rc.CategoryId = categoryId;
                            MyDB().deleteCategory(rc);
                        }
                    } catch (Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });

            swGroupedBudget.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        if (swGroupedBudget.isChecked())
                        {
                            spDefaultBudgetType.setVisibility(View.VISIBLE);
                        } else
                        {
                            spDefaultBudgetType.setVisibility(View.GONE);
                        }
                    } catch (Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                }
            });

            final Button button = findViewById(R.id.btnOk);
            button.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        RecordCategory rc;
                        rc = new RecordCategory();
                        rc.CategoryName = edtCategoryName.getEditText().getText().toString();
                        rc.Monitor = chkMonitor.isChecked();
                        if (actionType.compareTo("ADD") == 0)
                        {
                            MyDB().addCategory(rc);
                        }

                        if (actionType.compareTo("EDIT") == 0)
                        {
                            rc.CategoryId = categoryId;
                            rc.GroupedBudget = swGroupedBudget.isChecked();
                            rc.DefaultBudgetType = 0;
                            rc.Monitor = chkMonitor.isChecked();
                            if (rc.GroupedBudget)
                                rc.DefaultBudgetType = spDefaultBudgetType.getSelectedItemPosition();

                            MyDB().updateCategory(rc);
                        }


                        //setResult(RESULT_OK, intent);
                    } catch (Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

}
