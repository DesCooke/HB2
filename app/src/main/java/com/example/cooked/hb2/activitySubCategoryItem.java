package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
