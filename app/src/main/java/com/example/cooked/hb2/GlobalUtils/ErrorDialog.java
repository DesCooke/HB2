package com.example.cooked.hb2.GlobalUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;

public class ErrorDialog
{
    @SuppressLint("StaticFieldLeak")
    private static Context myContext;

    static void SetContext(Context context)
    {
        myContext = context;
    }

    public static void Show(String title, String description)
    {
        if (myContext == null)
            return;

        MyLog.WriteLogMessage("Error:" + title + ", " + description);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(myContext);
        messageBox.setTitle(title);
        messageBox.setMessage(description);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

}
