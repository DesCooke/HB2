package com.example.cooked.hb2.GlobalUtils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Records.RecordTransaction;
import com.example.cooked.hb2.MainActivity;
import com.example.cooked.hb2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    @SuppressLint("StaticFieldLeak")
    private static MyDownloads myDL;
    private String downloadDirectory;
    private String txArchiveDirectory;
    private Pattern pattern;
    private ArrayList<String> mylist = new ArrayList<>();

    public static MyDownloads MyDL()
    {
        if (myDL == null)
            myDL = new MyDownloads(MainActivity.context);
        return (myDL);
    }

    private MyDownloads(Context context)
    {
        downloadDirectory = context.getResources().getString(R.string.download_directory);
        txArchiveDirectory = context.getResources().getString(R.string.tx_archive_directory);
        File f = new File(txArchiveDirectory);
        if (!f.isDirectory())
            f.mkdir();
        String expression = "\\d{8}_\\d{8}_\\d{4}.csv";
        pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        mylist = new ArrayList<>();
    }

    private boolean isBankFile(String filename)
    {
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    private Boolean loadFile(String filename)
    {
        String inputFile;
        String archiveFile;
        int l_LineNo = 0;
        MyLog.WriteLogMessage("Reading file " + downloadDirectory + "/" + filename);

        inputFile = downloadDirectory + "/" + filename;
        archiveFile = txArchiveDirectory + "/" + filename;

        try
        {
            List<RecordTransaction> resultList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String csvLine;
            while ((csvLine = reader.readLine()) != null)
            {
                l_LineNo++;
                if (l_LineNo == 1)
                {
                    continue;
                }

                String[] row = csvLine.split(",");

                RecordTransaction rt = new RecordTransaction();

                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                rt.TxDate = df.parse(row[0]);

                rt.BudgetYear = dateUtils().GetBudgetYear(rt.TxDate);
                rt.BudgetMonth = dateUtils().GetBudgetMonth(rt.TxDate);

                rt.TxType = row[1];
                rt.TxSortCode = row[2].substring(1);
                rt.TxAccountNumber = row[3];
                rt.TxDescription = row[4].replace("'", "");
                if (row[5].length() > 0)
                {
                    rt.TxAmount = Float.parseFloat(row[5]) * -1;
                } else
                {
                    rt.TxAmount = Float.parseFloat(row[6]);
                }
                rt.TxBalance = Float.parseFloat(row[7]);
                rt.TxFilename = filename;
                rt.TxAdded = new Date(System.currentTimeMillis());
                rt.TxLineNo = l_LineNo;
                rt.TxSeqNo = 0;
                rt.TxStatus = RecordTransaction.Status.NEW;
                resultList.add(0, rt);
            }
            reader.close();
            if (resultList.size() == 0)
                return (TRUE);
            Date lFirstDate = resultList.get(0).TxDate;
            Date lLastDate = resultList.get(resultList.size() - 1).TxDate;
            String lSortCode = resultList.get(resultList.size() - 1).TxSortCode;
            String lAccountNumber = resultList.get(resultList.size() - 1).TxAccountNumber;
            ArrayList<RecordTransaction> lInDBList = MyDatabase.MyDB().getTxDateRange(lFirstDate, lLastDate,
                    lSortCode, lAccountNumber);

            int FileIndex = 0;
            RecordTransaction DBrec;
            RecordTransaction Filerec;
            while (FileIndex < resultList.size())
            {
                Filerec = resultList.get(FileIndex);
                Boolean lFound = FALSE;
                for (int i = 0; i < lInDBList.size(); i++)
                {
                    DBrec = lInDBList.get(i);
                    if (DBrec.Equals(Filerec))
                    {
                        lFound = TRUE;
                        if ((Filerec.TxFilename.compareTo(DBrec.TxFilename) != 0) ||
                                (Filerec.TxLineNo.intValue() != DBrec.TxLineNo.intValue()))
                        {
                            MyDB().updateFilenameLineNo(DBrec, Filerec);
                            DBrec.TxFilename = Filerec.TxFilename;
                            DBrec.TxLineNo = Filerec.TxLineNo;
                        }
                        break;
                    }
                }
                if (lFound == FALSE)
                {
                    MyDB().addTransaction(Filerec);
                }
                FileIndex++;
            }

            File f1 = new File(inputFile);
            File f2 = new File(archiveFile);
            if (f2.exists())
                f2.delete();

            f1.renameTo(f2);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        return (TRUE);
    }

    public Boolean CollectFiles()
    {
        try
        {
            mylist.clear();
            File f = new File(downloadDirectory);
            File[] fl = f.listFiles();
            for (File aFl : fl)
            {
                if (isBankFile(aFl.getName()))
                {
                    mylist.add(aFl.getName());
                }
            }
            Collections.sort(mylist);
            for (int i = 0; i < mylist.size(); i++)
            {
                MyLog.WriteLogMessage("Loading File: " + mylist.get(i));
                if (loadFile(mylist.get(i)) == FALSE)
                    return (FALSE);
            }
        } catch (Exception e)
        {
            ErrorDialog.Show("Error in MyDownloads::CollectFiles", e.getMessage());
        }
        return (TRUE);
    }

}
