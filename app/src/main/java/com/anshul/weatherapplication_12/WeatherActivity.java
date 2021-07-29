package com.anshul.weatherapplication_12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView cityWeather, TemperatureWeather,WeatherConditionsWeather,HumidityWeather,maxTempWeather,minTempWeather,pressureWeather,windWeather;
    private ImageView imageViewWeather;
    private Button search;
    private EditText edtText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityWeather=findViewById(R.id.textCityViewWeather);
        TemperatureWeather=findViewById(R.id.textViewTempWeather);
        WeatherConditionsWeather=findViewById(R.id.textViewWeatherConditionWeather);
        HumidityWeather=findViewById(R.id.textViewHumidityWeather);
        maxTempWeather=findViewById(R.id.textViewMaxTempWeather);
        minTempWeather=findViewById(R.id.textViewMinTempWeather);
        pressureWeather=findViewById(R.id.textViewPressureWeather);
        windWeather=findViewById(R.id.textViewWindWeather);
        imageViewWeather=findViewById(R.id.imageViewWeather);
        search=findViewById(R.id.search);
        edtText=findViewById(R.id.editTextCityName);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName=edtText.getText().toString();
                getWeatherData(cityName);
                edtText.setText(cityName);

            }
        });
    }
    public void getWeatherData(String str){
        WeatherApi weatherApi=RetrofitWeather.getClient().create(WeatherApi.class);
        Call<OpenWeatherMap> call=weatherApi.getWeatherWithCityName(str);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful()){
                    cityWeather.setText(response.body().getName()+" , "+response.body().getSys().getCountry());
                    TemperatureWeather.setText(response.body().getMain().getTemp()+" °C");
                    WeatherConditionsWeather.setText(response.body().getWeather().get(0).getDescription());
                    HumidityWeather.setText(" : "+response.body().getMain().getHumidity()+"%");
                    maxTempWeather.setText(" : "+response.body().getMain().getTempMax()+" °C");
                    minTempWeather.setText(" : "+response.body().getMain().getTempMin()+" °C");
                    pressureWeather.setText(" : "+response.body().getMain().getPressure());
                    pressureWeather.setText(" : "+response.body().getWind().getSpeed());

                    String iconCode=response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/"+iconCode+"@2x.png").placeholder(R.drawable.ic_launcher_background).into(imageViewWeather);
                }
                else {
                    Toast.makeText(WeatherActivity.this, "City Not Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("TAG","Anshul");
            }
        });
    }
}