package ru.gb.timesheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.gb.timesheet.client.ProjectResponse;
import ru.gb.timesheet.client.TimesheetResponse;
import ru.gb.timesheet.controller.TimesheetPageDto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
class RestTemplateConfig {

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}

@Service
public class TimesheetPageService {

  private final RestTemplate restTemplate;
  private static final String TIMESHEET_SERVICE_URL = "http://TIMESHEET-REST";

  @Autowired
  public TimesheetPageService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<TimesheetPageDto> findAll() {
    List<TimesheetResponse> timesheets = fetchTimesheets();
    if (timesheets == null) {
      throw new RuntimeException("Failed to fetch timesheets after multiple attempts.");
    }
    return mapToTimesheetPageDtos(timesheets);
  }

  public Optional<TimesheetPageDto> findById(Long id) {
    try {
      TimesheetResponse timesheet = restTemplate.getForObject(
          TIMESHEET_SERVICE_URL + "/timesheets/" + id,
          TimesheetResponse.class);

      if (timesheet == null) {
        return Optional.empty();
      }

      TimesheetPageDto timesheetPageDto = mapToTimesheetPageDto(timesheet);
      return Optional.of(timesheetPageDto);
    } catch (RestClientException e) {
      return Optional.empty();
    }
  }

  private List<TimesheetResponse> fetchTimesheets() {
    int attempts = 5;
    while (attempts-- > 0) {
      try {
        ResponseEntity<List<TimesheetResponse>> response = restTemplate.exchange(
            TIMESHEET_SERVICE_URL + "/timesheets",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TimesheetResponse>>() {
            });
        return response.getBody();
      } catch (RestClientException e) {
        // Log the exception and retry
      }
    }
    return null;
  }

  private List<TimesheetPageDto> mapToTimesheetPageDtos(List<TimesheetResponse> timesheets) {
    List<TimesheetPageDto> result = new ArrayList<>();
    for (TimesheetResponse timesheet : timesheets) {
      TimesheetPageDto timesheetPageDto = mapToTimesheetPageDto(timesheet);
      result.add(timesheetPageDto);
    }
    return result;
  }

  private TimesheetPageDto mapToTimesheetPageDto(TimesheetResponse timesheet) {
    TimesheetPageDto timesheetPageDto = new TimesheetPageDto();
    timesheetPageDto.setId(String.valueOf(timesheet.getId()));
    timesheetPageDto.setMinutes(String.valueOf(timesheet.getMinutes()));
    timesheetPageDto.setCreatedAt(timesheet.getCreatedAt().format(DateTimeFormatter.ISO_DATE));

    ProjectResponse project = restTemplate.getForObject(
        TIMESHEET_SERVICE_URL + "/projects/" + timesheet.getProjectId(),
        ProjectResponse.class);
    timesheetPageDto.setProjectName(project.getName());

    return timesheetPageDto;
  }
}