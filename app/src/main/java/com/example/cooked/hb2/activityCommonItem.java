package com.example.cooked.hb2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cooked.hb2.Database.RecordCommon;
import com.example.cooked.hb2.GlobalUtils.CategoryPicker;
import com.example.cooked.hb2.GlobalUtils.DialogDatePicker;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.MyString;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.View.GONE;
import static com.example.cooked.hb2.Database.MyDatabase.MyDB;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;

public class activityCommonItem extends AppCompatActivity
{
    public String actionType;
    public TextView edtTxDate;
    public EditText edtTxDescription;
    public EditText edtTxAmount;
    public TextView tvCategory;
    public TextView edtComments;
    public MyInt MySubCategoryId;
    public Button btnOk;
    public Button btnDelete;
    public CategoryPicker cp;
    public Integer txSeqNo;
    public RecordCommon originalRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_common_item);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            MySubCategoryId = new MyInt();
            edtTxDate = findViewById(R.id.edtTxDate);
            edtTxDescription = findViewById(R.id.edtDescription);
            edtTxAmount = findViewById(R.id.edtTxAmount);
            tvCategory = findViewById(R.id.edtCategory);
            edtComments = findViewById(R.id.edtComments);
            btnOk = findViewById(R.id.btnOk);
            btnDelete = findViewById(R.id.btnDelete);

            cp = new CategoryPicker(this);
            cp.MySubCategoryId = MySubCategoryId;
            cp.tvSubCategoryName = tvCategory;

            setTitle("<Unknown>");
            actionType = getIntent().getStringExtra("ACTIONTYPE");
            if (actionType.compareTo("ADD") == 0)
            {
                originalRecord = new RecordCommon();
                setTitle("Add a new Common Transaction");
                btnDelete.setVisibility(GONE);
                setDefaults();
            }
            if (actionType.compareTo("EDIT") == 0)
            {
                setTitle("Amend Common Transaction");
                txSeqNo = getIntent().getIntExtra("TxSeqNo", 0);
                originalRecord = MyDB().getSingleCommonTransaction(txSeqNo);
                MyString lDateStr = new MyString();
                dateUtils().DateToStr(originalRecord.TxDate, lDateStr);
                edtTxDate.setText(lDateStr.Value);
                edtTxDescription.setText(originalRecord.TxDescription);
                edtTxAmount.setText(String.format(Locale.UK, "%.2f", originalRecord.TxAmount));
                MySubCategoryId.Value = originalRecord.CategoryId;
                tvCategory.setText(originalRecord.SubCategoryName);
                edtComments.setText(originalRecord.Comments);

                btnDelete.setVisibility(View.VISIBLE);
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
                    }
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try
                    {
                        originalRecord.TxDate = new Date();
                        originalRecord.TxDate = dateUtils().StrToDate(edtTxDate.getText().toString());
                        originalRecord.TxDescription = edtTxDescription.getText().toString();
                        originalRecord.TxAmount = Float.parseFloat(edtTxAmount.getText().toString());
                        originalRecord.CategoryId = MySubCategoryId.Value;
                        originalRecord.SubCategoryName = tvCategory.getText().toString();
                        originalRecord.Comments = edtComments.getText().toString();
                        if (actionType.compareTo("ADD") == 0)
                        {
                            originalRecord.TxSeqNo = 0; // will be auto generated
                            MyDB().addCommonTransaction(originalRecord);
                        }
                        if (actionType.compareTo("EDIT") == 0)
                        {
                            MyDB().updateCommonTransaction(originalRecord);
                        }
                    } catch (Exception e)
                    {
                        ErrorDialog.Show("Error in activityCommonItem::onClick", e.getMessage());
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
                        MyDB().deleteCommonTransaction(originalRecord);
                    } catch (Exception e)
                    {
                        ErrorDialog.Show("Error in activityCommonItem::onClick", e.getMessage());
                    }
                    finish();
                }
            });
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void setDefaults()
    {
        try
        {
            MyString myString = new MyString();
            if (!dateUtils().DateToStr(new Date(), myString))
                return;
            edtTxDate.setText(myString.Value);

            edtTxDescription.setText("");
            edtTxAmount.setText(R.string.AmountZero);
            tvCategory.setText("");
            edtComments.setText("");
            Integer lMonth = dateUtils().CurrentBudgetMonth();
            Integer lYear = dateUtils().CurrentBudgetYear();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void pickDateTime(View view)
    {
        try
        {
            DialogDatePicker ddp = new DialogDatePicker(this);
            ddp.txtStartDate = findViewById(R.id.edtTxDate);
            Date date = dateUtils().StrToDate(ddp.txtStartDate.getText().toString());
            ddp.setInitialDate(date);
            ddp.show();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    public void selectAll(View view)
    {
        try
        {
            ((EditText) view).selectAll();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
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
            if (requestCode == 1969)
            {
                if (resultCode == RESULT_OK)
                {
                    String lSubCategoryName = data.getStringExtra("SubCategoryName");
                    String lSubCategoryId = data.getStringExtra("SubCategoryId");
                    MySubCategoryId.Value = Integer.parseInt(lSubCategoryId);
                    tvCategory.setText(lSubCategoryName);
                }
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }
}
