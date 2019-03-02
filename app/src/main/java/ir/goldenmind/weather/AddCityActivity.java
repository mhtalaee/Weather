package ir.goldenmind.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        AutoCompleteTextView etCountry;
        etCountry = findViewById(R.id.etCountry);

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

        String name;
        JSONObject searchObject;

        JSONObject array = null;
        try {
            array = new JSONObject(jsonString);
            ArrayList countryList = new ArrayList();
            Iterator<String> keys = array.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if (array.get(key) instanceof JSONObject) {
                    // do something with jsonObject here
                    JSONObject arr1 = (JSONObject) array.get(key);
                    countryList.add((new JSONObject(arr1.toString())).get("name"));
                    int i = 0;
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countryList);
            etCountry.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
