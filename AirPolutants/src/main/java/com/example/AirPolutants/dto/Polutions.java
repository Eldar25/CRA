package com.example.AirPolutants.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Polutions {
    String code;
    String min;
    String max;
    String unit;
}
