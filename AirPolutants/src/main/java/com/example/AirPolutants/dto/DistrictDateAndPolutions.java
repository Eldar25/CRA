package com.example.AirPolutants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DistrictDateAndPolutions {
    String district;
    String date;
    List<Polutions> compless;
}
