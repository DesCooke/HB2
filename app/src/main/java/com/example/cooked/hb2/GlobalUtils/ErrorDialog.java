package com.example.cooked.hb2.GlobalUtils;

public class ErrorDialog
{
    public static void Show(String title, String description)
    {
        MyLog.WriteLogMessage("Error:" + title + ", " + description);
    }

}
