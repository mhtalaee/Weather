package ir.goldenmind.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.goldenmind.weather.R;
import ir.goldenmind.weather.adapters.CityAdapter;
import ir.goldenmind.weather.model.base.City;
import ir.goldenmind.weather.utils.SwipeToDeleteCallback;

public class CitiesFragment extends Fragment implements CityAdapter.ListItemClickListener {

    RecyclerView cityRecycler;
    View vCities;
    private ArrayList<City> userCities;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Hawk.init(getContext()).build();
        vCities = inflater.inflate(R.layout.fragment_cities, container, Boolean.FALSE);
        cityRecycler = vCities.findViewById(R.id.cityRecycler);

        userCities = (Hawk.contains("UserCities")) ? userCities = Hawk.get("UserCities") : new ArrayList<City>();
        CityAdapter cityAdapter = new CityAdapter(userCities, this);
        cityRecycler.setAdapter(cityAdapter);
        cityRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(cityAdapter, getContext()));
        itemTouchHelper.attachToRecyclerView(cityRecycler);

        return vCities;
    }

    @Override
    public void onResume() {
        userCities = (Hawk.contains("UserCities")) ? userCities = Hawk.get("UserCities") : new ArrayList<City>();
        CityAdapter cityAdapter = new CityAdapter(userCities, this);
        cityRecycler.setAdapter(cityAdapter);
        cityRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        super.onResume();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        userCities = (Hawk.contains("UserCities")) ? userCities = Hawk.get("UserCities") : new ArrayList<City>();
        City userCity = userCities.get(clickedItemIndex);
        Hawk.put("SelectedCityName",userCity.getCityName());
        Hawk.put("SelectedCountryCode",userCity.getCountryCode());
        Toast.makeText(getContext(),userCity.getCityName() + " set as default city", Toast.LENGTH_SHORT).show();
    }
}
