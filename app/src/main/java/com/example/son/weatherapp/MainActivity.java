package com.example.son.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.son.weatherapp.data.model.MainObjectJSON;
import com.example.son.weatherapp.data.remote.WeatherService;
import com.example.son.weatherapp.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static final String HUMIDITY = "Humidity: ";
    public static final String PRESSURE = "Pressure: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        loadData("Ha Noi");
    }

    private void loadData(String cityName) {
        weatherService = ApiUtils.getWeatherService();
        weatherService.getWeatherInformationOfCity(cityName).enqueue(new Callback<MainObjectJSON>() {
            @Override
            public void onResponse(Call<MainObjectJSON> call, Response<MainObjectJSON> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    loadUI(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MainObjectJSON> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadUI(MainObjectJSON body) {
        tvLocation.setText(body.getName().toUpperCase(Locale.US) +
                ", " +
                body.getSys().getCountry());

        tvDescription.setText(
                body.getWeather().get(0).getDescription().toUpperCase(Locale.US) +
                        "\n" + "Humidity: " + body.getMain().getHumidity() + " %" +
                        "\n" + "Pressure: " + body.getMain().getPressure() + " hPa");

        double c = body.getMain().getTemp();
        tvTemperature.setText(String.format("%.2f ℃", c));

        Date date = new Date();
        date.setTime(body.getDt().longValue() * 1000);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");

        tvLastUpdate.setText("Last update: " + format.format(date.getTime()));

        setWeatherIcon(body.getWeather().get(0).getId(),
                body.getSys().getSunrise() * 1000,
                body.getSys().getSunset() * 1000);
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                ivWeather.setImageResource(R.drawable.ic_sun_100px);
            } else {
                ivWeather.setImageResource(R.drawable.ic_moon_100px);
            }
        } else {
            switch(id) {
                case 2 :
                    ivWeather.setImageResource(R.drawable.ic_lightning_bolt_100px);
                    break;
                case 3 :
                    ivWeather.setImageResource(R.drawable.ic_hail_100px);
                    break;
                case 7 :
                    ivWeather.setImageResource(R.drawable.ic_windy_100px);
                    break;
                case 8 :
                    ivWeather.setImageResource(R.drawable.ic_clouds_100px);
                    break;
                case 6 :
                    ivWeather.setImageResource(R.drawable.ic_snow_100px);
                    break;
                case 5 :
                    ivWeather.setImageResource(R.drawable.ic_rain_100px);
                    break;
            }
        }
    }

    private void setupUI() {
        etCity = (EditText) findViewById(R.id.et_city_name);
        ivSearch = (ImageView) findViewById(R.id.bt_search);
        tvLocation = (TextView) findViewById(R.id.tv_country);
        ivWeather = (ImageView) findViewById(R.id.iv_weather);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvLastUpdate = (TextView) findViewById(R.id.tv_last_update);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = etCity.getText().toString();
                if (cityName.equals("")) {
                    Toast.makeText(MainActivity.this, "!!!Enter City", Toast.LENGTH_SHORT).show();
                } else {
                    etCity.setText("");
                    loadData(cityName);
                }
            }
        });
    }
}
