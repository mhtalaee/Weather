package ir.goldenmind.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.model.base.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    List<City> cityList;
    final private ListItemClickListener mOnClickListener;

    public CityAdapter(List<City> cityList, ListItemClickListener listener) {
        this.cityList = cityList;
        this.mOnClickListener = listener;

    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
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

    public void deleteItem(int position) {
        cityList.remove(position);
        Hawk.put("UserCities", cityList);
        notifyItemRemoved(position);
    }

    class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
