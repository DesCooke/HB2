package com.example.cooked.hb2.GlobalUtils;

import com.example.cooked.hb2.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;

import static java.lang.Boolean.TRUE;

public class MyLog
{
    private static File getLogFilename()
    {
        if(MyResources.R()==null)
            return(null);

        String homeDirectory = MyResources.R().getString(R.string.home_directory);
        String logfilename = MyResources.R().getString(R.string.log_filename);

        // create a File object from it
        return(new File(homeDirectory + '/' + logfilename));
    }

    private static File getHomeDirectory()
    {
        if(MyResources.R()==null)
            return(null);

        String homeDirectory = MyResources.R().getString(R.string.home_directory);

        // create a File object from it
        return(new File(homeDirectory));
    }

    public static void ClearLog()
    {
        try
        {
            File file=getLogFilename();
            if(file==null)
                return;

            File dir=getHomeDirectory();
            if(dir==null)
                return;

            // create a File object from it
            if(!file.exists())
            {
                if (!dir.exists())
                    dir.mkdir();
            }
            else
            {
                file.delete();
            }

            if (!file.createNewFile())
                throw new Exception("file.CreateNewFile() returned false");

            String timeStamp=DateFormat.getDateTimeInstance().format(new Date());

            FileWriter fw = new FileWriter(file, /*append*/ TRUE);

            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(timeStamp + ":New Log Created\n");
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            //
        }
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void WriteLogMessage(String argString)
    {
        try
        {
            File file=getLogFilename();
            if(file==null)
                return;

            File dir=getHomeDirectory();
            if(dir==null)
                return;

            // create a File object from it
            if(!file.exists()) {
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
            //
        }
    }
}