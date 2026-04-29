package com.example.smartcity.model.dto.create;

import lombok.Data;

@Data
public class SimulateRequestDto {

    private String type;

    private Double temperatureDelta;
    private Double pollutionMultiplier;
    private Double trafficMultiplier;
    private Boolean roadClosure;

    private Long locationId;
}