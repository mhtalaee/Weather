package ir.goldenmind.weather.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.hawk.Hawk;

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
import ir.goldenmind.weather.MainActivity;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.adapters.ForecastAdapter;
import ir.goldenmind.weather.model.openweathermap.current.CurrentWeatherResponse;
import ir.goldenmind.weather.model.openweathermap.forecast.Weather;
import ir.goldenmind.weather.model.openweathermap.forecast.WeatherResponse;

public class CurrentWeatherFragment extends Fragment {

    static final String currentWeatherApiBaseUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
    static final String forecastWeatherApiBaseUrl = "http://api.openweathermap.org/data/2.5/forecast?q=";
    static final String weatherApiId = "&APPID=36d8b5c48835b6f93f6656b065affb46";
    TextView tvCurrentLocation;
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
    String selectedCityName;
    String selectedCountryCode;
    ProgressBar currentWeatherProgressBar;
    LinearLayout layoutCurrent;
    View vCurrentWeather;
    ProgressBar forecastWeatherProgressBar;

    public CurrentWeatherFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vCurrentWeather = inflater.inflate(R.layout.fragment_current_weather, container, Boolean.FALSE);
        Hawk.init(vCurrentWeather.getContext()).build();

        tvCurrentLocation = vCurrentWeather.findViewById(R.id.tvCurrentLocation);
//        tvCurrentTime = vCurrentWeather.findViewById(R.id.tvCurrentTime);
        tvCurrentTemperature = vCurrentWeather.findViewById(R.id.tvCurrentTemperature);
        tvCurrentWind = vCurrentWeather.findViewById(R.id.tvCurrentWind);
        tvCurrentPressure = vCurrentWeather.findViewById(R.id.tvCurrentPressure);
        tvCurrentCloudiness = vCurrentWeather.findViewById(R.id.tvCurrentCloudiness);
        tvCurrentHumidity = vCurrentWeather.findViewById(R.id.tvCurrentHumidity);
        tvCurrentSunrise = vCurrentWeather.findViewById(R.id.tvCurrentSunrise);
        tvCurrentSunset = vCurrentWeather.findViewById(R.id.tvCurrentSunset);

        imgWeatherStatus = vCurrentWeather.findViewById(R.id.imgWeatherStatus);
        forecastRecyclerView = vCurrentWeather.findViewById(R.id.forecastRecyclerView);

        currentWeatherProgressBar = vCurrentWeather.findViewById(R.id.currentWeatherProgressBar);
        layoutCurrent = vCurrentWeather.findViewById(R.id.layoutCurrent);
        forecastWeatherProgressBar = vCurrentWeather.findViewById(R.id.forecastWeatherProgressBar);

        if (Hawk.contains("SelectedCityName")) {
            selectedCityName = Hawk.get("SelectedCityName");
            selectedCountryCode = Hawk.get("SelectedCountryCode");
        } else {
            selectedCityName = vCurrentWeather.getContext().getResources().getString(R.string.default_city);
            selectedCountryCode = vCurrentWeather.getContext().getResources().getString(R.string.default_country_code);
        }

        getCurrentWeatherData(selectedCityName, selectedCountryCode);
        getForecastWeatherData(selectedCityName, selectedCountryCode);

        return vCurrentWeather;
    }

    private void getCurrentWeatherData(String cityName, String countryCode) {

        AsyncHttpClient currentWeatherClient = new AsyncHttpClient();

        String currentWeatherUrl = currentWeatherApiBaseUrl + cityName + "," + countryCode + weatherApiId;

        currentWeatherClient.get(currentWeatherUrl, new JsonHttpResponseHandler() {
            String desc = "";
            String newDesc = "";
            @Override
            public void onStart() {
                currentWeatherProgressBar.setVisibility(ProgressBar.VISIBLE);
                layoutCurrent.setVisibility(View.GONE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                currentWeatherProgressBar.setVisibility(ProgressBar.GONE);
                layoutCurrent.setVisibility(ProgressBar.VISIBLE);
                super.onFinish();
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Gson gson = new Gson();
                CurrentWeatherResponse currentWeatherResponse = gson.fromJson(response.toString(), CurrentWeatherResponse.class);

                tvCurrentLocation.setText(currentWeatherResponse.getName());

                DecimalFormat tempFormat = new DecimalFormat(".#");
                String temperature = tempFormat.format(currentWeatherResponse.getMain().getTemp() - 273.15D);
                tvCurrentTemperature.setText(temperature + "°C");

                imgWeatherStatus.setAnimation(getWeatherStatusImage(currentWeatherResponse.getWeather().get(0).getMain()));
                imgWeatherStatus.loop(true);
                imgWeatherStatus.playAnimation();

                tvCurrentWind.setText(currentWeatherResponse.getWind().getSpeed() + " m/s");
                tvCurrentPressure.setText(currentWeatherResponse.getMain().getPressure() + "hpa");

                desc = currentWeatherResponse.getWeather().get(0).getDescription();
                String[] words = desc.split(" ");

                for (int i = 0; i < words.length; i++) {
                    newDesc += words[i].substring(0, 1).toUpperCase() + words[i].substring(1).concat(" ");
                }

                tvCurrentCloudiness.setText(newDesc.trim());
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

    private void getForecastWeatherData(String cityName, String countryCode) {

        AsyncHttpClient forecastWeatherClient = new AsyncHttpClient();
        String forecastWeatherUrl = forecastWeatherApiBaseUrl + cityName + "," + countryCode + weatherApiId;

        forecastWeatherClient.get(forecastWeatherUrl, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                forecastWeatherProgressBar.setVisibility(ProgressBar.VISIBLE);
                forecastRecyclerView.setVisibility(View.GONE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                forecastWeatherProgressBar.setVisibility(ProgressBar.GONE);
                forecastRecyclerView.setVisibility(View.VISIBLE);
                super.onFinish();
            }

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && vCurrentWeather != null) {
            Hawk.init(vCurrentWeather.getContext()).build();

            if (!Hawk.contains("SelectedCityName")) {
                selectedCityName = vCurrentWeather.getContext().getResources().getString(R.string.default_city);
                selectedCountryCode = vCurrentWeather.getContext().getResources().getString(R.string.default_country_code);
            } else {
                selectedCityName = Hawk.get("SelectedCityName");
                selectedCountryCode = Hawk.get("SelectedCountryCode");
            }

            getCurrentWeatherData(selectedCityName, selectedCountryCode);
            getForecastWeatherData(selectedCityName, selectedCountryCode);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
