package com.example.yangweather;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yzz on 2020/5/19.
 */

public class HttpUtil {
   public static void sendHttpRequest(String address,okhttp3.Callback callback){
       OkHttpClient client=new OkHttpClient();
       Request request=new Request.Builder().url(address).build();
       client.newCall(request).enqueue(callback);
   }
}
