package ir.goldenmind.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        DecimalFormat df2 = new DecimalFormat(".##");
        String temperature = df2.format(weatherForecastList.get(position).getMain().getTemp() - 273.15D);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd");
        String formattedTime = timeFormat.format(new Date(weatherForecastList.get(position).getDt() * 1000L));
        String formattedDate = dateFormat.format(new Date(weatherForecastList.get(position).getDt() * 1000L));
        holder.tvForecastTemperature.setText(temperature + "°C");
        holder.tvForecastTime.setText(formattedTime);
        holder.tvForecastDate.setText(formattedDate);
        holder.tvUnix.setText(weatherForecastList.get(position).getDt().toString() + "/" +
                weatherForecastList.get(position).getMain().getTemp());
        Picasso.get().load(weatherIconBaseUrl + ((Weather) weatherForecastList.get(position).getWeather().get(0)).getIcon() + ".png").into(holder.imgWeatherIcon);
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView tvForecastTemperature;
        TextView tvForecastDate;
        TextView tvForecastTime;
        TextView tvUnix;
        ImageView imgWeatherIcon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvForecastTemperature = itemView.findViewById(R.id.tvForecastTemperature);
            tvForecastDate = itemView.findViewById(R.id.tvForecastDate);
            tvForecastTime = itemView.findViewById(R.id.tvForecastTime);
            tvUnix = itemView.findViewById(R.id.tvUnix);
            imgWeatherIcon = itemView.findViewById((R.id.imgWeatherIcon));
        }
    }

}
