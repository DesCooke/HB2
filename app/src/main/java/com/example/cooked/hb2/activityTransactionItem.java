package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.CategoryPicker;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activityTransactionItem extends AppCompatActivity
{
    public String actionType;
    public TextView edtTxDate;
    public EditText edtTxDescription;
    public EditText edtTxAmount;
    public TextView edtCategory;
    public TextView edtComments;
    public TextView edtBudgetYear;
    public TextView edtBudgetMonth;
    public MyInt MySubCategoryId;
    public Button btnOk;
    public Button btnDelete;
    public CategoryPicker cp;
    public Integer txSeqNo;
    public RecordTransaction originalRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_transaction_item);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    
            MySubCategoryId = new MyInt();
            edtTxDate = (TextView) findViewById(R.id.edtTxDate);
            edtTxDescription = (EditText) findViewById(R.id.edtDescription);
            edtTxAmount = (EditText) findViewById(R.id.edtTxAmount);
            edtCategory = (TextView) findViewById(R.id.edtCategory);
            edtComments = (TextView) findViewById(R.id.edtComments);
            edtBudgetYear = (TextView) findViewById(R.id.edtBudgetYear);
            edtBudgetMonth = (TextView) findViewById(R.id.edtBudgetMonth);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnDelete = (Button) findViewById(R.id.btnDelete);
    
            cp = new CategoryPicker(this);
            cp.MySubCategoryId = MySubCategoryId;
            cp.edtSubCategoryName = edtCategory;
    
            setTitle("<Unknown>");
            actionType = getIntent().getStringExtra("ACTIONTYPE");
            if (actionType.compareTo("ADD") == 0)
            {
                originalRecord = new RecordTransaction();
                setTitle("Add a new Cash Transaction");
                btnDelete.setVisibility(GONE);
                setDefaults();
            }
            if (actionType.compareTo("EDIT") == 0)
            {
                setTitle("Amend Transaction");
                txSeqNo = getIntent().getIntExtra("TxSeqNo", 0);
                originalRecord = MyDB().getSingleTransaction(txSeqNo);
                MyString lDateStr = new MyString();
                dateUtils().DateToStr(originalRecord.TxDate, lDateStr);
                edtTxDate.setText(lDateStr.Value);
                edtTxDescription.setText(originalRecord.TxDescription);
                edtTxAmount.setText(Float.toString(originalRecord.TxAmount));
                MySubCategoryId.Value = originalRecord.CategoryId;
                edtCategory.setText(originalRecord.SubCategoryName);
                edtComments.setText(originalRecord.Comments);
                if (originalRecord.BudgetYear == 0)
                    originalRecord.BudgetYear = dateUtils().CurrentBudgetYear();
                if (originalRecord.BudgetMonth == 0)
                    originalRecord.BudgetMonth = dateUtils().CurrentBudgetMonth();
    
                edtBudgetYear.setText(Integer.toString(originalRecord.BudgetYear));
                edtBudgetMonth.setText(Integer.toString(originalRecord.BudgetMonth));
    
                btnDelete.setVisibility(View.VISIBLE);
/*                if(originalRecord.TxSortCode.compareTo("Cash")!=0)
                {
                    edtTxDate.setFocusable(FALSE);
                    edtTxDate.setEnabled(FALSE);
                    edtTxDescription.setFocusable(FALSE);
                    edtTxDescription.setEnabled(FALSE);
                    edtTxAmount.setFocusable(FALSE);
                    edtTxAmount.setEnabled(FALSE);
                    btnDelete.setVisibility(GONE);
                }
  */
            }
    
    
            edtTxDate.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void afterTextChanged(Editable s)
                {
                }
        
                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after)
                {
                }
        
                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count)
                {
                    if (s.length() != 0)
                    {
                        dateUtils().StrToDate(edtTxDate.getText().toString(), originalRecord.TxDate);
                        
                        originalRecord.BudgetYear = dateUtils().GetBudgetYear(originalRecord.TxDate);
                        originalRecord.BudgetMonth = dateUtils().GetBudgetMonth(originalRecord.TxDate);
    
                        edtBudgetYear.setText(Integer.toString(originalRecord.BudgetYear));
                        edtBudgetMonth.setText(Integer.toString(originalRecord.BudgetMonth));
                    }
                }
            });
        
            btnOk.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        RecordTransaction rt;
                        originalRecord.TxDate = new Date();
                        dateUtils().StrToDate(edtTxDate.getText().toString(), originalRecord.TxDate);
                        originalRecord.TxDescription = edtTxDescription.getText().toString();
                        originalRecord.TxAmount = Float.parseFloat(edtTxAmount.getText().toString());
                        originalRecord.CategoryId = MySubCategoryId.Value;
                        originalRecord.SubCategoryName = edtCategory.getText().toString();
                        originalRecord.Comments = edtComments.getText().toString();
                        originalRecord.BudgetYear = Integer.parseInt(edtBudgetYear.getText().toString());
                        originalRecord.BudgetMonth = Integer.parseInt(edtBudgetMonth.getText().toString());
                        if (actionType.compareTo("ADD") == 0)
                        {
                            originalRecord.TxType = "Cash";
                            originalRecord.TxAdded = Calendar.getInstance().getTime();
                            originalRecord.TxStatus = RecordTransaction.Status.NEW;
                            originalRecord.TxBalance = Float.parseFloat("0.00");
                            originalRecord.TxSortCode = "Cash";
                            originalRecord.TxAccountNumber = "Cash";
                            originalRecord.TxLineNo = MyDB().getNextTxLineNo(originalRecord.TxDate);
                            originalRecord.TxFilename = "Cash";
                            originalRecord.TxSeqNo = 0; // will be auto generated
                            MyDB().addTransaction(originalRecord);
                        }
                        if (actionType.compareTo("EDIT") == 0)
                        {
                            MyDB().updateTransaction(originalRecord);
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        MyDB().deleteTransaction(originalRecord);
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });
/*
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
                        if (actionType.compareTo("ADD") == 0)
                        {
                            MyDB().addCategory(rc);
                        }

                        if (actionType.compareTo("EDIT") == 0)
                        {
                            rc.CategoryId = categoryId;
                            MyDB().updateCategory(rc);

                        }


                        //setResult(RESULT_OK, intent);
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });
            */
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in activityTransactionItem::onCreate", e.getMessage());
        }
    }

    public void setDefaults()
    {
        MyString myString=new MyString();
        if(!dateUtils().DateToStr(new Date(), myString))
            return;
        edtTxDate.setText(myString.Value);
        edtTxDescription.setText("");
        edtTxAmount.setText("0.00");
        edtCategory.setText("");
        edtComments.setText("");
        Integer lMonth = dateUtils().CurrentBudgetMonth();
        Integer lYear = dateUtils().CurrentBudgetYear();
        edtBudgetYear.setText(lYear.toString());
        edtCategory.setText(lMonth.toString());
    }

    public void pickDateTime(View view)
    {
        try
        {
            DialogDatePicker ddp=new DialogDatePicker(this);
            ddp.txtStartDate=(TextView) findViewById(R.id.edtTxDate);
            Date date=new Date();
            if(!dateUtils().StrToDate(ddp.txtStartDate.getText().toString(), date))
                return;
            ddp.setInitialDate(date);
            ddp.show();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickDateTime", e.getMessage());
        }
    }

    public void selectAll(View view)
    {
        try
        {
            ((EditText)view).selectAll();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("selectAll", e.getMessage());
        }
    }

    public void pickCategory(View view)
    {
        try
        {
            cp.show();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickCategory", e.getMessage());
        }
    }
}
