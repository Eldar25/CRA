package com.example.AirPolutants.dto;

import java.util.Date;

public interface OpenWeatherMaxMin {
    String getDistrict();
    String getDate();
    String getMax_temp();
    String getMin_temp();
    String getMax_feels();
    String getMin_feels();
    String getMax_speed();
    String getMin_speed();
    Integer getMax_degree();
    Integer getMin_degree();
    String getMax_pressure();
    String getMin_pressure();
    String getMax_humidity();
    String getMin_humidity();

}
