package com.example.smartcity.service.application;

import com.example.smartcity.model.dto.create.SimulateRequestDto;
import com.example.smartcity.model.dto.display.DisplaySimulationResultDto;

import java.util.List;

public interface SimulationApplicationService {

    DisplaySimulationResultDto runSimulation(Long scenarioId);

    List<DisplaySimulationResultDto> findAll();
    DisplaySimulationResultDto simulate(SimulateRequestDto request);
}