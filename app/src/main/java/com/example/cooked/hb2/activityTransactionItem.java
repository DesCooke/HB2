package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.GlobalUtils.CategoryPicker;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.GlobalUtils.DialogUpdatePlannedQ;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class activityTransactionItem extends AppCompatActivity
{
    public String actionType;
    public TextView edtTxDate;
    public EditText edtTxDescription;
    public EditText edtTxAmount;
    public TextView tvCategory;
    public TextView edtComments;
    public TextView edtBudgetYear;
    public TextView edtBudgetMonth;
    public MyInt MySubCategoryId;
    public Button btnOk;
    public Button btnDelete;
    public Button btnCreatePlanned;
    public CategoryPicker cp;
    public Integer txSeqNo;
    public RecordTransaction originalRecord;
    public int templateSeqNo;
    DialogUpdatePlannedQ dialogUpdatePlannedQ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            templateSeqNo=-1;
            setContentView(R.layout.activity_transaction_item);
            Toolbar toolbar =  findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    
            MySubCategoryId = new MyInt();
            edtTxDate = findViewById(R.id.edtTxDate);
            edtTxDescription =  findViewById(R.id.edtDescription);
            edtTxAmount = findViewById(R.id.edtTxAmount);
            tvCategory =  findViewById(R.id.edtCategory);
            edtComments =  findViewById(R.id.edtComments);
            edtBudgetYear =  findViewById(R.id.edtBudgetYear);
            edtBudgetMonth =  findViewById(R.id.edtBudgetMonth);
            btnOk = findViewById(R.id.btnOk);
            btnDelete = findViewById(R.id.btnDelete);
            btnCreatePlanned = findViewById(R.id.btnPlanned);
            
            dialogUpdatePlannedQ = new DialogUpdatePlannedQ(this);

            cp = new CategoryPicker(this);
            cp.MySubCategoryId = MySubCategoryId;
            cp.tvSubCategoryName = tvCategory;
    
            setTitle("<Unknown>");
            actionType = getIntent().getStringExtra("ACTIONTYPE");
            if (actionType.compareTo("ADD") == 0)
            {
                originalRecord = new RecordTransaction();
                setTitle("Add a new Cash Transaction");
                btnDelete.setVisibility(GONE);
                btnCreatePlanned.setVisibility(GONE);
                setDefaults();
                String lTemplateDesc = getIntent().getStringExtra("TEMPLATEDESC");
                if(lTemplateDesc!=null)
                {
                    MyLog.WriteLogMessage("Received TEMPLATEDESC of " + lTemplateDesc);

                    setDefaultsForTemplate(lTemplateDesc);
                }
                else
                {
                    MyLog.WriteLogMessage("Did not receive a TEMPLATEDESC");
                }
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
                edtTxAmount.setText(String.format(Locale.UK, "%.2f", originalRecord.TxAmount));
                MySubCategoryId.Value = originalRecord.CategoryId;
                tvCategory.setText(originalRecord.SubCategoryName);
                edtComments.setText(originalRecord.Comments);
                if (originalRecord.BudgetYear == 0)
                    originalRecord.BudgetYear = dateUtils().CurrentBudgetYear();
                if (originalRecord.BudgetMonth == 0)
                    originalRecord.BudgetMonth = dateUtils().CurrentBudgetMonth();
    
                edtBudgetYear.setText(String.format(Locale.UK, "%d", originalRecord.BudgetYear));
                edtBudgetMonth.setText(String.format(Locale.UK, "%d", originalRecord.BudgetMonth));
    
                btnDelete.setVisibility(View.VISIBLE);
                btnCreatePlanned.setVisibility(View.VISIBLE);
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
                        originalRecord.TxDate = dateUtils().StrToDate(edtTxDate.getText().toString());
                        
                        originalRecord.BudgetYear = dateUtils().GetBudgetYear(originalRecord.TxDate);
                        originalRecord.BudgetMonth = dateUtils().GetBudgetMonth(originalRecord.TxDate);
    
                        edtBudgetYear.setText(String.format(Locale.UK, "%d", originalRecord.BudgetYear));
                        edtBudgetMonth.setText(String.format(Locale.UK, "%d", originalRecord.BudgetMonth));
                    }
                }
            });
        
            btnOk.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    boolean lOkToFinish=true;
                    try
                    {
                        originalRecord.TxDate = new Date();
                        originalRecord.TxDate = dateUtils().StrToDate(edtTxDate.getText().toString());
                        originalRecord.TxDescription = edtTxDescription.getText().toString();
                        originalRecord.TxAmount = Float.parseFloat(edtTxAmount.getText().toString());
                        originalRecord.CategoryId = MySubCategoryId.Value;
                        originalRecord.SubCategoryName = tvCategory.getText().toString();
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
                            if(templateSeqNo!=-1)
                            {
                               RecordCommon lrc = MyDB().getSingleCommonTransaction(templateSeqNo);
                               lrc.TxDate = originalRecord.TxDate;
                               MyDB().updateCommonTransaction(lrc);
                            }
                            
                        }
                        if (actionType.compareTo("EDIT") == 0)
                        {
                            MyDB().updateTransaction(originalRecord);
                        }
                        MyLog.WriteLogMessage("VV: MySubCategoryId.Value is " + MySubCategoryId.Value);
                        if(MySubCategoryId.Value != 0)
                        {
                            Date lNow = Calendar.getInstance().getTime();
                            ArrayList<RecordPlanned> lrpa = MyDatabase.MyDB().getPlannedListForSubCategory(MySubCategoryId.Value);
                            MyLog.WriteLogMessage("VV: There are " + lrpa + " planned items");
                            for(int i=0;i<lrpa.size();i++)
                            {
                                RecordPlanned lrp=lrpa.get(i);
                                MyLog.WriteLogMessage("VV: Checking " + lrp.mPlanned);
                                
                                MyLog.WriteLogMessage("VV:    Start " + lrp.mStartDate.toString());
                                MyLog.WriteLogMessage("VV:    End " + lrp.mEndDate.toString());
                                if( lrp.mStartDate.before(lNow) && lrp.mEndDate.after(lNow) )
                                {
                                    MyLog.WriteLogMessage("VV:    Type " + lrp.mPlannedType);
                                    if (lrp.mPlannedType == RecordPlanned.mPTMonthly)
                                    {
                                        MyLog.WriteLogMessage("VV:    Amount " + lrp.mMatchingTxAmount);
                                        if( (lrp.mMatchingTxAmount > 0 && originalRecord.TxAmount > 0) ||
                                          (lrp.mMatchingTxAmount < 0 && originalRecord.TxAmount < 0) )
                                        {
                                            if( lrp.mMatchingTxAmount.compareTo(originalRecord.TxAmount)!=0 )
                                            {
                                                lOkToFinish=false;
                                                dialogUpdatePlannedQ.plannedAmount = lrp.mMatchingTxAmount;
                                                dialogUpdatePlannedQ.thisTransactionAmount = originalRecord.TxAmount;
                                                dialogUpdatePlannedQ.plannedId = lrp.mPlannedId;
                                                dialogUpdatePlannedQ.planned = lrp.mPlannedName;
                                                dialogUpdatePlannedQ.show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    if(lOkToFinish)
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
            btnCreatePlanned.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        createPlanned(v);
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
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
        edtTxAmount.setText(R.string.AmountZero);
        tvCategory.setText("");
        edtComments.setText("");
        Integer lMonth = dateUtils().CurrentBudgetMonth();
        Integer lYear = dateUtils().CurrentBudgetYear();
        edtBudgetYear.setText(String.format(Locale.UK, "%d", lYear));
        edtBudgetMonth.setText(String.format(Locale.UK, "%d", lMonth));
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
    }
    
    public void setDefaultsForTemplate(String argTemplate)
    {
        RecordCommon lRecordCommon = MyDatabase.MyDB().getSingleCommonTransaction(argTemplate);
        if (lRecordCommon == null)
            return;
       
        templateSeqNo = lRecordCommon.TxSeqNo;
        edtTxDescription.setText(lRecordCommon.TxDescription);
        edtTxAmount.setText(lRecordCommon.TxAmount.toString());
        tvCategory.setText(lRecordCommon.SubCategoryName);
        edtComments.setText(lRecordCommon.Comments);

        MyString lDateStr = new MyString();
        dateUtils().DateToStr(lRecordCommon.TxDate, lDateStr);
        edtTxDate.setText(lDateStr.Value);
        originalRecord.BudgetYear = dateUtils().GetBudgetYear(lRecordCommon.TxDate);
        originalRecord.BudgetMonth = dateUtils().GetBudgetMonth(lRecordCommon.TxDate);
    
        edtBudgetYear.setText(String.format(Locale.UK, "%d", originalRecord.BudgetYear));
        edtBudgetMonth.setText(String.format(Locale.UK, "%d", originalRecord.BudgetMonth));

        MySubCategoryId.Value = lRecordCommon.CategoryId;
    }

    public void pickDateTime(View view)
    {
        try
        {
            DialogDatePicker ddp=new DialogDatePicker(this);
            ddp.txtStartDate= findViewById(R.id.edtTxDate);
            Date date=dateUtils().StrToDate(ddp.txtStartDate.getText().toString() );
            ddp.setInitialDate(date);
            ddp.show();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickDateTime", e.getMessage());
        }
    }

    public void createPlanned(View view)
    {
        try
        {
            int lPlannedId = MyDB().createPlanned(originalRecord.TxSeqNo);
            Intent intent = new Intent(getApplicationContext(), activityPlanningItem.class);
            intent.putExtra("ACTIONTYPE", "EDIT");
            intent.putExtra("PlannedId", lPlannedId);
            startActivity(intent);
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
//            cp.show();
            Intent intent = new Intent(getApplicationContext(), activityCategory2.class);
            intent.putExtra("ACTIONTYPE", "EDIT");
//            intent.putExtra("PlannedId", lPlannedId);
            startActivityForResult(intent, 1969);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("pickCategory", e.getMessage());
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1969) {
         if(resultCode == RESULT_OK) {
             String lSubCategoryName= data.getStringExtra("SubCategoryName");
             String lSubCategoryId= data.getStringExtra("SubCategoryId");
             MySubCategoryId.Value = Integer.parseInt(lSubCategoryId);
             tvCategory.setText(lSubCategoryName);
         }
    }
}
}
