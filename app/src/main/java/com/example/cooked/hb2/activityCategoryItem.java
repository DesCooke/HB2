package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;

public class activityCategoryItem extends AppCompatActivity
{
    public String actionType;
    public Integer categoryId;
    public TextInputLayout edtCategoryName;
    public Switch swGroupedBudget;
    public Spinner spDefaultBudgetType;
    public CheckBox chkMonitor;

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
