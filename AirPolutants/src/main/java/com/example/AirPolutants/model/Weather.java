package com.example.AirPolutants.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="openweather")
public class Weather {
    @Id
    private int id;
    private String district;
    private String datetime;
    private String temp;
    private String feels_like;
    private String wind_speed;
    private Integer wind_degree;
    private String pressure;
    private String humidity;
}
