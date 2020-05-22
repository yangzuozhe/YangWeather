package com.example.yangweather;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                                                     //这是搜索页面
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (prefs.getString("weather", null) != null) {                                             //如果本地有缓存
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);                      //直接跳转到天气页面
            startActivity(intent);
            finish();

        } else {
            MyDate myDate = new MyDate(MainActivity.this, "City.db", null, 1);
            SQLiteDatabase db = myDate.getWritableDatabase();
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put("name", "泉州市");
                values.put("code", "101230501");
                db.insert("city", null, values);
                values.clear();
                values.put("name", "福州市");
                values.put("code", "101230101");
                db.insert("city", null, values);
                values.clear();
            }


        }
    }


}
