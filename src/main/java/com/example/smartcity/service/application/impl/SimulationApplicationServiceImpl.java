package com.example.smartcity.service.application.impl;

import com.example.smartcity.model.domain.Location;
import com.example.smartcity.model.domain.Scenario;
import com.example.smartcity.model.domain.SimulationResult;
import com.example.smartcity.model.dto.DisplaySimulationResultDto;
import com.example.smartcity.service.application.SimulationApplicationService;
import com.example.smartcity.service.domain.LocationService;
import com.example.smartcity.service.domain.ScenarioService;
import com.example.smartcity.service.domain.SimulationResultService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationApplicationServiceImpl implements SimulationApplicationService {

    private final ScenarioService scenarioService;
    private final LocationService locationService;
    private final SimulationResultService simulationResultService;

    public SimulationApplicationServiceImpl(
            ScenarioService scenarioService,
            LocationService locationService,
            SimulationResultService simulationResultService
    ) {
        this.scenarioService = scenarioService;
        this.locationService = locationService;
        this.simulationResultService = simulationResultService;
    }

    @Override
    public DisplaySimulationResultDto runSimulation(Long scenarioId) {

        Scenario scenario = scenarioService.findById(scenarioId)
                .orElseThrow(() -> new RuntimeException("Scenario not found"));

        Location location = scenario.getLocation();

        // 🔥 MOCK LOGIC (најважно)
        double baseTemp = 25;
        double basePollution = 80;
        double baseTraffic = 100;

        double predictedTemp = baseTemp + (scenario.getTemperatureDelta() != null ? scenario.getTemperatureDelta() : 0);
        double predictedPollution = basePollution * (scenario.getPollutionMultiplier() != null ? scenario.getPollutionMultiplier() : 1);
        double predictedTraffic = baseTraffic * (scenario.getTrafficMultiplier() != null ? scenario.getTrafficMultiplier() : 1);

        if (Boolean.TRUE.equals(scenario.getRoadClosure())) {
            predictedTraffic += 50;
        }

        String heatRisk = predictedTemp > 35 ? "HIGH" : "LOW";
        String airRisk = predictedPollution > 120 ? "HIGH" : "LOW";
        String trafficRisk = predictedTraffic > 150 ? "HIGH" : "LOW";

        String overall = (heatRisk.equals("HIGH") || airRisk.equals("HIGH") || trafficRisk.equals("HIGH"))
                ? "HIGH"
                : "LOW";

        SimulationResult result = new SimulationResult();
        result.setPredictedTemperature(predictedTemp);
        result.setPredictedPollution(predictedPollution);
        result.setPredictedTraffic(predictedTraffic);
        result.setHeatRiskLevel(heatRisk);
        result.setAirRiskLevel(airRisk);
        result.setTrafficRiskLevel(trafficRisk);
        result.setOverallRiskLevel(overall);
        result.setGeneratedAt(LocalDateTime.now());
        result.setScenario(scenario);
        result.setLocation(location);

        return toDto(simulationResultService.save(result));
    }

    @Override
    public List<DisplaySimulationResultDto> findAll() {
        return simulationResultService.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private DisplaySimulationResultDto toDto(SimulationResult r) {
        DisplaySimulationResultDto dto = new DisplaySimulationResultDto();

        dto.setId(r.getId());
        dto.setPredictedTemperature(r.getPredictedTemperature());
        dto.setPredictedPollution(r.getPredictedPollution());
        dto.setPredictedTraffic(r.getPredictedTraffic());
        dto.setHeatRiskLevel(r.getHeatRiskLevel());
        dto.setAirRiskLevel(r.getAirRiskLevel());
        dto.setTrafficRiskLevel(r.getTrafficRiskLevel());
        dto.setOverallRiskLevel(r.getOverallRiskLevel());
        dto.setGeneratedAt(r.getGeneratedAt());

        if (r.getScenario() != null) {
            dto.setScenarioId(r.getScenario().getId());
        }

        if (r.getLocation() != null) {
            dto.setLocationId(r.getLocation().getId());
        }

        return dto;
    }
}