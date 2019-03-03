package ir.goldenmind.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.adapters.ForecastAdapter;
import ir.goldenmind.weather.model.openweathermap.current.CurrentWeatherResponse;
import ir.goldenmind.weather.model.openweathermap.forecast.Weather;
import ir.goldenmind.weather.model.openweathermap.forecast.WeatherResponse;

public class CurrentWeatherFragment extends Fragment {

    static final String currentWeatherApiBaseUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tehran,IR";
    static final String forecastWeatherApiBaseUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Tehran,IR";
    static final String weatherApiId = "&APPID=36d8b5c48835b6f93f6656b065affb46";
    TextView tvCurrentLocation;
    TextView tvCurrentTime;
    TextView tvCurrentTemperature;
    TextView tvCurrentWind;
    TextView tvCurrentPressure;
    TextView tvCurrentCloudiness;
    TextView tvCurrentHumidity;
    TextView tvCurrentSunrise;
    TextView tvCurrentSunset;
    LottieAnimationView imgWeatherStatus;
    RecyclerView forecastRecyclerView;
    ForecastAdapter forecastAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vCurrentWeather = inflater.inflate(R.layout.fragment_current_weather, container, Boolean.FALSE);

        tvCurrentLocation = vCurrentWeather.findViewById(R.id.tvCurrentLocation);
        tvCurrentTime = vCurrentWeather.findViewById(R.id.tvCurrentTime);
        tvCurrentTemperature = vCurrentWeather.findViewById(R.id.tvCurrentTemperature);
        tvCurrentWind = vCurrentWeather.findViewById(R.id.tvCurrentWind);
        tvCurrentPressure = vCurrentWeather.findViewById(R.id.tvCurrentPressure);
        tvCurrentCloudiness = vCurrentWeather.findViewById(R.id.tvCurrentCloudiness);
        tvCurrentHumidity = vCurrentWeather.findViewById(R.id.tvCurrentHumidity);
        tvCurrentSunrise = vCurrentWeather.findViewById(R.id.tvCurrentSunrise);
        tvCurrentSunset = vCurrentWeather.findViewById(R.id.tvCurrentSunset);

        imgWeatherStatus = vCurrentWeather.findViewById(R.id.imgWeatherStatus);
        forecastRecyclerView = vCurrentWeather.findViewById(R.id.forecastRecyclerView);

        getCurrentWeatherData();
        getForecastWeatherData();

        return vCurrentWeather;
    }

    private void getCurrentWeatherData() {

        AsyncHttpClient currentWeatherClient = new AsyncHttpClient();
        String currentWeatherUrl = currentWeatherApiBaseUrl + weatherApiId;

        currentWeatherClient.get(currentWeatherUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Gson gson = new Gson();
                CurrentWeatherResponse currentWeatherResponse = gson.fromJson(response.toString(), CurrentWeatherResponse.class);

                tvCurrentLocation.setText(currentWeatherResponse.getName());

                SimpleDateFormat currentDateFormat = new SimpleDateFormat("HH:mm");
                tvCurrentTime.setText(currentDateFormat.format(new Date()));

                DecimalFormat tempFormat = new DecimalFormat(".#");
                String temperature = tempFormat.format(currentWeatherResponse.getMain().getTemp() - 273.15D);
                tvCurrentTemperature.setText(temperature + "°C");

                imgWeatherStatus.setAnimation(getWeatherStatusImage(currentWeatherResponse.getWeather().get(0).getMain()));
//                imgWeatherStatus.animate();
                imgWeatherStatus.loop(true);
                imgWeatherStatus.playAnimation();

                tvCurrentWind.setText(currentWeatherResponse.getWind().getSpeed() + " m/s");
                tvCurrentPressure.setText(currentWeatherResponse.getMain().getPressure() + "hpa");
                tvCurrentCloudiness.setText(currentWeatherResponse.getWeather().get(0).getDescription());
                tvCurrentHumidity.setText(currentWeatherResponse.getMain().getHumidity() + "%");

                SimpleDateFormat sunriseFormat = new SimpleDateFormat("HH:mm");
                String formattedSunrise = sunriseFormat.format(new Date(currentWeatherResponse.getSys().getSunrise() * 1000L));
                tvCurrentSunrise.setText(formattedSunrise);

                SimpleDateFormat sunsetFormat = new SimpleDateFormat("HH:mm");
                String formattedSunset = sunsetFormat.format(new Date(currentWeatherResponse.getSys().getSunset() * 1000L));
                tvCurrentSunset.setText(formattedSunset);



                super.onSuccess(statusCode, headers, response);
            }

        });

    }

    private void getForecastWeatherData() {

        AsyncHttpClient forecastWeatherClient = new AsyncHttpClient();
        String forecastWeatherUrl = forecastWeatherApiBaseUrl + weatherApiId;

        forecastWeatherClient.get(forecastWeatherUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Gson gson = new Gson();
                WeatherResponse weatherResponse = gson.fromJson(response.toString(), WeatherResponse.class);

                forecastAdapter = new ForecastAdapter(weatherResponse.getList());
                forecastRecyclerView.setAdapter(forecastAdapter);
                forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

                super.onSuccess(statusCode, headers, response);
            }

        });
    }

    private int getWeatherStatusImage(String weatherStatus) {

        try {
            return R.raw.class.getField(weatherStatus.toLowerCase()).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return R.raw.clear;
        }

    }
}
