package com.example.yangweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yangweather.gson.CityInfo;
import com.example.yangweather.gson.Forecast;
import com.example.yangweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    TextView cityName;
    TextView cityTemperature;
    TextView updatetime;
    String address;
    private TextView quality;
    private TextView pm10;
    private TextView pm25;

    public DrawerLayout drawerLayout;
    private Button navButton;

    SwipeRefreshLayout swipeRefresh;
    ImageView bing_pic_img;
    String mWeatherId;
    List<Forecast> mforecastList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        bing_pic_img=(ImageView)findViewById(R.id.bing_pic_img);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navButton=(Button)findViewById(R.id.nav_button);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);    //获取本地缓存
        final String infomation = prefs.getString("weather", null);                                             //得到本地缓存字符串
        Intent intent= getIntent();                                                                             //得到搜索页面的integer对象
        if(infomation==null) {                                                                                  //如果本地缓存为空，联网搜搜
             address = intent.getStringExtra("address");                                                        //从搜索页面传递来的integer对象，得到网址
            HttpWeather(address);                                                                               //联网搜索
            mWeatherId=intent.getStringExtra("weatherId");
        }else   {                                                                                               //否则，本地有缓存时，解析本地缓存
            runOnUiThread(new Runnable() {
                @Override
                public void run() {                                                                                 //这是主线程耗时活动
                    Weather weather= handleWeatherResponse(infomation);                                         //解析本地缓存
                    showMessage(weather);                                                                       //将缓存文件里的数据放入组件当中
                 mWeatherId=weather.cityInfo.citykey;
                }
            });
        }

        final String bing_pic_message=prefs.getString("bing_pic",null);
        if(bing_pic_message!=null){
                    Glide.with(WeatherActivity.this).load(bing_pic_message).into(bing_pic_img);
        }else {
            loadBingPic();
        }




        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                HttpWeather("http://t.weather.sojson.com/api/weather/city/"+mWeatherId);

            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }





    }

    public static Weather handleWeatherResponse(String response){
        return new Gson().fromJson(response,Weather.class);
    }




    public void HttpWeather(String address){
        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);                  //下拉刷新完会消失

                        Toast.makeText(WeatherActivity.this,"出错了",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               final String info=response.body().string();
                SharedPreferences.Editor prefs= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                prefs.putString("weather",info);
                prefs.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Weather weather= handleWeatherResponse(info);
                        if(weather!=null&&"200".equals(weather.status)){
                            showMessage(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"出错了",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);

                    }
                });
                loadBingPic();

            }

        });
    }

    public void showMessage(Weather weather){
        mWeatherId=weather.cityInfo.citykey;                                                //关于开头的一些标题
        cityName = (TextView) findViewById(R.id.city_name);
        cityTemperature = (TextView) findViewById(R.id.city_temperature);
        updatetime=(TextView)findViewById(R.id.updatetime);
        cityName.setText(weather.cityInfo.city);
        cityTemperature.setText(weather.data.wendu+"℃");

        Calendar cal= Calendar.getInstance();
        int h=cal.get(Calendar.HOUR_OF_DAY);                                                //获得当前小时
        int mi=cal.get(Calendar.MINUTE);                                                    //获得当前分钟
        updatetime.setText(h+":"+mi);                                                       //赋值当前时间
                                                                                            //关于开头的一些标题

        mforecastList.clear();
        for(Forecast forecast:weather.data.forecastList){                                   //关于未来天气
            mforecastList.add(forecast);
        }
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview_forecast);
        LinearLayoutManager manager=new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        MyAdapter myAdapter=new MyAdapter(mforecastList);
        recyclerView.setAdapter(myAdapter);                                                     //关于未来天气

        quality = (TextView) findViewById(R.id.quality);                                        //关于空气质量
        pm10 = (TextView) findViewById(R.id.pm10);
        pm25 = (TextView) findViewById(R.id.pm25);
        quality.setText(weather.data.quality);
        pm10.setText(weather.data.pm10);
        pm25.setText(weather.data.pm25);                                                    //关于空气质量



    }

    void loadBingPic(){
        String imageaddress="http://guolin.tech/api/bing_pic";          //存放图片网址的网址
        HttpUtil.sendHttpRequest(imageaddress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WeatherActivity.this,"获取图片失败",Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
            });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String imfo = response.body().string();
                SharedPreferences.Editor prefs=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                prefs.putString("bing_pic",imfo);
                prefs.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            Glide.with(WeatherActivity.this).load(imfo).into(bing_pic_img);


                    }
                });

            }
        });



    }

}
