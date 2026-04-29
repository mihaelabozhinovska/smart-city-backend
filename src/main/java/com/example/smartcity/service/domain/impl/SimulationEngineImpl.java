package com.example.smartcity.service.domain.impl;

import com.example.smartcity.model.dto.create.SimulateRequestDto;
import com.example.smartcity.model.dto.display.DisplaySimulationResultDto;
import com.example.smartcity.service.domain.CityDataService;
import com.example.smartcity.service.domain.SimulationEngine;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SimulationEngineImpl implements SimulationEngine {

    private final CityDataService cityDataService;

    public SimulationEngineImpl(CityDataService cityDataService) {
        this.cityDataService = cityDataService;
    }

    @Override
    public DisplaySimulationResultDto simulate(SimulateRequestDto request) {
        return switch (request.getType().toLowerCase()) {
            case "traffic"     -> simulateTraffic(request);
            case "temperature" -> simulateTemperature(request);
            case "pollution"   -> simulatePollution(request);
            default -> throw new IllegalArgumentException(
                    "Unknown simulation type: " + request.getType()
            );
        };
    }

    private DisplaySimulationResultDto simulateTraffic(SimulateRequestDto request) {
        Map<String, Double> data = cityDataService.getTrafficData();

        double traffic = data.get("vehicleCount")
                * (request.getTrafficMultiplier() != null ? request.getTrafficMultiplier() : 1.0);

        if (Boolean.TRUE.equals(request.getRoadClosure())) {
            traffic += 50;
        }

        String trafficRisk = traffic > 150 ? "HIGH" : "LOW";

        DisplaySimulationResultDto dto = new DisplaySimulationResultDto();
        dto.setPredictedTraffic(traffic);
        dto.setTrafficRiskLevel(trafficRisk);
        dto.setOverallRiskLevel(trafficRisk);
        dto.setGeneratedAt(LocalDateTime.now());
        return dto;
    }

    private DisplaySimulationResultDto simulateTemperature(SimulateRequestDto request) {
        Map<String, Double> data = cityDataService.getTemperatureData();

        double temp = data.get("baseTemperature")
                + (request.getTemperatureDelta() != null ? request.getTemperatureDelta() : 0.0);

        String heatRisk = temp > 35 ? "HIGH" : "LOW";

        DisplaySimulationResultDto dto = new DisplaySimulationResultDto();
        dto.setPredictedTemperature(temp);
        dto.setHeatRiskLevel(heatRisk);
        dto.setOverallRiskLevel(heatRisk);
        dto.setGeneratedAt(LocalDateTime.now());
        return dto;
    }

    private DisplaySimulationResultDto simulatePollution(SimulateRequestDto request) {
        Map<String, Double> data = cityDataService.getPollutionData();

        double pollution = data.get("basePollution")
                * (request.getPollutionMultiplier() != null ? request.getPollutionMultiplier() : 1.0);

        String airRisk = pollution > 120 ? "HIGH" : "LOW";

        DisplaySimulationResultDto dto = new DisplaySimulationResultDto();
        dto.setPredictedPollution(pollution);
        dto.setAirRiskLevel(airRisk);
        dto.setOverallRiskLevel(airRisk);
        dto.setGeneratedAt(LocalDateTime.now());
        return dto;
    }
}