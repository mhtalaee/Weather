package ir.goldenmind.weather;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ir.goldenmind.weather.adapters.MainPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    ViewPager mainPager;
    MainPagerAdapter mainPagerAdapter;
    SmartTabLayout smartTabLayout;
    Button btnAddCity;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddCity = findViewById(R.id.btnAddCity);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mainPager = findViewById(R.id.mainPager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mainPager.setAdapter(mainPagerAdapter);

        smartTabLayout = findViewById(R.id.smartTabLayout);
        smartTabLayout.setViewPager(mainPager);

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddCity =  new Intent(MainActivity.this, AddCityActivity.class);
                startActivity(intentToAddCity);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
