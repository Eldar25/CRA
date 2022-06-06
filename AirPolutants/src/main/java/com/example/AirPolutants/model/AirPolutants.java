package com.example.AirPolutants.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="station_indicators")
public class AirPolutants {
    @Id
    private String ownid;

    private String datee;

    private String unit;

    private String code;

    private String valuee;

    private String stationid;

    private String city;
}
