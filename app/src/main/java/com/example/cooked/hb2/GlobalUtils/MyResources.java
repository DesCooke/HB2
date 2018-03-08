package com.example.cooked.hb2.GlobalUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

public class MyResources
{
    @SuppressLint("StaticFieldLeak")
    private static Context _context;

    public static void setContext(Context context)
    {
        _context = context;
    }

    public static Resources R()
    {
        if(_context != null)
            return(_context.getResources());
        return(null);
    }
}
