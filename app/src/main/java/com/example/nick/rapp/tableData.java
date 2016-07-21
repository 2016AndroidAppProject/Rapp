package com.example.nick.rapp;

import android.provider.BaseColumns;

/**
 * Created by Nick on 7/19/2016.
 */
public class tableData {

    public tableData(){

    }

    //inherits a primary key field called ID
    public static abstract class TableInfo implements BaseColumns
    {
        public static final String USER_NAME = "user_name";
        public static final String USER_PASSWORD = "user_password";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "account_info";
    }
}
