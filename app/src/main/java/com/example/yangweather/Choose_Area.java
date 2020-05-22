package com.example.yangweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by yzz on 2020/5/18.
 */

public class Choose_Area extends Fragment {
    Button search_yes;
    EditText search_city;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.choose_area,container,false);
        search_yes=(Button)view.findViewById(R.id.search_yes);
        search_city=(EditText)view.findViewById(R.id.search_city);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        search_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        if(getActivity()instanceof WeatherActivity){                                //如果页面是在天气页面那里，下拉刷新数据

            WeatherActivity activity=(WeatherActivity)getActivity();
            String name=search_city.getText().toString();
            search(name,activity);
            search_city.setText("");

        }else if(getActivity()instanceof MainActivity){                                 //如果页面是在搜索的页面碎片里，那么就跳转到天气页面

                    String name=search_city.getText().toString();

                    MyDate myDate=new MyDate(getContext(),"City.db",null,1);
                    SQLiteDatabase db=myDate.getWritableDatabase();
                    Cursor cursor=db.query("city",null,null,null,null,null,null);
                    String cityname=null;
                    String citycode=null;
                    String address=null;
                    if(cursor!=null){
                        if(cursor.moveToFirst()) {
                            do {
                                cityname = cursor.getString(cursor.getColumnIndex("name"));
                                if (cityname.equals(name)) {
                                    citycode = cursor.getString(cursor.getColumnIndex("code"));
                                    break;
                                }
                            } while (cursor.moveToNext());
                        }
                        if(citycode!=null) {
                            address = "http://t.weather.sojson.com/api/weather/city/" + citycode;
                            Intent intent = new Intent(getContext(), WeatherActivity.class);
                            intent.putExtra("address", address);
                            intent.putExtra("weatherId",citycode);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            Toast.makeText(getContext(),"没有数据",Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.close();



                }



        }
        });
        }

        public void search(String name,WeatherActivity activity){

            MyDate myDate=new MyDate(getContext(),"City.db",null,1);
            SQLiteDatabase db=myDate.getWritableDatabase();
            Cursor cursor=db.query("city",null,null,null,null,null,null);
            String cityname=null;
            String citycode=null;
            String address=null;
            if(cursor.moveToFirst()) {
                do {
                    cityname = cursor.getString(cursor.getColumnIndex("name"));
                    if (cityname.equals(name)) {
                        citycode = cursor.getString(cursor.getColumnIndex("code"));
                        break;
                    }
                } while (cursor.moveToNext());
            }
            if(citycode!=null) {
                activity.drawerLayout.closeDrawers();
                activity.swipeRefresh.setRefreshing(true);
                address = "http://t.weather.sojson.com/api/weather/city/" + citycode;
                activity.HttpWeather(address);
            }else {
                Toast.makeText(getContext(),"没有数据",Toast.LENGTH_SHORT).show();
            }
        }

}
