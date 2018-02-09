package com.example.cooked.hb2.GlobalUtils;

import android.content.Context;
import android.os.Environment;

import com.example.cooked.hb2.Database.RecordTransaction;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDownloads
{
    private static MyDownloads myDL;
    private String downloadDirectory;
    private Context _context;
    private Pattern pattern;
    private ArrayList<String> mylist = new ArrayList<String>();
    public static MyDownloads MyDL()
    {
        if(myDL==null)
          myDL = new MyDownloads(MainActivity.context);
        return(myDL);
    }
    
    private MyDownloads(Context context)
    {
        _context = context;
        try
        {
            downloadDirectory = context.getResources().getString(R.string.download_directory);
            String expression = "\\d{8}_\\d{8}_\\d{4}.csv";
            pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            mylist = new ArrayList<String>();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDownloads::MyDownloads", e.getMessage());
        }
        
    }
    
    public boolean isBankFile(String filename)
    {
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    public void loadFile(String filename)
    {
        try
        {
            MyLog.WriteLogMessage("Reading file " + downloadDirectory + "/" + filename);
            
            List resultList = new ArrayList();
            BufferedReader reader = new BufferedReader(new FileReader(downloadDirectory + "/" + filename));
            String csvLine;
            while ((csvLine = reader.readLine()) != null)
            {
                String[] row = csvLine.split(",");
                
                RecordTransaction rt = new RecordTransaction();
                
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                rt.TxDate = df.parse(row[0]);

                rt.TxDescription = row[4];
                
                resultList.add(rt);
            }
            reader.close();
            for (int row=0;row < resultList.size();row++)
            {
                MyLog.WriteLogMessage("Date: " + ((RecordTransaction)resultList.get(row)).TxDate.toString());
                MyLog.WriteLogMessage("Description: " + ((RecordTransaction)resultList.get(row)).TxDescription);
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDownloads::loadFile", e.getMessage());
        }
    }
    
    public void CollectFiles()
    {
        try
        {
            mylist.clear();
            File f = new File(downloadDirectory);
            File[] fl = f.listFiles();
            for (int i = 0; i < fl.length; i++)
            {
                if (isBankFile(fl[i].getName()))
                {
                    mylist.add(fl[i].getName());
                }
            }
            Collections.sort(mylist);
            for (int i = 0; i < mylist.size(); i++)
            {
                MyLog.WriteLogMessage("Loading File: " + mylist.get(i));
                loadFile(mylist.get(i));
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDownloads::CollectFiles", e.getMessage());
        }
    }
    
}
