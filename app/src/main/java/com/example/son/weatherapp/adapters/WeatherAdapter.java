package com.example.son.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.son.weatherapp.R;
import com.example.son.weatherapp.data.model.WeatherList;
import com.example.son.weatherapp.utils.ApiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by S.O.N on 10/19/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>{

    private Context context;
    private List<WeatherList> weatherList;

    public class WeatherViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        TextView tvTemperature;
        ImageView ivWeather;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tv_time);
            tvTemperature = itemView.findViewById(R.id.tv_temperature);
            ivWeather = itemView.findViewById(R.id.iv_weather);
        }

        public void setData(WeatherList weather) {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatOutput = new SimpleDateFormat("MMMM d");

            Date date;
            String out = "";

            try {
                date = formatInput.parse(weather.getDtTxt());
                out = formatOutput.format(date.getTime());
            } catch (ParseException e) {
            }

            tvTime.setText(out.toString());
            tvTemperature.setText(weather.getMain().getTemp() + " ℃");
            ApiUtils.setWeatherIcon(weather.getWeather().get(0).getId(),
                    0,
                    0,
                    ivWeather, context);
        }
    }

    public WeatherAdapter(List<WeatherList> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_weather_day, parent, false);

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.setData(weatherList.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


}
