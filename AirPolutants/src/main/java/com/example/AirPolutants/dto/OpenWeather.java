package com.example.AirPolutants.dto;

public interface OpenWeather {
     String getDistrict();
     String getDatetime();
     String getTemp();
     String getFeels_like();
     String getWind_speed();
     Integer getWind_degree();
     String getPressure();
     String getHumidity();
}
