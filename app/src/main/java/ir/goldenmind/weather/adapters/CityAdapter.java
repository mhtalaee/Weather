package ir.goldenmind.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.model.base.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    List<City> cityList;

    public CityAdapter(List<City> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_recyclerview_item, parent, Boolean.FALSE);
        CityViewHolder cityViewHolder = new CityViewHolder(itemView);
        return cityViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.tvCountryName.setText(cityList.get(position).getCountryName() + " (" + cityList.get(position).getCountryCode() + ")");
        holder.tvCityName.setText(cityList.get(position).getCityName());
        Picasso.get().load("https://www.countryflags.io/" + cityList.get(position).getCountryCode() + "/shiny/64.png").into(holder.imgFlag);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void removeItem(int position) {
        cityList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(City item, int position) {
        cityList.add(position, item);
        notifyItemInserted(position);
    }

    public List<City> getData() {
        return cityList;
    }

    class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCountryName;
        TextView tvCityName;
        ImageView imgFlag;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            City userCity = cityList.get(getAdapterPosition());
            Hawk.put("SelectedCityName", userCity.getCityName());
            Hawk.put("SelectedCountryCode", userCity.getCountryCode());
            Toast.makeText(v.getContext(), userCity.getCityName() + " set as default city", Toast.LENGTH_SHORT).show();
        }
    }
}
