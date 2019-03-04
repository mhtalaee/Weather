package ir.goldenmind.weather.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import ir.goldenmind.weather.fragments.CitiesFragment;
import ir.goldenmind.weather.fragments.CurrentWeatherFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public MainPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new CurrentWeatherFragment(context);
            case 1:
                return new CitiesFragment();
            default:
                return new CurrentWeatherFragment(context);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Current Weather";
            case 1:
                return "Favorite Cities";
            default:
                return null;
        }
    }
}
