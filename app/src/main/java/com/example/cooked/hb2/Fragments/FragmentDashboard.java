package com.example.cooked.hb2.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordBudgetProgress;
import com.example.cooked.hb2.Database.RecordPlanned;
import com.example.cooked.hb2.GlobalUtils.DateUtils;
import com.example.cooked.hb2.GlobalUtils.MyInt;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.GlobalUtils.Tools;
import com.example.cooked.hb2.Records.RecordBudgetClass;
import com.example.cooked.hb2.Records.RecordBudgetGroup;
import com.example.cooked.hb2.Records.RecordBudgetItem;
import com.example.cooked.hb2.Records.RecordBudgetMonth;
import com.example.cooked.hb2.R;
import com.example.cooked.hb2.TransactionUI.BudgetProgressAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Math.abs;

public class FragmentDashboard extends Fragment
{
    private View _root;
    private TextView _startingBalance;
    private TextView _monthlyIncome;
    private TextView _monthlyExpense;
    private TextView _amountLeft;
    private TextView _lblAmountLeft;
    private TextView _extraIncome;
    private TextView _extraExpense;
    private TextView _extraLeft;
    private TextView _lblExtraLeft;
    private TextView _finalBudgetBalanceThisMonth;
    private RecordBudgetMonth _rbm;

    public ArrayList<RecordBudgetProgress> mDataset;
    public RecyclerView mBudgetProgressList;
    public RecyclerView.LayoutManager mLayoutManagerCurrent;
    public BudgetProgressAdapter mBudgetProgressAdapter;

    public FragmentDashboard()
    {
    }

    public static FragmentDashboard newInstance()
    {
        FragmentDashboard fragment = new FragmentDashboard();
        return fragment;
    }

    private void initComponent(View root)
    {
        try
        {
            _startingBalance = root.findViewById(R.id.dbbs_StartingBalance);
            _monthlyIncome = root.findViewById(R.id.dbbs_MonthlyIncome);
            _monthlyExpense = root.findViewById(R.id.dbbs_MonthlyExpense);
            _amountLeft = root.findViewById(R.id.dbbs_AmountLeft);
            _lblAmountLeft = root.findViewById(R.id.dbbs_LblAmountLeft);
            _extraIncome = root.findViewById(R.id.dbbs_ExtraIncome);
            _extraExpense = root.findViewById(R.id.dbbs_ExtraExpense);
            _extraLeft = root.findViewById(R.id.dbbs_ExtraLeft);
            _lblExtraLeft = root.findViewById(R.id.dbbs_lblExtraLeft);
            _finalBudgetBalanceThisMonth = root.findViewById(R.id.dbbs_FinalBudgetBalanceThisMonth);
            _root=root;

        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void CreateRecyclerView()
    {
        try
        {
            if(_rbm==null)
                return;

            mDataset = new ArrayList<RecordBudgetProgress>();

            String lNotes = _rbm.notes;
            String lines[] = lNotes.split("\\r?\\n");
            for (int i = 0; i < lines.length; ++i)
            {
                RecordBudgetProgress rbp = new RecordBudgetProgress();
                rbp.mJustANote = true;
                rbp.mTitle=lines[i];
                rbp.mNoteColor= Color.RED;
                mDataset.add(rbp);
            }

            for(int i=0;i<_rbm.budgetClasses.size();i++)
            {
                RecordBudgetClass rbc=_rbm.budgetClasses.get(i);
                for(int j=0;j<rbc.budgetGroups.size();j++)
                {
                    RecordBudgetGroup rbg = rbc.budgetGroups.get(j);
                    if(rbg.Monitor)
                    {
                        RecordBudgetProgress rbp = new RecordBudgetProgress();
                        rbp.mTitle=rbg.budgetGroupName;
                        rbp.mTotalAmount=rbg.origTotal;
                        rbp.mLeftAmount=rbg.outstanding;
                        rbp.mSpentAmount=rbg.spent;

                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                        Date date = calendar.getTime();
                        int currentDay = calendar.get(Calendar.DATE);
                        int lPerc=0;

                        int lCurrentBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
                        int lCurrentBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
                        boolean lFutureBudget=false;
                        boolean lPastBudget=false;
                        if(lCurrentBudgetYear>_rbm.budgetYear ||
                                (lCurrentBudgetYear == _rbm.budgetYear && lCurrentBudgetMonth > _rbm.budgetMonth) )
                        {
                            lPastBudget = true;
                        }
                        if(lCurrentBudgetYear<_rbm.budgetYear ||
                                (lCurrentBudgetYear == _rbm.budgetYear && lCurrentBudgetMonth < _rbm.budgetMonth) )
                        {
                            lFutureBudget = true;
                        }

                        if(currentDay == 26 || lFutureBudget)
                        {
                            lPerc=0;
                        }
                        else
                        {
                            if(currentDay==25 || lPastBudget)
                            {
                                lPerc=100;
                            }
                            else
                            {
                                if(currentDay>26)
                                {
                                    int lDays=currentDay-26;
                                    lPerc=(int)((lDays*100)/30);
                                }
                                else
                                {
                                    int lDays=currentDay + 5;
                                    lPerc=(int)((lDays*100)/30);
                                }
                            }
                        }

                        rbp.mPercInMonth=lPerc;
                        float lFloat=rbg.spent / rbg.origTotal * 100.00f;
                        rbp.mSpentPerc=(int)lFloat;
                        mDataset.add(rbp);
                    }

                    for(int k=0;k<rbg.budgetItems.size();k++)
                    {
                        RecordBudgetItem rbi=rbg.budgetItems.get(k);
                        if(rbi.Monitor)
                        {
                            RecordBudgetProgress rbp = new RecordBudgetProgress();
                            rbp.mTitle=rbg.budgetGroupName + "/" + rbi.budgetItemName;
                            rbp.mTotalAmount=rbi.origTotal;
                            rbp.mLeftAmount=rbi.outstanding;
                            rbp.mSpentAmount=rbi.spent;

                            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                            Date date = calendar.getTime();
                            int currentDay = calendar.get(Calendar.DATE);
                            int lPerc=0;

                            int lCurrentBudgetMonth = DateUtils.dateUtils().CurrentBudgetMonth();
                            int lCurrentBudgetYear = DateUtils.dateUtils().CurrentBudgetYear();
                            boolean lFutureBudget=false;
                            boolean lPastBudget=false;
                            if(lCurrentBudgetYear>_rbm.budgetYear ||
                                    (lCurrentBudgetYear == _rbm.budgetYear && lCurrentBudgetMonth > _rbm.budgetMonth) )
                            {
                                lPastBudget = true;
                            }
                            if(lCurrentBudgetYear<_rbm.budgetYear ||
                                    (lCurrentBudgetYear == _rbm.budgetYear && lCurrentBudgetMonth < _rbm.budgetMonth) )
                            {
                                lFutureBudget = true;
                            }

                            if(currentDay == 26 || lFutureBudget)
                            {
                                lPerc=0;
                            }
                            else
                            {
                                if(currentDay==25 || lPastBudget)
                                {
                                    lPerc=100;
                                }
                                else
                                {
                                    if(currentDay>26)
                                    {
                                        int lDays=currentDay-26;
                                        lPerc=(int)((lDays*100)/30);
                                    }
                                    else
                                    {
                                        int lDays=currentDay + 5;
                                        lPerc=(int)((lDays*100)/30);
                                    }
                                }
                            }

                            rbp.mPercInMonth=lPerc;
                            float lFloat=rbi.spent / rbi.origTotal * 100.00f;
                            rbp.mSpentPerc=(int)lFloat;
                            mDataset.add(rbp);
                        }
                    }
                }
            }

            ArrayList<RecordPlanned> pl = MyDatabase.MyDB().getPlannedList(true);
            for (int i=0; i<pl.size(); i++)
            {
              RecordPlanned rp = pl.get(i);
              if(rp.mHighlight30DaysBeforeAnniversary)
              {
                  MyInt lDay = new MyInt();
                  MyInt lMonth = new MyInt();
                  MyInt lYear = new MyInt();

                  DateUtils.dateUtils().GetDay(rp.mStartDate, lDay);
                  DateUtils.dateUtils().GetMonth(rp.mStartDate, lMonth);
                  DateUtils.dateUtils().GetYear(DateUtils.dateUtils().GetNow(), lYear);

                  Calendar calendar = DateUtils.dateUtils().GetCalendar();
                  calendar.set(lYear.Value, lMonth.Value-1, lDay.Value);
                  calendar.set(Calendar.HOUR_OF_DAY, 0);
                  calendar.set(Calendar.MINUTE, 0);
                  calendar.set(Calendar.SECOND, 0);
                  calendar.set(Calendar.MILLISECOND, 0);
                  if(calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                  {
                      calendar.add(Calendar.YEAR, 1);
                  }

                  // anniv
                  long lAnnivLong = DateUtils.dateUtils().SecsFromCalendar(calendar);
                  long lNowLong = DateUtils.dateUtils().SecsFromCalendar(Calendar.getInstance());

                  long lDiffInSecs=lAnnivLong-lNowLong;
                  long lDiffInDays = lDiffInSecs / 86400;
                  if(lDiffInDays < 31)
                  {
                      RecordBudgetProgress rbp = new RecordBudgetProgress();
                      rbp.mTitle="Planned item due soon: " + rp.mPlannedName + " due in " +
                        lDiffInDays + " day(s)";
                      rbp.mTotalAmount=0.00f;
                      rbp.mLeftAmount=0.00f;
                      rbp.mSpentAmount=0.00f;
                      rbp.mPercInMonth=0;
                      rbp.mSpentPerc=0;
                      mDataset.add(rbp);
                  }
              }
            }

            mBudgetProgressList = _root.findViewById(R.id.budgetProgressList);
            mBudgetProgressList.setHasFixedSize(true);
            mLayoutManagerCurrent = new LinearLayoutManager(_root.getContext());
            mBudgetProgressList.setLayoutManager(mLayoutManagerCurrent);
            mBudgetProgressAdapter = new BudgetProgressAdapter(mDataset);
            mBudgetProgressList.setAdapter(mBudgetProgressAdapter);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }


    public void refreshUI()
    {
        try
        {
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initComponent(root);

        populate();
        return root;
    }

    public void RefreshForm(RecordBudgetMonth rbm)
    {
        try
        {
            PopulateForm(rbm);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    public void PopulateForm(RecordBudgetMonth rbm)
    {
        try
        {
            _rbm = rbm;
            if (_monthlyIncome == null)
                return;
            populate();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void populate()
    {
        try
        {
            CreateRecyclerView();

            _monthlyIncome.setText(Tools.moneyFormat(_rbm.monthlyIncome));
            _monthlyExpense.setText(Tools.moneyFormat(_rbm.monthlyExpense));
            _amountLeft.setText(Tools.moneyFormat(abs(_rbm.amountLeft)));
            if ((_rbm.amountLeft) > 0.00f)
            {
                _lblAmountLeft.setText("Monthly Underspend by...");
            } else
            {
                if ((_rbm.amountLeft) < 0.00f)
                {
                    _lblAmountLeft.setText("Monthly Overspend by...");
                } else
                {
                    {
                        _lblAmountLeft.setText("Monthly income matches expense");
                    }
                }
            }

            _extraIncome.setText(Tools.moneyFormat(_rbm.extraIncome));
            _extraExpense.setText(Tools.moneyFormat(_rbm.extraExpense));
            _extraLeft.setText(Tools.moneyFormat(abs(_rbm.extraLeft)));
            if ((_rbm.extraLeft) > 0.00f)
            {
                _lblExtraLeft.setText("Extra Underspend by...");
            } else
            {
                if ((_rbm.extraLeft) < 0.00f)
                {
                    _lblExtraLeft.setText("Extra Overspend by...");
                } else
                {
                    {
                        _lblExtraLeft.setText("Extra income matches expense");
                    }
                }
            }

            _startingBalance.setText(Tools.moneyFormat(_rbm.startingBalance));

            _finalBudgetBalanceThisMonth.setText(Tools.moneyFormat(_rbm.finalBudgetBalanceThisMonth));

        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }
}