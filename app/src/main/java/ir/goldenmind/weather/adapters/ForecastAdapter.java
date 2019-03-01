package ir.goldenmind.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.model.openweathermap.List;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    java.util.List<List> weatherForecastList;

    public ForecastAdapter(java.util.List<ir.goldenmind.weather.model.openweathermap.List> weatherForecastList) {
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
//        holder.tvForecastTemperature.setText(String.valueOf(weatherForecastList.get(position).getMain().getTemp() - 273.15D) + "°C");
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView tvForecastTemperature;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvForecastTemperature = itemView.findViewById(R.id.tvForecastTemperature);

        }
    }

}
