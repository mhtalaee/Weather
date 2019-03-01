package ir.goldenmind.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.adapters.ForecastAdapter;
import ir.goldenmind.weather.model.openweathermap.WeatherResponse;

public class CurrentWeatherFragment extends Fragment {


    static final String weatherApiBaseUrl = "http://api.openweathermap.org/data/2.5/forecast?q=tehran";
    static final String weatherApiId = "&APPID=36d8b5c48835b6f93f6656b065affb46";
    TextView tvCurrentLocation;
    TextView tvtvCurrentTime;
    TextView tvCurrentTemperature;
    LottieAnimationView imgWeatherStatus;
    RecyclerView forecastRecyclerView;
    ForecastAdapter forecastAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vCurrentWeather = inflater.inflate(R.layout.fragment_current_weather, container, Boolean.FALSE);

        tvCurrentLocation = vCurrentWeather.findViewById(R.id.tvCurrentLocation);
        tvtvCurrentTime = vCurrentWeather.findViewById(R.id.tvCurrentTime);
        tvCurrentTemperature = vCurrentWeather.findViewById(R.id.tvCurrentTemperature);
        imgWeatherStatus = vCurrentWeather.findViewById(R.id.imgWeatherStatus);
        forecastRecyclerView = vCurrentWeather.findViewById(R.id.forecastRecyclerView);

        AsyncHttpClient client = new AsyncHttpClient();

        String url = weatherApiBaseUrl + weatherApiId;
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Gson gson = new Gson();
                WeatherResponse weatherResponse = gson.fromJson(response.toString(), WeatherResponse.class);

                tvCurrentLocation.setText(weatherResponse.getCity().getName());

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String formattedDate = sdf.format(new Date());

                tvtvCurrentTime.setText(formattedDate);
                tvCurrentTemperature.setText(String.valueOf(weatherResponse.getList().get(0).getMain().getTemp() - 273.15D) + "°C");
//                imgWeatherStatus.setAnimation(getWeatherStatusImage(weatherResponse.getList().get(0).getWeather().get(0));
//                imgWeatherStatus.animate();
                imgWeatherStatus.loop(true);
                imgWeatherStatus.playAnimation();

                forecastAdapter = new ForecastAdapter(weatherResponse.getList());
                forecastRecyclerView.setAdapter(forecastAdapter);
                forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

                super.onSuccess(statusCode, headers, response);
            }

        });


        return vCurrentWeather;

    }

    private int getWeatherStatusImage(String weatherStatus) {

        try {
            return R.raw.class.getField(weatherStatus).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return R.raw.sun;
        }

//        switch (weatherStatus.toLowerCase()) {
//            case "thunderstorm":
//                return R.raw.sun;
//            case "drizzle":
//                return R.raw.sun;
//            case "rain":
//                return R.raw.sun;
//            case "snow":
//                return R.raw.sun;
//            case "atmosphere":
//                return R.raw.sun;
//            case "clear":
//                return R.raw.sun;
//            case "clouds":
//                return R.raw.sun;
//            default:
//                return 1;
//        }
    }
}
