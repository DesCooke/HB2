package com.example.cooked.hb2.GlobalUtils;

import android.content.Context;
import android.content.res.Resources;

import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;

import static com.example.cooked.hb2.MainActivity.context;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//
// Simple class containing File Utility functions
//
public class MyLog
{
    private static Resources res;
    private static Context mycontext;
    
    public static void SetContext(Context context)
    {
        mycontext = context;
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

            FileWriter fw = new FileWriter(file, /*append*/ TRUE);
            
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
}