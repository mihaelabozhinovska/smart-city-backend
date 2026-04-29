package com.example.smartcity.service.domain;

import com.example.smartcity.model.dto.create.SimulateRequestDto;
import com.example.smartcity.model.dto.display.DisplaySimulationResultDto;

public interface SimulationEngine {

    DisplaySimulationResultDto simulate(SimulateRequestDto request);
}