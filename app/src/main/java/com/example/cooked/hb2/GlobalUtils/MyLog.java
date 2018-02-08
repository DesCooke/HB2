package com.example.cooked.hb2.GlobalUtils;

import android.content.Context;
import android.content.res.Resources;

import com.example.cooked.hb2.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;

//
// Simple class containing File Utility functions
//
public class MyLog
{
    private static Resources res;
    
    public static void SetContext(Context context)
    {
        res = context.getResources();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void WriteLogMessage(String argString)
    {
        try
        {
            String homeDirectory=res.getString(R.string.home_directory);
            String logfilename=res.getString(R.string.log_filename);

            // create a File object from it
            File file=new File(homeDirectory + '/' + logfilename);
            if(!file.exists()) {
                File dir = new File(homeDirectory);
                if (!dir.exists())
                    dir.mkdir();

                if (!file.createNewFile())
                    throw new Exception("file.CreateNewFile() returned false");
            }

            String timeStamp=DateFormat.getDateTimeInstance().format(new Date());

            FileWriter fw=new FileWriter(file, true);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(timeStamp + ":" + argString + "\n");
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyLog::WriteLogMessage", e.getMessage());
        }
    }

    public static void RemoveLog()
    {
        try
        {
            String homeDirectory=res.getString(R.string.home_directory);
            String logfilename=res.getString(R.string.log_filename);

            // create a File object from it
            File file=new File(homeDirectory + '/' + logfilename);
            if(file.exists())
                if(!file.delete())
                    throw new Exception("file.delete() returned false");
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyLog::RemoveLog", e.getMessage());
        }
    }
}