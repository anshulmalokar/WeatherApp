package com.anshul.weatherapplication_12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private TextView city, Temperature,WeatherConditions,Humidity,maxTemp,minTemp,pressure,wind;
    private ImageView imageView;
    private FloatingActionButton fab;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        city=findViewById(R.id.textCityView);
        Temperature=findViewById(R.id.textViewTemp);
        WeatherConditions=findViewById(R.id.textViewWeatherCondition);
        Humidity=findViewById(R.id.textViewHumidity);
        maxTemp=findViewById(R.id.textViewMaxTemp);
        minTemp=findViewById(R.id.textViewMinTemp);
        pressure=findViewById(R.id.textViewPressure);
        wind=findViewById(R.id.textViewWind);
        imageView=findViewById(R.id.imageView);
        fab=findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,WeatherActivity.class);
                startActivity(intent);
            }
        });

        locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat=location.getLatitude();
                lon=location.getLongitude();
                Log.d("Tag lat",String.valueOf(lat));
                Log.d("Tag lon",String.valueOf(lon));
                getWeatherData(lat,lon);
            }

        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && permissions.length>0 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }
    public void getWeatherData(double lat,double lon){
       WeatherApi weatherApi=RetrofitWeather.getClient().create(WeatherApi.class);
       Call<OpenWeatherMap>call=weatherApi.getWeatherWithLocation(lat, lon);
       call.enqueue(new Callback<OpenWeatherMap>() {
           @Override
           public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
               city.setText(response.body().getName()+" , "+response.body().getSys().getCountry());
               Temperature.setText(response.body().getMain().getTemp()+" °C");
               WeatherConditions.setText(response.body().getWeather().get(0).getDescription());
               Humidity.setText(" : "+response.body().getMain().getHumidity()+"%");
               maxTemp.setText(" : "+response.body().getMain().getTempMax()+" °C");
               minTemp.setText(" : "+response.body().getMain().getTempMin()+" °C");
               pressure.setText(" : "+response.body().getMain().getPressure());
               pressure.setText(" : "+response.body().getWind().getSpeed());

               String iconCode=response.body().getWeather().get(0).getIcon();
               Picasso.get().load("https://openweathermap.org/img/wn/"+iconCode+"@2x.png").placeholder(R.drawable.ic_launcher_background).into(imageView);

           }

           @Override
           public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("TAG","Anshul");
           }
       });
    }
}



