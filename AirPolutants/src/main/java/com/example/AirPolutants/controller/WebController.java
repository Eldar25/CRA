package com.example.AirPolutants.controller;

import com.example.AirPolutants.dto.Polutions;
import com.example.AirPolutants.dto.Polution;
import com.example.AirPolutants.dto.DistrictDateAndPolutions;
import com.example.AirPolutants.repository.PollutantsRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebController {

    @Autowired
    PollutantsRepository repository;

   @GetMapping("/indicators")
    public List<DistrictDateAndPolutions> showStations(@RequestParam(name = "code", defaultValue = "", required = false) String code,
                                                       @RequestParam(name = "period", defaultValue = "", required = false) String period,
                                                       @RequestParam(name = "district", defaultValue = "", required = false) String district){


       DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       List<DistrictDateAndPolutions> ttt = new ArrayList<>();
       String lastdate = repository.GetLastDate();
       LocalDate ll = LocalDate.parse(lastdate.substring(0, 10), ymd);
       List<Polutions> comples = new ArrayList<>();
       String lastDateTime = repository.GetLastDateTime();



       //       List<String> districts = repository.GetAllDistrict();
//       LocalDate yesterday = ll.minusDays(1);
//       LocalDate week = ll.minusWeeks(1);
//       LocalDate month = ll.minusMonths(1);
//       LocalDate quartal = ll.minusMonths(3);



       List<DistrictDateAndPolutions> output = new ArrayList<>();

       code = "%" + code + "%";
       district = "%" + district + "%";

       LocalDate periodDate = ll.minusDays(1);

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
       List<Polution> tests = new ArrayList<>();
       if(period == ""){
            tests = repository.getDataToday(code, district, lastDateTime);

       }else {
            tests = repository.getNeedsData(code, district, periodDate);

       }

       for (int i = 0; i < tests.size(); i++) {

           String dist = tests.get(i).getDistrict();
           String date = tests.get(i).getDate();

           comples.add(new Polutions(tests.get(i).getCode(), tests.get(i).getMin(), tests.get(i).getMax(), tests.get(i).getUnit()));

           if (i == tests.size() - 1) {
               output.add(new DistrictDateAndPolutions(dist, date, comples));
           } else if (!dist.equals(tests.get(i + 1).getDistrict()) ||
                   !date.equals(tests.get(i + 1).getDate())) {
               output.add(new DistrictDateAndPolutions(dist, date, comples));
               comples = new ArrayList<>();
           }

       }

       return output;



       //List<Polution> tests = repository.getNeedsData(ymd.format(ll), code, district);
       //List<Polution> tests = repository.getNeedsData(ymd.format(ll));
       //List<Polution> tests = repository.getNeedsData(ymd.format(ll), code);
       //List<Polution> tests = repository.getNeedsData(ymd.format(ll), district);
       //List<Polution> tests = repository.getNeedsData();


   }





}
