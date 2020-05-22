package com.example.yangweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yzz on 2020/5/19.
 */

public class Data {
    public String ganmao;
    public String pm10;
    public String pm25;
    public String quality;
    public String shidu;
    public String wendu;
    @SerializedName("forecast")
    public List<Forecast> forecastList;

}
