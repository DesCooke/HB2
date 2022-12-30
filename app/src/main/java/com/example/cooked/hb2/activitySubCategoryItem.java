package com.example.cooked.hb2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.cooked.hb2.Adapters.SubCategoryByMonthAdapter;
import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Records.RecordSubCategoryByMonth;
import com.google.android.material.textfield.TextInputLayout;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;

import java.util.List;

public class activitySubCategoryItem extends AppCompatActivity
{
    public String actionType;
    public Integer categoryId;
    public Integer subCategoryId;
    public String categoryName;
    public TextInputLayout edtSubCategoryName;
    public CheckBox chkMonitor;
    public CheckBox chkOld;
    public Button btnMove;
    public Button btnTransactions;
    public Activity _activity;
    public Spinner spnSubCategoryType;
    public RecyclerView SubCategoryByMonthList;
    public LinearLayoutManager SubCategoryByMonthLayoutManager;
    public SubCategoryByMonthAdapter subCategoryByMonthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            _activity = this;
            setContentView(R.layout.activity_subcategory_item);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            edtSubCategoryName = findViewById(R.id.edtSubCategoryName);
            Button btnDelete = findViewById(R.id.btnDelete);
            chkMonitor = findViewById(R.id.chkMonitor);
            chkOld = findViewById(R.id.chkOld);
            spnSubCategoryType = findViewById(R.id.spnSubCategoryType);

            btnMove = findViewById(R.id.btnMove);
            btnTransactions = findViewById(R.id.btnTransactions);

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
                chkMonitor.setChecked(rsc.Monitor);
                chkOld.setChecked(rsc.Old);
                spnSubCategoryType.setSelection(rsc.SubCategoryType);

                RefreshPage();
            }

            btnDelete.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        if (actionType.compareTo("EDIT") == 0)
                        {
                            pickCategory(null);
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activitySubCategoryItem::onClick", e.getMessage());
                    }
                }
            });

            btnMove.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        Intent intent = new Intent(getApplicationContext(), activityCategory.class);
                        intent.putExtra("ACTIONTYPE", "PICK");
                        startActivityForResult(intent, 1901);
                    } catch (Exception e)
                    {
                        MyLog.WriteExceptionMessage(e);
                    }
                }
            });

            btnTransactions.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        Intent intent = new Intent(getApplicationContext(), activityTransactionList.class);
                        intent.putExtra("CAPTION", "Transactions for " + categoryName + "/" + edtSubCategoryName.getEditText().getText());
                        intent.putExtra("BUDGETYEAR", 0);
                        intent.putExtra("BUDGETMONTH", 0);
                        intent.putExtra("SUBCATEGORYID", subCategoryId);
                        startActivity(intent);
                    } catch (Exception e)
                    {
                        MyLog.WriteExceptionMessage(e);
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
                        RecordSubCategory rc;
                        rc = new RecordSubCategory();
                        rc.CategoryId = categoryId;
                        rc.SubCategoryName = edtSubCategoryName.getEditText().getText().toString();
                        rc.SubCategoryType = spnSubCategoryType.getSelectedItemPosition();
                        rc.Monitor = chkMonitor.isChecked();
                        rc.Old = chkOld.isChecked();
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
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    private void RefreshPage()
    {
        List<RecordSubCategoryByMonth> list = MyDatabase.MyDB().getSubCategoryTotalByMonth(subCategoryId);
        SubCategoryByMonthList = (RecyclerView) findViewById(R.id.SubCategoryByMonthList);
        SubCategoryByMonthList.setHasFixedSize(true);
        SubCategoryByMonthLayoutManager = new LinearLayoutManager(this);
        SubCategoryByMonthList.setLayoutManager(SubCategoryByMonthLayoutManager);
        subCategoryByMonthAdapter = new SubCategoryByMonthAdapter(_activity, list);
        SubCategoryByMonthList.setAdapter(subCategoryByMonthAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        MyLog.WriteLogMessage("activitySubCategoryItem:onResume:Starting");
        try
        {
            RefreshPage();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("activitySubCategoryItem:onResume:Ending");
    }

    public void pickCategory(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), activityCategory2.class);
            intent.putExtra("ACTIONTYPE", "EDIT");
            startActivityForResult(intent, 1969);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if (requestCode == 1901)
            {
                if (resultCode == RESULT_OK)
                {
                    int lNewCategoryId = data.getIntExtra("NEWCATEGORYID", 0);
                    categoryName = data.getStringExtra("NEWCATEGORYNAME");
                    categoryId = lNewCategoryId;

                    RecordSubCategory rsc = MyDatabase.MyDB().getSubCategory(subCategoryId);
                    rsc.CategoryId = lNewCategoryId;

                    MyDB().updateSubCategory(rsc);
                }
            }
            if (requestCode == 1969)
            {
                if (resultCode == RESULT_OK)
                {
                    String lSubCategoryName = data.getStringExtra("SubCategoryName");
                    String lSubCategoryId = data.getStringExtra("SubCategoryId" );

                    MyDB().changeSubCategory(subCategoryId, Integer.parseInt(lSubCategoryId));

                    RecordSubCategory rc;
                    rc = new RecordSubCategory();
                    rc.SubCategoryName = edtSubCategoryName.getEditText().getText().toString();
                    rc.SubCategoryId = subCategoryId;
                    rc.CategoryId = categoryId;
                    MyDB().deleteSubCategory(rc);

                    finish();
                }
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

}
