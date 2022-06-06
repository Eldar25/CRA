package com.example.AirPolutants.repository;

import com.example.AirPolutants.dto.OpenWeather;
import com.example.AirPolutants.dto.OpenWeatherDistrict;
import com.example.AirPolutants.dto.OpenWeatherMaxMin;
import com.example.AirPolutants.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;



public interface OpenweatherRepository extends JpaRepository<Weather,Long> {
    //@Query(value = "select * from openweather;", nativeQuery = true)
//    @Query(value = "select datetime,temp,feels_like,wind_speed,wind_degree,pressure,humidity" +
//            " from openweather where district = :district order by datetime desc LIMIT 1;", nativeQuery = true)
//    List<OpenWeather> GetWeatherIndicatorsByDistrict(@Param("district") String district);

    @Query(value = "select district,datetime ,max(temp) as max_temp, min(temp) as min_temp, max(feels_like) as max_feels,min(feels_like) as min_feels,\n" +
            "       max(wind_speed) as max_speed,min(wind_speed) as min_speed,max(wind_degree) as max_degree,min(wind_degree) as min_degree,\n" +
            "       max(pressure) as max_pressure,min(pressure) as min_pressure,max(humidity) as max_humidity,min(humidity) as min_humidity\n" +
            "from openweather\n" +
            "where to_date(substr(datetime,1,10),'DD-MM-YYYY') = :periodDate and district like :district\n" +
            "group by  district, datetime\n" +
            "order by datetime desc LIMIT 8;", nativeQuery = true)
    List<OpenWeatherMaxMin> GetWeatherToday(@Param("district") String district,
                                      @Param("periodDate") LocalDate periodDate);


    @Query(value = "select district, to_date(substr(datetime,1,10),'DD-MM-YYYY') as date,\n" +
            "       max(temp) as max_temp, min(temp) as min_temp, max(feels_like) as max_feels,min(feels_like) as min_feels,\n" +
            "       max(wind_speed) as max_speed,min(wind_speed) as min_speed,max(wind_degree) as max_degree,min(wind_degree) as min_degree,\n" +
            "       max(pressure) as max_pressure,min(pressure) as min_pressure,max(humidity) as max_humidity,min(humidity) as min_humidity\n" +
            "from openweather\n" +
            "where to_date(substr(datetime,1,10),'DD-MM-YYYY')  >= :periodDate and district like :district\n" +
            "group by  district, date\n" +
            "order by district, date;", nativeQuery = true)
    List<OpenWeatherMaxMin> GetWeather(@Param("district") String district,
                                       @Param("periodDate") LocalDate periodDate);

    @Query(value = "select max(to_date(substr(datetime,1,10),'DD-MM-YYYY')) from openweather", nativeQuery = true )
    LocalDate GetLastDate();

//    @Query(value = "select distinct district from openweather;", nativeQuery = true)
//    List<String> GetAllDistrict();
}
