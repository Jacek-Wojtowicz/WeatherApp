package com.jw.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView temperature;
    private TextView SetCityName;
    private TextView WeatherTextView;
    private TextView HumidityTextView;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            city = bundle.getString("City");
        }

        SetCityName = findViewById(R.id.CityNameTextView);
        temperature = findViewById(R.id.TemperatureTextView);
        WeatherTextView = findViewById(R.id.WeatherTextView);
        HumidityTextView = findViewById(R.id.HumidityTextView);

        OkHttpClient client = new OkHttpClient();
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=bdc9b8c9eb6631c41e0b9c6f973bec35&units=Metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    WeatherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);

                                JSONObject sys = json.getJSONObject("sys");
                                JSONObject main = json.getJSONObject("main");
                                JSONArray weather_array = json.getJSONArray("weather");

                                //weather holds icon also
                                //TODO add icon
                                JSONObject weather = weather_array.getJSONObject(0);
                                String description = weather.getString("description");
                                Double temp = main.getDouble("temp");

                                SetCityName.setText(city+", "+sys.getString("country"));
                                temperature.setText(temp.toString());
                                WeatherTextView.setText(description);
                                HumidityTextView.setText(String.valueOf(main.getInt("humidity"))+"%");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}