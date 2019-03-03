package ir.goldenmind.weather;

import androidx.appcompat.app.AppCompatActivity;
import ir.goldenmind.weather.model.base.City;
import ir.goldenmind.weather.model.openweathermap.forecast.Weather;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class AddCityActivity extends AppCompatActivity {
    AutoCompleteTextView etCountry;
    AutoCompleteTextView etCity;
    JSONObject countries = null;
    ImageView imgFlag;
    Button btnAdd;
    String cityCode;
    String countryCode;
    private ArrayList<City> userCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        etCountry = findViewById(R.id.etCountry);
        etCity = findViewById(R.id.etCity);
        imgFlag = findViewById(R.id.imgFlag);
        btnAdd = findViewById(R.id.btnAdd);

        Hawk.init(AddCityActivity.this).build();

        InputStream is = getResources().openRawResource(R.raw.zones);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        try {
            countries = new JSONObject(jsonString);
            ArrayList countryList = new ArrayList();
            Iterator<String> keys = countries.keys();

            while (keys.hasNext()) {
                String key = keys.next();
//                if (countries.get(key) instanceof JSONObject) {
                // do something with jsonObject here
                JSONObject country = (JSONObject) countries.get(key);
                countryList.add((new JSONObject(country.toString())).get("name"));
                int i = 0;
//                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countryList);
            etCountry.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        etCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Iterator<String> keys = countries.keys();
                while (keys.hasNext()) {
                    cityCode = keys.next();

                    try {
                        JSONObject country = (JSONObject) countries.get(cityCode);
                        if (country.get("name").equals(etCountry.getText().toString())) {
                            Picasso.get().load("https://www.countryflags.io/" + cityCode + "/shiny/64.png").into(imgFlag);
                            countryCode = cityCode;
                            Iterator<String> cities = (new JSONObject(country.get("divisions").toString())).keys();
                            ArrayList cityList = new ArrayList();
                            while (cities.hasNext()) {
                                String ck = cities.next();

                                String cityName = (new JSONObject(country.get("divisions").toString())).get(ck).toString();
                                cityList.add(cityName);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cityList);
                            etCity.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCities = (Hawk.contains("UserCities")) ? userCities = Hawk.get("UserCities") : new ArrayList<City>();
                    City userCity = new City(countryCode, etCountry.getText().toString(), etCity.getText().toString());
                    userCities.add(userCity);
                    Hawk.put("UserCities", userCities);
                    finish();
            }
        });

    }
}
