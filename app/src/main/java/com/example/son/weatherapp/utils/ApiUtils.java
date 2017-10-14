package com.example.son.weatherapp.utils;

import com.example.son.weatherapp.data.remote.RetrofitClient;
import com.example.son.weatherapp.data.remote.WeatherService;

/**
 * Created by S.O.N on 10/14/2017.
 */

public class ApiUtils {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    public static WeatherService getWeatherService() {
        return RetrofitClient.getInstance(BASE_URL).create(WeatherService.class);
    }
}
