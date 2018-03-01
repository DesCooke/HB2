package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;

public class activitySubCategoryItem extends AppCompatActivity
{
    public String actionType;
    public Integer categoryId;
    public Integer subCategoryId;
    public String categoryName;
    public TextInputLayout edtSubCategoryName;
    public RadioButton radMonthly;
    public RadioButton radExra;
    public RadioButton radIncome;
    public RadioButton radExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_subcategory_item);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            edtSubCategoryName = findViewById(R.id.edtSubCategoryName);
            Button btnDelete = findViewById(R.id.btnDelete);
            final RadioButton radMonthly = findViewById(R.id.radMonthly);
            final RadioButton radExtra = findViewById(R.id.radExtra);
            final RadioButton radIncome = findViewById(R.id.radIncome);
            final RadioButton radExpense = findViewById(R.id.radExpense);

            setTitle("<Unknown>");
            categoryId = getIntent().getIntExtra("CATEGORYID", 0);
            categoryName = getIntent().getStringExtra("CATEGORYNAME");

            actionType = getIntent().getStringExtra("ACTIONTYPE");
            if (actionType.compareTo("ADD") == 0)
            {
                setTitle("Add a new subCategory to " + categoryName);
                btnDelete.setVisibility(GONE);
            }
            if (actionType.compareTo("EDIT") == 0)
            {
                setTitle("Amend SubCategory");
                String lSubCategoryName = getIntent().getStringExtra("SUBCATEGORYNAME");
                subCategoryId = getIntent().getIntExtra("SUBCATEGORYID", -1);
                if (lSubCategoryName.length()>0)
                {
                    edtSubCategoryName.getEditText().setText(lSubCategoryName);
                }
                btnDelete.setVisibility(View.VISIBLE);
                RecordSubCategory rsc= MyDatabase.MyDB().getSubCategory(subCategoryId);
                if(rsc.SubCategoryType==RecordSubCategory.mSCTMonthlyExpense)
                {
                    radMonthly.setChecked(true);
                    radExpense.setChecked(true);
                }
                if(rsc.SubCategoryType==RecordSubCategory.mSCTMonthlyIncome)
                {
                    radMonthly.setChecked(true);
                    radIncome.setChecked(true);
                }
                if(rsc.SubCategoryType==RecordSubCategory.mSCTExtraExpense)
                {
                    radExtra.setChecked(true);
                    radExpense.setChecked(true);
                }
                if(rsc.SubCategoryType==RecordSubCategory.mSCTExtraIncome)
                {
                    radExtra.setChecked(true);
                    radIncome.setChecked(true);
                }
            }

            btnDelete.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        RecordSubCategory rc;
                        rc = new RecordSubCategory();
                        rc.SubCategoryName = edtSubCategoryName.getEditText().getText().toString();
                        if (actionType.compareTo("EDIT") == 0)
                        {
                            rc.SubCategoryId = subCategoryId;
                            rc.CategoryId = categoryId;
                            MyDB().deleteSubCategory(rc);
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activitySubCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });

            radMonthly.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) { radExtra.setChecked(false);}
            });
            radExtra.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) { radMonthly.setChecked(false);}
            });
            radIncome.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) { radExpense.setChecked(false);}
            });
            radExpense.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) { radIncome.setChecked(false);}
            });

            final Button button = findViewById(R.id.btnOk);
            button.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        RecordSubCategory rc;
                        rc = new RecordSubCategory();
                        rc.CategoryId = categoryId;
                        rc.SubCategoryName = edtSubCategoryName.getEditText().getText().toString();
                        if(radMonthly.isChecked())
                        {
                            if(radIncome.isChecked())
                                rc.SubCategoryType = RecordSubCategory.mSCTMonthlyIncome;
                            else
                                rc.SubCategoryType = RecordSubCategory.mSCTMonthlyExpense;

                        }
                        else
                        {
                            if(radIncome.isChecked())
                                rc.SubCategoryType = RecordSubCategory.mSCTExtraIncome;
                            else
                                rc.SubCategoryType = RecordSubCategory.mSCTExtraExpense;
                        }
                        if (actionType.compareTo("ADD") == 0)
                        {
                            MyDB().addSubCategory(rc);
                        }

                        if (actionType.compareTo("EDIT") == 0)
                        {
                            rc.SubCategoryId = subCategoryId;
                            MyDB().updateSubCategory(rc);

                        }


                        //setResult(RESULT_OK, intent);
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activitySubCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in activitSubyCategoryItem::onCreate", e.getMessage());
        }
    }

}
