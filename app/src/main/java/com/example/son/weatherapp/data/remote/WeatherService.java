package com.example.son.weatherapp.data.remote;

import com.example.son.weatherapp.data.model.WeatherDailyJSON;
import com.example.son.weatherapp.data.model.WeatherForecastJSON;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by S.O.N on 10/14/2017.
 */

public interface WeatherService {
    @GET("weather?units=metric&APPID=46d8dcf3c78c738ac884cbe6ce30196d")
    Call<WeatherDailyJSON> getCurrentWeatherInformationOfCity(@Query("q") String cityName);

    @GET("forecast?units=metric&APPID=46d8dcf3c78c738ac884cbe6ce30196d")
    Call<WeatherForecastJSON> getWeatherForecastInformationOfCity(@Query("q") String cityName);
}
