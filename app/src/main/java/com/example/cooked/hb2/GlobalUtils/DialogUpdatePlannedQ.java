package com.example.cooked.hb2.GlobalUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.util.Locale;


public class DialogUpdatePlannedQ extends Dialog implements View.OnClickListener
{
    public Button btnOk;
    public Button btnCancel;
    public TextView txtPlannedAmount;
    public TextView txtThisTransactionAmount;
    public TextView txtPlanned;

    public Float plannedAmount;
    public Float thisTransactionAmount;
    public int plannedId;
    public String planned;
    public Activity myActivity;

    public DialogUpdatePlannedQ(Activity a)
    {
        super(a);
        myActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_update_planned);

        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        txtPlannedAmount = findViewById(R.id.txtPlannedAmount);
        txtThisTransactionAmount = findViewById(R.id.txtThisTransactionAmount);
        txtPlanned = findViewById(R.id.txtPlanned);

        txtPlannedAmount.setText(String.format(Locale.UK, "%.2f", plannedAmount));
        txtThisTransactionAmount.setText(String.format(Locale.UK, "%.2f", thisTransactionAmount));
        txtPlanned.setText(planned);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            switch (v.getId())
            {
                case R.id.btnOk:
                    RecordPlanned lRecordPlanned = MyDatabase.MyDB().getSinglePlanned(plannedId);
                    lRecordPlanned.mMatchingTxAmount = thisTransactionAmount;
                    MyDatabase.MyDB().updatePlanned(lRecordPlanned);
                    Toast.makeText(myActivity.getBaseContext(), "Planned Item Updated", Toast.LENGTH_SHORT).show();
                    myActivity.finish();
                    dismiss();
                    break;
                case R.id.btnCancel:
                    myActivity.finish();
                    dismiss();
                    break;
            }
        } catch (Exception e)
        {
//            ShowError("onClick", e.getMessage());
        }
    }

}
