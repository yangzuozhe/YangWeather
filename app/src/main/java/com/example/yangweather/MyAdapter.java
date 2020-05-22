package com.example.yangweather;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yangweather.gson.Forecast;

import java.util.List;

/**
 * Created by yzz on 2020/5/19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<Forecast> mForecastList;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView week;
        public TextView high;
        public TextView low;
        public TextView type;
        public TextView ymd;
        public ViewHolder(View itemView) {
            super(itemView);
            week = (TextView)itemView.findViewById(R.id.week);
            high = (TextView)itemView.findViewById(R.id.high);
            low = (TextView)itemView.findViewById(R.id.low);
            type = (TextView)itemView.findViewById(R.id.type);
            ymd=(TextView)itemView.findViewById(R.id.ymd);
        }
    }
   public MyAdapter(List<Forecast> forecastList){
       mForecastList=forecastList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forecastitem,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    Forecast forecast=mForecastList.get(position);
        holder.high.setText(forecast.high);
        holder.low.setText(forecast.low);
        holder.type.setText(forecast.type);
        holder.week.setText(forecast.week);
        holder.ymd.setText(forecast.ymd);
    }

    @Override
    public int getItemCount() {
        return mForecastList.size();
    }


}
