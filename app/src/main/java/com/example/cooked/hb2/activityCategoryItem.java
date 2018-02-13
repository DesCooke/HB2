package com.example.cooked.hb2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cooked.hb2.Database.RecordCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;

import static com.example.cooked.hb2.Database.MyDatabase.MyDB;

public class activityCategoryItem extends AppCompatActivity
{
    public String actionType;
    public TextInputLayout edtCategoryName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_category_item);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    
            setTitle("<Unknown>");
            actionType = getIntent().getStringExtra("ACTIONTYPE");
            if (actionType.compareTo("ADD") == 0)
            {
                setTitle("Add a new Category");
            }
            
            edtCategoryName = findViewById(R.id.edtCategoryName);

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

                        MyDB().addCategory(rc);

                        //setResult(RESULT_OK, intent);
                    }
                    catch(Exception e)
                    {
                        ErrorDialog.Show("Error in activityCategoryItem::onClick", e.getMessage());
                    }
                    finish();
                }
         });
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in activityCategoryItem::onCreate", e.getMessage());
        }
    }
    
}
