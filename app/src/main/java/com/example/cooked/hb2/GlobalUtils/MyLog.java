package com.example.cooked.hb2.GlobalUtils;

import com.example.cooked.hb2.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static String getLogFilenameText()
    {
        if(MyResources.R()==null)
            return(null);

        String homeDirectory = MyResources.R().getString(R.string.home_directory);
        String logfilename = MyResources.R().getString(R.string.log_filename);

        // create a File object from it
        return(homeDirectory + '/' + logfilename);
    }

    public static String GetText()
    {
        String file=getLogFilenameText();
        if(file==null)
            return("");

        try
        {
            String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
            return(text);
        } catch( Exception e)
        {
            return("");
        }


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

    public static void WriteExceptionMessage(Exception e)
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
            bw.write("==============================================\n");
            bw.write("ERROR at " + timeStamp + "\n");
            bw.write("==============================================\n");
            bw.write(e.getMessage() + "\n");

            StringBuilder sb=new StringBuilder();
            sb.append("  ");
            StackTraceElement ste[] = e.getStackTrace();
            for(int i=0;i<ste.length; i++)
            {
                bw.write(sb.toString() +
                        ste[i].getFileName() + ":" +
                        ste[i].getClassName() + ":" +
                        ste[i].getMethodName() + ":" +
                        ste[i].getLineNumber() + "\n");
                sb.append("  ");
            }

            bw.flush();
            bw.close();
        }
        catch(Exception ex)
        {
            //
        }
    }
}