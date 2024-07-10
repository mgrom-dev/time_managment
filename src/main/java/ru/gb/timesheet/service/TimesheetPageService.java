package ru.gb.timesheet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.timesheet.page.TimesheetPageDto;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetPageService {

  private final TimesheetService timesheetService;
  private final ProjectService projectService;

  public List<TimesheetPageDto> findAll() {
    return timesheetService.getAll().stream()
      .map(this::convert)
      .toList();
  }

  public Optional<TimesheetPageDto> findById(Long id) {
    return timesheetService.getById(id) // Optional<Timesheet>
      .map(this::convert);
  }

  private TimesheetPageDto convert(Timesheet timesheet) {
    Project project = projectService.getById(timesheet.getProjectId())
      .orElseThrow();

    TimesheetPageDto timesheetPageParameters = new TimesheetPageDto();
    timesheetPageParameters.setProjectName(project.getName());
    timesheetPageParameters.setId(String.valueOf(timesheet.getId()));
    timesheetPageParameters.setProjectId(project.getId() + "");
    // 150 -> 2h30m
    timesheetPageParameters.setMinutes(String.valueOf(timesheet.getMinutes()));
    timesheetPageParameters.setCreatedAt(timesheet.getCreatedAt().format(DateTimeFormatter.ISO_DATE));

    return timesheetPageParameters;
  }

}
