package com.example.AirPolutants.repository;
import com.example.AirPolutants.dto.Polution;
import com.example.AirPolutants.model.AirPolutants;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface PollutantsRepository extends JpaRepository<AirPolutants,Long> {
//    @Query(value = "select si.code , min(valuee), max(valuee), si.unit from station_indicators si left join public.stations s on si.stationid = s.stationid" +
//            " where substr(si.datee, 1, 10) = :date and s.district = :district group by  s.district, si.code, si.unit;",nativeQuery = true)
//    List<Polution> findAirPolutantsByDatee(@Param("date") String date,@Param("district") String district);

    //@Query(value = "select s.district, substr(si.datee, 1, 10) as date, si.code, min(valuee), max(valuee), si.unit from station_indicators si left join public.stations s on si.stationid = s.stationid where si.datee >= '2022-05-10' group by s.district, si.code, si.unit,substr(si.datee, 1, 10);", nativeQuery = true)
    //List<Test> getNeedsData();


    @Query(value = INDICATORS_QUERY, nativeQuery = true)
    List<Polution> getNeedsData(@Param("code") String code,
                                @Param("district") String district,
                                @Param("periodDate") LocalDate periodDate);

    String INDICATORS_QUERY = "select s.district, substring(si.datee, 1, 10) as date, si.code, min(valuee), max(valuee), si.unit\n" +
            "from station_indicators si\n" +
            "         right join public.stations s on si.stationid = s.stationid\n" +
            "where substr(si.datee, 1, 10) > :periodDate\n" +
            "  and s.district like :district\n" +
            "  and si.code like :code\n" +
            "group by s.district, substring(si.datee, 1, 10), si.code, si.unit\n" +
            "order by s.district, date;";
//    @Query(value = "select s.district, substring(si.datee, 1, 10) as date, si.code, min(valuee), max(valuee), si.unit\n" +
//            "from station_indicators si\n" +
//            "         right join public.stations s on si.stationid = s.stationid\n" +
//            "where substr(si.datee, 1, 10) >= '2022-03-10' and si.code like '%%' and s.district like '%%'\n" +
//            "group by s.district, substring(si.datee, 1, 10), si.code, si.unit\n" +
//            "order by s.district, date;", nativeQuery = true)
//    List<Polution> getNeedsData();


//    @Query(value = "select distinct district from stations;", nativeQuery = true)
//    List<String> GetAllDistrict();

//    @Query(value = "select substring(si.datee, 1, 10) dd, si.code, min(valuee), max(valuee), si.unit\n" +
//            "from station_indicators si\n" +
//            "         left join public.stations s on si.stationid = s.stationid\n" +
//            "where substr(si.datee, 1, 10) >= :date\n" +
//            "  and si.code like :code\n" +
//            "  and s.district like :district\n" +
//            "group by s.district, si.code, si.unit, substring(si.datee, 1, 10)\n" +
//            "order by dd, s.district;",nativeQuery = true)
//    List<String> getAirPolutantsByDateeAndCodeAndDistrict(@Param("date") String date,@Param("district") String district,@Param("code")String code);

    @Query(value = "select max(datee) from station_indicators", nativeQuery = true )
    String GetLastDate();

    @Query(value = "select datee from station_indicators order by datee desc limit 1;", nativeQuery = true )
    String GetLastDateTime();

    @Query(value = "select s.district, substring(si.datee, 1, 10) dd, si.code, min(valuee), max(valuee), si.unit\n" +
            "from station_indicators si\n" +
            "         right join public.stations s on si.stationid = s.stationid\n" +
            "where si.datee >= :periodDate\n" +
            "  and si.code = :code\n" +
            "  and s.district = :district\n" +
            "group by s.district, si.code, si.unit, substring(si.datee, 1, 10)\n" +
            "order by dd, s.district;", nativeQuery = true)
    List<Polution> getDataToday(@Param("code") String code,
                                @Param("district") String district,
                                @Param("periodDate") String periodDate);


}

    
