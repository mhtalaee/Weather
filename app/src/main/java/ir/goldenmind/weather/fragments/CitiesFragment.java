package ir.goldenmind.weather.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
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

public class CitiesFragment extends Fragment {

    RecyclerView cityRecycler;
    View vCities;
    CityAdapter cityAdapter;
    private ArrayList<City> userCities;

    LinearLayout layoutForRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Hawk.init(getContext()).build();
        vCities = inflater.inflate(R.layout.fragment_cities, container, Boolean.FALSE);
        cityRecycler = vCities.findViewById(R.id.cityRecycler);
        layoutForRecycler = vCities.findViewById(R.id.layoutForRecycler);

        userCities = (Hawk.contains("UserCities")) ? userCities = Hawk.get("UserCities") : new ArrayList<City>();
        cityAdapter = new CityAdapter(userCities);
        cityRecycler.setAdapter(cityAdapter);
        cityRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        enableSwipeToDeleteAndUndo();

        return vCities;
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final City item = cityAdapter.getData().get(position);

                cityAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(layoutForRecycler, "City was removed", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cityAdapter.restoreItem(item, position);
                        cityRecycler.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(cityRecycler);
    }

    @Override
    public void onResume() {
        userCities = (Hawk.contains("UserCities")) ? userCities = Hawk.get("UserCities") : new ArrayList<City>();
        CityAdapter cityAdapter = new CityAdapter(userCities);
        cityRecycler.setAdapter(cityAdapter);
        cityRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        super.onResume();
    }

}
