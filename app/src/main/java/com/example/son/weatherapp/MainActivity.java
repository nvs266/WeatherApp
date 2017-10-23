package com.example.son.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.son.weatherapp.adapters.WeatherAdapter;
import com.example.son.weatherapp.data.model.WeatherDailyJSON;
import com.example.son.weatherapp.data.model.WeatherForecastJSON;
import com.example.son.weatherapp.data.model.WeatherList;
import com.example.son.weatherapp.data.remote.WeatherService;
import com.example.son.weatherapp.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Amen!!!";

    private WeatherService weatherService;

    private EditText etCity;
    private ImageView ivSearch;
    private TextView tvLocation;
    private ImageView ivWeather;
    private TextView tvDescription;
    private TextView tvTemperature;
    private TextView tvLastUpdate;
    private RecyclerView rvWeather;

    public static final String HUMIDITY = "Humidity: ";
    public static final String PRESSURE = "Pressure: ";


    public List<WeatherList> weatherList = new ArrayList<>();
    public WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        loadData("Ha Noi");
    }

    private void loadData(String cityName) {
        weatherService = ApiUtils.getWeatherService();
        weatherService.getCurrentWeatherInformationOfCity(cityName).enqueue(new Callback<WeatherDailyJSON>() {
            @Override
            public void onResponse(Call<WeatherDailyJSON> call, Response<WeatherDailyJSON> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    loadUI(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherDailyJSON> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        });

        weatherService.getWeatherForecastInformationOfCity(cityName).enqueue(new Callback<WeatherForecastJSON>() {
            @Override
            public void onResponse(Call<WeatherForecastJSON> call, Response<WeatherForecastJSON> response) {
                if (response.isSuccessful()) {
                    weatherList.clear();
                    List<WeatherList> list = response.body().getList();

                    for (int i = 1; i < list.size(); i++) {
                        if (((i+8) % 8 == 0) && (i + 8 <= list.size())) {
                            weatherList.add(list.get(i));
                        }
                    }

                    weatherAdapter.notifyDataSetChanged();

//                    for (WeatherList weather: weatherList) {
//                        Log.d(TAG, "onResponse: "  + weather.toString());
//                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastJSON> call, Throwable t) {
                Log.d(TAG, "onFailure:");
            }
        });
    }

    private void loadUI(WeatherDailyJSON body) {
        tvLocation.setText(body.getName().toUpperCase(Locale.US) +
                ", " +
                body.getSys().getCountry());

        tvDescription.setText(
                body.getWeather().get(0).getDescription().toUpperCase(Locale.US) +
                        "\n" + "Humidity: " + body.getMain().getHumidity() + " %" +
                        "\n" + "Pressure: " + body.getMain().getPressure() + " hPa");

        tvTemperature.setText(body.getMain().getTemp() + " ℃");

        Date date = new Date();
        date.setTime(body.getDt().longValue() * 1000);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");

        tvLastUpdate.setText("Last update: " + format.format(date.getTime()));

        ApiUtils.setWeatherIcon(body.getWeather().get(0).getId(),
                body.getSys().getSunrise() * 1000,
                body.getSys().getSunset() * 1000,
                ivWeather, this);
    }

    private void setupUI() {
        etCity = (EditText) findViewById(R.id.et_city_name);
        etCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        ivSearch = (ImageView) findViewById(R.id.bt_search);
        tvLocation = (TextView) findViewById(R.id.tv_country);
        ivWeather = (ImageView) findViewById(R.id.iv_weather);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvLastUpdate = (TextView) findViewById(R.id.tv_last_update);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        rvWeather = (RecyclerView) findViewById(R.id.rv_weather);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weatherAdapter = new WeatherAdapter(weatherList);
        rvWeather.setAdapter(weatherAdapter);
        rvWeather.setLayoutManager(linearLayoutManager);
    }

    private void performSearch() {
        String cityName = etCity.getText().toString();
        if (cityName.equals("")) {
            Toast.makeText(MainActivity.this, "!!!Enter City", Toast.LENGTH_SHORT).show();
        } else {
            etCity.setText("");
            loadData(cityName);
        }
    }
}
