package ir.goldenmind.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ir.goldenmind.weather.adapters.MainPagerAdapter;

import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    ViewPager mainPager;
    MainPagerAdapter mainPagerAdapter;
    SmartTabLayout smartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPager = findViewById(R.id.mainPager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mainPager.setAdapter(mainPagerAdapter);

        smartTabLayout = findViewById(R.id.smartTabLayout);
        smartTabLayout.setViewPager(mainPager);

    }
}
