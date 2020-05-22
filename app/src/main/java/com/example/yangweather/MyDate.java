package com.example.yangweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yzz on 2020/5/19.
 */

public class MyDate extends SQLiteOpenHelper{
        Context mContext;
       public final String CREATE_CITY="create table city(" +
               "id integer primary key autoincrement," +
               "name text unique," +
               "code text unique)";

    public MyDate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
