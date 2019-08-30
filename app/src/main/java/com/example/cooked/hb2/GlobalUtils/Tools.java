package com.example.cooked.hb2.GlobalUtils;

import android.view.View;

import java.util.Locale;

public class Tools
{
    public static String moneyFormat(Float value)
    {
        return(String.format(Locale.ENGLISH, "£%.2f", value));
    }

    public static String expenseFormat(Float value)
    {
        if(value==0.00)
            return(String.format(Locale.ENGLISH, "£%.2f", value));
        return(String.format(Locale.ENGLISH, "£%.2f", value*-1));
    }

    public static boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


    public static boolean toggleArrow(boolean show, View view) {
        return toggleArrow(show, view, true);
    }

    public static boolean toggleArrow(boolean show, View view, boolean delay) {
        if (show) {
            view.animate().setDuration(delay ? 200 : 0).rotation(180);
            return true;
        } else {
            view.animate().setDuration(delay ? 200 : 0).rotation(0);
            return false;
        }
    }

}
