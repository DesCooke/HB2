package com.example.cooked.hb2.GlobalUtils;

import java.util.Locale;

public class IntUtils
{
    public static String IntToStr(int value)
    {
        return(String.format(Locale.getDefault(),"%d", value));
    }
}
