package com.example.AirPolutants.controller;
import com.example.AirPolutants.dto.OpenWeather;
import com.example.AirPolutants.dto.OpenWeatherDistrict;
import com.example.AirPolutants.dto.OpenWeatherMaxMin;
import com.example.AirPolutants.model.Weather;
import com.example.AirPolutants.repository.OpenweatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OpenWeatherController {

    @Autowired
    OpenweatherRepository repository;

//    @GetMapping("/weather")
//    public List<OpenWeatherDistrict> showWeather(){
//        List<OpenWeatherDistrict> output = new ArrayList<>();
//        List<String> districts = repository.GetAllDistrict();
//        for(String dist: districts){
//            OpenWeatherDistrict newDistrict = new OpenWeatherDistrict();
//            newDistrict.setDistrict(dist);
//            newDistrict.setOpenWeather(repository.GetWeatherIndicatorsByDistrict(dist));
//            output.add(newDistrict);
//        }
//        return output;
//    }


    @GetMapping("/weather")
    public List<OpenWeatherMaxMin> show(@RequestParam(name = "period", defaultValue = "", required = false) String period,
                                            @RequestParam(name = "district", defaultValue = "", required = false) String district){
        DateTimeFormatter ymd = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ll = repository.GetLastDate();
        //LocalDate ll = LocalDate.parse(lastdate.substring(0, 10), ymd);
        district = "%" + district + "%";

        LocalDate periodDate = ll.minusDays(0);

        switch (period) {
            case "week":
                periodDate = ll.minusWeeks(1);
                break;
            case "month":
                periodDate = ll.minusMonths(1);
                break;
            case "quarter":
                periodDate = ll.minusMonths(3);
                break;
        }


        List<OpenWeatherMaxMin> output = new ArrayList<>();
        if(period == ""){
            output = repository.GetWeatherToday(district,periodDate);

        }else {
            output = repository.GetWeather(district,periodDate);

        }


        return output;
    }

    @GetMapping("/weatherGetAll")
    public List<Weather> getAll(){
        List<Weather> output = new ArrayList<>();
        output = repository.findAll();
        return output;
    }

}
