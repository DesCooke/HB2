package com.example.cooked.hb2.GlobalUtils;

import android.content.Context;
import android.os.Environment;

import com.example.cooked.hb2.Database.MyDatabase;
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

import static com.example.cooked.hb2.Database.MyDatabase.MyDB;
import static com.example.cooked.hb2.GlobalUtils.DateUtils.dateUtils;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MyDownloads
{
    private static MyDownloads myDL;
    private String downloadDirectory;
    private String txArchiveDirectory;
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
            txArchiveDirectory = context.getResources().getString(R.string.tx_archive_directory);
            File f = new File(txArchiveDirectory);
            if(!f.isDirectory())
                f.mkdir();
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

    public Boolean loadFile(String filename)
    {
        String inputFile;
        String archiveFile;
        int l_LineNo=1;
        try
        {
            MyLog.WriteLogMessage("Reading file " + downloadDirectory + "/" + filename);
            
            inputFile = downloadDirectory + "/" + filename;
            archiveFile = txArchiveDirectory + "/" + filename;
            
            List resultList = new ArrayList();
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String csvLine;
            csvLine = reader.readLine();
            while ((csvLine = reader.readLine()) != null)
            {
                l_LineNo++;

                String[] row = csvLine.split(",");
                
                RecordTransaction rt = new RecordTransaction();
                
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                rt.TxDate = df.parse(row[0]);

                rt.BudgetYear = dateUtils().GetBudgetYear(rt.TxDate);
                rt.BudgetMonth = dateUtils().GetBudgetMonth(rt.TxDate);

                rt.TxType = row[1];
                rt.TxSortCode = row[2].substring(1);
                rt.TxAccountNumber = row[3];
                rt.TxDescription = row[4];
                if (row[5].length() > 0) {
                    rt.TxAmount = Float.parseFloat(row[5])*-1;
                }
                else
                {
                    rt.TxAmount = Float.parseFloat(row[6]);
                }
                rt.TxBalance = Float.parseFloat(row[7]);
                rt.TxFilename = filename;
                rt.TxAdded = new Date(System.currentTimeMillis());
                rt.TxLineNo = l_LineNo;
                rt.TxSeqNo = 0;
                rt.TxStatus = RecordTransaction.Status.NEW;
                resultList.add(0,rt);
            }
            reader.close();
            if(resultList.size()==0)
                return(TRUE);
            Date lFirstDate = ((RecordTransaction)resultList.get(0)).TxDate;
            Date lLastDate = ((RecordTransaction)resultList.get(resultList.size()-1)).TxDate;
            String lSortCode = ((RecordTransaction)resultList.get(resultList.size()-1)).TxSortCode;
            String lAccountNumber = ((RecordTransaction)resultList.get(resultList.size()-1)).TxAccountNumber;
            ArrayList<RecordTransaction> lInDBList = MyDatabase.MyDB().getTxDateRange(lFirstDate, lLastDate,
                    lSortCode, lAccountNumber);

            int DBIndex=0;
            int FileIndex=0;
            RecordTransaction DBrec;
            RecordTransaction Filerec;
            while (FileIndex < resultList.size())
            {
                Filerec = ((RecordTransaction)resultList.get(FileIndex));
                Boolean lFound=FALSE;
                for(int i=0;i<lInDBList.size();i++)
                {
                    DBrec = ((RecordTransaction)lInDBList.get(i));
                    if(DBrec.Equals(Filerec))
                    {
                        lFound=TRUE;
                        if( (Filerec.TxFilename.compareTo(DBrec.TxFilename)!=0) ||
                            (Filerec.TxLineNo != DBrec.TxLineNo) )
                        {
                            MyDB().updateFilenameLineNo(DBrec, Filerec);
                            DBrec.TxFilename = Filerec.TxFilename;
                            DBrec.TxLineNo = Filerec.TxLineNo;
                        }
                        break;
                    }
                }
                if(lFound==FALSE)
                {
                    MyDB().addTransaction(Filerec);
                }
                FileIndex++;
            }
            
            File f1 = new File(inputFile);
            File f2 = new File(archiveFile);
            if(f2.exists())
                f2.delete();
            
            f1.renameTo(f2);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDownloads::loadFile", e.getMessage());
            return(FALSE);
        }
        return(TRUE);
    }
    
    public Boolean CollectFiles()
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
                if (loadFile(mylist.get(i))==FALSE)
                    return(FALSE);
            }
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDownloads::CollectFiles", e.getMessage());
        }
        return(TRUE);
    }
    
}
