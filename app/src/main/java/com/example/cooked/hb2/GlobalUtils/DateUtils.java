package com.example.cooked.hb2.GlobalUtils;

import android.annotation.SuppressLint;
import android.widget.DatePicker;

import com.example.cooked.hb2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.cooked.hb2.MainActivity.context;

public class DateUtils
{
    private static DateUtils myDateUtils=null;

    public static DateUtils dateUtils()
    {
        if(myDateUtils == null)
            myDateUtils=new DateUtils();

        return (myDateUtils);
    }


    //
    // getDateFromDatePicker
    //   Description: accepts a DatePicker and extracts the date element
    //                and passes back through retDate
    //   Returns: true(worked)/false(failed)
    //
    private boolean getDateFromDatePicker(DatePicker datePicker, Date retDate)
    {
        try
        {
            int day=datePicker.getDayOfMonth();
            int month=datePicker.getMonth();
            int year=datePicker.getYear();

            Calendar calendar=Calendar.getInstance();
            calendar.set(year, month, day);

            retDate.setTime(calendar.getTimeInMillis());
            return (true);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("getDateFromDatePicker", e.getMessage());
            return (false);
        }
    }

    public String MonthAsText(Integer pBudgetMonth)
    {
        if(pBudgetMonth==1)
            return("Jan");
        if(pBudgetMonth==2)
            return("Feb");
        if(pBudgetMonth==3)
            return("Mar");
        if(pBudgetMonth==4)
            return("Apr");
        if(pBudgetMonth==5)
            return("May");
        if(pBudgetMonth==6)
            return("Jun");
        if(pBudgetMonth==7)
            return("Jul");
        if(pBudgetMonth==8)
            return("Aug");
        if(pBudgetMonth==9)
            return("Sep");
        if(pBudgetMonth==10)
            return("Oct");
        if(pBudgetMonth==11)
            return("Nov");
        if(pBudgetMonth==12)
            return("Dec");
        return("???");
    }
    public String BudgetAsString(Integer pBudgetYear, Integer pBudgetMonth)
    {
        if(pBudgetYear==0 || pBudgetMonth==0)
            return("Budget Not Assigned");
        return(MonthAsText(pBudgetMonth) + " " + pBudgetYear.toString());
    }
    
    //
    // AddDays
    //   Description: accepts date - if this is equal to the rogue value 'unknown'
    //                then the retBoolean is returned true, else false
    //   Returns: true(worked)/false(failed)
    //
    public void AddDays(Date date, int days, Date retDate)
    {
        try
        {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.add(Calendar.DATE, days);

            retDate.setTime(calendar.getTimeInMillis());
    
        }
        catch(Exception e)
        {
            ErrorDialog.Show("AddDays", e.getMessage());
        }
    }

    public static Date BudgetStart(int pMonth, int pYear)
    {
        try
        {
            String lString = "26/" + pMonth + "/" + pYear;
            return(StrToDate(lString));
        }
        catch(Exception e)
        {
            ErrorDialog.Show("CurrentBudgetYear", e.getMessage());
        }
        return (new Date());
    }

    public static Date BudgetEnd(int pMonth, int pYear)
    {
        try
        {
            int lMonth=pMonth;
            int lYear=pYear;
            lMonth++;
            if(lMonth>12)
            {
                lMonth=1;
                lYear++;
            }
            String lString = "25/" + lMonth + "/" + lYear;
            return(StrToDate(lString));
        }
        catch(Exception e)
        {
            ErrorDialog.Show("CurrentBudgetYear", e.getMessage());
        }
        return (new Date());
    }

    public static String BudgetStartAsStr(int pMonth, int pYear)
    {
        return(DateUtils.DateToString(DateUtils.BudgetStart(pMonth, pYear)));
    }

    public static String BudgetEndAsStr(int pMonth, int pYear)
    {
        return(DateUtils.DateToString(DateUtils.BudgetEnd(pMonth, pYear)));
    }

    public Integer CurrentBudgetYear()
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            int lYear=calendar.get(Calendar.YEAR);
            int lMonth=calendar.get(Calendar.MONTH);
            int lDay=calendar.get(Calendar.DAY_OF_MONTH);
            if(lDay<26)
            {
                lMonth--;
                if(lMonth<0)
                {
                    lYear--;
                }
            }
            return(lYear);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("CurrentBudgetYear", e.getMessage());
        }
        return (0);
    }

    public Integer CurrentBudgetMonth()
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            int lMonth=calendar.get(Calendar.MONTH);
            int lDay=calendar.get(Calendar.DAY_OF_MONTH);
            if(lDay<26)
            {
                lMonth--;
                if(lMonth<0)
                {
                    lMonth=11;
                }
            }
            return(lMonth+1);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("CurrentBudgetMonth", e.getMessage());
        }
        return (0);
    }

    public Integer GetBudgetYear(Date pDate)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pDate);
            int lYear=calendar.get(Calendar.YEAR);
            int lMonth=calendar.get(Calendar.MONTH);
            int lDay=calendar.get(Calendar.DAY_OF_MONTH);
            if(lDay<26)
            {
                lMonth--;
                if(lMonth<0)
                {
                    lYear--;
                }
            }
            return(lYear);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("CurrentBudgetYear", e.getMessage());
        }
        return (0);
    }

    public Integer GetBudgetMonth(Date pDate)
    {
        try
        {
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pDate);
            int lMonth=calendar.get(Calendar.MONTH);
            int lDay=calendar.get(Calendar.DAY_OF_MONTH);
            if(lDay<26)
            {
                lMonth--;
                if(lMonth<0)
                {
                    lMonth=11;
                }
            }
            return(lMonth+1);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("CurrentBudgetMonth", e.getMessage());
        }
        return (0);
    }

    //
    // StrToDate
    //   Description: accepts string and sets retDate to the date equivalent
    //   Returns: true(worked)/false(failed)
    //
    public static Date StrToDate(String string)
    {
        try
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
            return(dateFormat.parse(string));
        }
        catch(ParseException e)
        {
            ErrorDialog.Show("StrToDate", e.getMessage());
            return (new Date());
        }
    }
    
    public void GetMonth(Date date, MyInt myInt)
    {
        try
        {
            String lString=new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
            myInt.Value=Integer.parseInt(lString);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("GetMonth", e.getMessage());
        }
    }

    public void GetDay(Date date, MyInt myInt)
    {
        try
        {
            String lString=new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
            myInt.Value=Integer.parseInt(lString);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("GetDay", e.getMessage());
        }
    }

    public void GetDayOfWeek(Date date, MyString myString)
    {
        try
        {
            myString.Value= new SimpleDateFormat("EEE", Locale.ENGLISH).format(date);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("GetDayOfWeek", e.getMessage());
        }
    }
    
    //
    // DatePickerToStr
    //   Description: accepts a DatePicker and extracts the date element
    //                and passes back the text version vases on the current
    //                date format through a MyString object
    //   Returns: true(worked)/false(failed)
    //
    boolean DatePickerToStr(DatePicker datePicker, MyString retString)
    {
        try
        {
            retString.Value = "";
    
            Date date = new Date();
            return getDateFromDatePicker(datePicker, date) && (DateToStr(date, retString));
    
        }
        catch(Exception e)
        {
            ErrorDialog.Show("DatePickerToStr", e.getMessage());
            return (false);
        }
    }


    public boolean DateTo_ddd_ddmmyyyy(Date date, MyString retString)
    {
        try
        {
            retString.Value = android.text.format.DateFormat.format("EEE, dd/MM/yyyy", date).toString();
            return(true);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("DateTo_ddd_ddmmyyyy", e.getMessage());
            return (false);
        }
    }

    public static String budgetFormat(int budgetMonth, int budgetYear)
    {
        return(String.format(Locale.ENGLISH, "%02d/%04d", budgetMonth, budgetYear));
    }

    public static String budgetMonthYear(int budgetMonth, int budgetYear)
    {
        return(context.getString(R.string.budgetyearmonthline,
                DateUtils.budgetFormat(budgetMonth,budgetYear)));
    }

    public static String getMonthLong(int month, int year)
    {
        Date d = DateUtils.DateFromComponents(year, month, 1);

        return( new SimpleDateFormat("MMMM").format(d));
    }

    public static String getMonthShort(int month, int year)
    {
        Date d = DateUtils.DateFromComponents(year, month, 1);

        return( new SimpleDateFormat("MMM").format(d));
    }

    public static Date DateFromComponents(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String DateToString(Date date)
    {
        return(android.text.format.DateFormat.format("dd/MM/yyyy", date).toString());
    }

    public static boolean DateToStr(Date date, MyString retString)
    {
        try
        {
            retString.Value = DateToString(date);
            return(true);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("DateToStr", e.getMessage());
            return (false);
        }
    }

    public Long StripTimeElement(Long argDate)
    {
        return(argDate-(argDate%86400000));
    }
}
