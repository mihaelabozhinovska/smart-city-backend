package com.example.smartcity.service.application;

import com.example.smartcity.model.dto.DisplaySimulationResultDto;

import java.util.List;

public interface SimulationApplicationService {

    DisplaySimulationResultDto runSimulation(Long scenarioId);

    List<DisplaySimulationResultDto> findAll();
}