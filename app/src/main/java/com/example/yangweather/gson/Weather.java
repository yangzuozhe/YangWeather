package com.example.yangweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzz on 2020/5/19.
 */

public class Weather {
    public String date;
    public String status;
    public CityInfo cityInfo;
    public Data data;

}
