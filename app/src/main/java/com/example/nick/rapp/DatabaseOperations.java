package com.example.nick.rapp;

/**
 * Created by Nick on 7/19/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;

    public String CREATE_QUERY = "CREATE TABLE "+
            tableData.TableInfo.TABLE_NAME+"(" + tableData.TableInfo.USER_NAME+" TEXT PRIMARY KEY,"
            + tableData.TableInfo.USER_PASSWORD+" TEXT," + tableData.TableInfo.USER_TYPE+" TEXT);";

    public DatabaseOperations(Context context) {
            //, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, tableData.TableInfo.DATABASE_NAME, null , database_version);
        Log.d("Database operations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //This method is used to add new users to the app.
    //It is meant to test the functionality of inserting data into the SQLlite database.
    //currently it is linked to a button on the adminview_screen but in the final product
    //it will be on its own screen farther into settings.
    public void addNewUser(DatabaseOperations dop, String userName, String password, String userType){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.TableInfo.USER_NAME, userName);
        cv.put(tableData.TableInfo.USER_PASSWORD, password);
        cv.put(tableData.TableInfo.USER_TYPE, userType);
        long k = SQLD.insert(tableData.TableInfo.TABLE_NAME, null, cv);

        Log.d("Database operations", "New user inserted");
    }



    public Cursor getUserCredentials(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.TableInfo.USER_NAME, tableData.TableInfo.USER_PASSWORD, tableData.TableInfo.USER_TYPE};
        Cursor CR = SQ.query(tableData.TableInfo.TABLE_NAME, columns, null, null, null, null, null);

        return CR;


    }






}
