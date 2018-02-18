package com.example.cooked.hb2.GlobalUtils;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.TimePicker;


import java.io.InputStream;

//
// All functions return true/false
//
public class MyApiSpecific
{
    public static MyApiSpecific apiSpecific=null;
    public static Context _context;

    public static void setContext(Context context)
    {
        _context=context;
    }
    public static MyApiSpecific myApiSpecific()
    {
        if(apiSpecific == null)
            apiSpecific=new MyApiSpecific();

        return (apiSpecific);
    }


    public int GetImageOrientation(Uri imageUri)
    {
        try
        {
            InputStream input = _context.getContentResolver().openInputStream(imageUri);
            ExifInterface ei;
            if (Build.VERSION.SDK_INT > 23)
                ei = new ExifInterface(input);
            else
                ei = new ExifInterface(imageUri.getPath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //myMessages().LogMessage("***ORIENTATION*** of " + imageUri.getPath() + " is " + String.valueOf(orientation));
            return orientation;
        }
        catch(Exception e)
        {
            ErrorDialog.Show("GetImageOrientation", e.getMessage());
        }
        return (0);
        
    }
    public int GetHour(TimePicker time)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                return (time.getCurrentHour());

            }
            return (time.getHour());
        }
        catch(Exception e)
        {
            ErrorDialog.Show("GetHour", e.getMessage());
        }
        return (0);

    }

    public int GetMinute(TimePicker time)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                return (time.getCurrentMinute());
            }
            return (time.getMinute());
        }
        catch(Exception e)
        {
            ErrorDialog.Show("GetMinute", e.getMessage());
        }
        return (0);
    }

    public void SetMinute(TimePicker time, int minute)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                time.setCurrentMinute(minute);
                return;
            } else
            {
                time.setMinute(minute);
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("SetMinute", e.getMessage());
        }

    }

    public void SetHour(TimePicker time, int hour)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                time.setCurrentHour(hour);
                return;
            } else
            {
                time.setHour(hour);
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("SetHour", e.getMessage());
        }

    }

    public int getTheColor(int resColor)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
                return (ContextCompat.getColor(_context, resColor));

            return (_context.getColor(resColor));
        }
        catch(Exception e)
        {
            ErrorDialog.Show("getListIcon", e.getMessage());
        }
        return (0);

    }


}
