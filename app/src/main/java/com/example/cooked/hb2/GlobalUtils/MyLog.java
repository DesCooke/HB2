package com.example.cooked.hb2.GlobalUtils;

import android.content.res.Resources;

import com.example.cooked.hb2.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;

import static java.lang.Boolean.TRUE;

//
// Simple class containing File Utility functions
//
public class MyLog
{
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void WriteLogMessage(String argString)
    {
        try
        {
            String homeDirectory=MyResources.R().getString(R.string.home_directory);
            String logfilename=MyResources.R().getString(R.string.log_filename);

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

            FileWriter fw = new FileWriter(file, /*append*/ TRUE);
            
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(timeStamp + ":" + argString + "\n");
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            String myerror = e.getMessage();
        }
    }
}