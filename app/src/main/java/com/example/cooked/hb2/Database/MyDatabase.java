package com.example.cooked.hb2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.MainActivity;

// derived from SQLiteOpenHelper because it give lots of features like - upgrade/downgrade
// automatically handles the creation and recreation of the database
public class MyDatabase extends SQLiteOpenHelper
{
    //region Member Variables
    // these regions help to compartmentalise the unit

    // The version - each change - increment by one
    // if the version increases onUpgrade is called - if decreases - onDowngrade is called
    // if current is 0 (does not exist) onCreate is called
    private static final int DATABASE_VERSION = 1;

    // the name of the database file (including path)
    private static final String DATABASE_NAME = "myHB";

    private static MyDatabase myDB;
    
    private TableTransaction tableTransaction;
    
    public static MyDatabase MyDB()
    {
        if(myDB==null)
          myDB = new MyDatabase(MainActivity.context);
        return(myDB);
    }
    //endregion

    //region Database functions
    public MyDatabase(Context context)
    {
        // super has to be the first command - can't put anything before it
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try
        {
            tableTransaction = new TableTransaction();
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::MyDatabase", e.getMessage());
        }
    }
    
    public void Check()
    {
    
    }

    // called when the current database version is 0
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            tableTransaction.onCreate(db);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::onCreate", e.getMessage());
        }
    }

    // called when the version number increases
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            tableTransaction.onUpgrade(db, oldVersion, newVersion);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::onUpgrade", e.getMessage());
        }
    }

    // called when the versino number decreases
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            tableTransaction.onDowngrade(db, oldVersion, newVersion);
        }
        catch(Exception e)
        {
            ErrorDialog.Show("Error in MyDatabase::onDowngrade", e.getMessage());
        }
    }
    //endregion
        
 }

 