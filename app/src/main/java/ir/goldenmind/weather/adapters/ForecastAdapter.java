package ir.goldenmind.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.model.openweathermap.forecast.List;
import ir.goldenmind.weather.model.openweathermap.forecast.Weather;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    java.util.List<List> weatherForecastList;
    String weatherIconBaseUrl = "http://openweathermap.org/img/w/";

    public ForecastAdapter(java.util.List<List> weatherForecastList) {
        this.weatherForecastList = weatherForecastList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_recyclerview_item, parent, Boolean.FALSE);
        ForecastViewHolder forecastViewHolder = new ForecastViewHolder(itemView);
        return forecastViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

        List<L> forcastRecord = weatherForecastList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        String formattedDate = dateFormat.format(new Date(forcastRecord.getDt() * 1000L));
        holder.tvForecastDate.setText(formattedDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String formattedTime = timeFormat.format(new Date(forcastRecord.getDt() * 1000L));
        holder.tvForecastTime.setText(formattedTime);

        Picasso.get().load(weatherIconBaseUrl + ((Weather) forcastRecord.getWeather().get(0)).getIcon() + ".png").resize(50, 50).centerCrop().into(holder.imgWeatherIcon);

        DecimalFormat tempFormat = new DecimalFormat(".#");
        String temperature = tempFormat.format(forcastRecord.getMain().getTemp() - 273.15D);
        holder.tvForecastTemperature.setText(temperature + " °C");

        holder.tvForecastWind.setText(forcastRecord.getWind().getSpeed().toString() + " m/s");
        holder.tvForecastPressure.setText(forcastRecord.getMain().getPressure().toString());

    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView tvForecastDate;
        TextView tvForecastTime;
        ImageView imgWeatherIcon;
        TextView tvForecastTemperature;
        TextView tvForecastWind;
        TextView tvForecastPressure;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            tvForecastDate = itemView.findViewById(R.id.tvForecastDate);
            tvForecastTime = itemView.findViewById(R.id.tvForecastTime);
            imgWeatherIcon = itemView.findViewById((R.id.imgWeatherIcon));
            tvForecastTemperature = itemView.findViewById(R.id.tvForecastTemperature);
            tvForecastWind = itemView.findViewById(R.id.tvForecastWind);
            tvForecastPressure = itemView.findViewById(R.id.tvForecastPressure);
        }
    }

}
