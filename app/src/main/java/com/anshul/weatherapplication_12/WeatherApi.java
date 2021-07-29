package com.anshul.weatherapplication_12;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather?appid=77edec90919292b112296145d093d170&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);


    @GET("weather?&appid=77edec90919292b112296145d093d170")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String name);
}
