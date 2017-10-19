package com.example.son.weatherapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.son.weatherapp.R;
import com.example.son.weatherapp.data.remote.RetrofitClient;
import com.example.son.weatherapp.data.remote.WeatherService;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by S.O.N on 10/14/2017.
 */

public class ApiUtils {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    public static WeatherService getWeatherService() {
        return RetrofitClient.getInstance(BASE_URL).create(WeatherService.class);
    }

    public static void setWeatherIcon(int actualId, long sunrise, long sunset, ImageView imageView, Context context) {
        int id = actualId / 100;
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset || (sunrise == 0)) {
                Picasso.with(context).load(R.drawable.ic_sun_100px).into(imageView);
            } else {
                Picasso.with(context).load(R.drawable.ic_moon_100px).into(imageView);
            }
        } else {
            switch(id) {
                case 2 :
                    Picasso.with(context).load(R.drawable.ic_lightning_bolt_100px).into(imageView);
                    break;
                case 3 :
                    Picasso.with(context).load(R.drawable.ic_hail_100px).into(imageView);
                    break;
                case 7 :
                    Picasso.with(context).load(R.drawable.ic_windy_100px).into(imageView);
                    break;
                case 8 :
                    Picasso.with(context).load(R.drawable.ic_clouds_100px).into(imageView);
                    break;
                case 6 :
                    Picasso.with(context).load(R.drawable.ic_snow_100px).into(imageView);
                    break;
                case 5 :
                    Picasso.with(context).load(R.drawable.ic_rain_100px).into(imageView);
                    break;
            }
        }
    }
}
