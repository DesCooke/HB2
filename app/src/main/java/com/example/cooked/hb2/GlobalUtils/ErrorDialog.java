package com.example.cooked.hb2.GlobalUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;

public class ErrorDialog
{
    public static void Show(String title, String description)
    {
        MyLog.WriteLogMessage("Error:" + title + ", " + description);
    }

}
