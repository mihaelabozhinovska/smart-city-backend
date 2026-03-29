package com.example.smartcity.service.application;

import com.example.smartcity.model.dto.CreateLocationDto;
import com.example.smartcity.model.dto.DisplayLocationDto;

import java.util.List;
import java.util.Optional;

public interface LocationApplicationService {
    List<DisplayLocationDto> findAll();
    Optional<DisplayLocationDto> findById(Long id);
    DisplayLocationDto save(CreateLocationDto createLocationDto);
}