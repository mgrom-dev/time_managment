package ru.gb.timesheet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.ProjectService;
import ru.gb.timesheet.service.TimesheetService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {
  private final ProjectService service;
  private final TimesheetService timesheetService;

  public ProjectController(ProjectService service, TimesheetService timesheetService) {
    this.service = service;
    this.timesheetService = timesheetService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Project> get(@PathVariable Long id) {
    Optional<Project> project = service.getById(id);
    if (project.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(project.get());
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}/timesheets")
  public ResponseEntity<List<Timesheet>> getTimesheets(@PathVariable Long id) {
    Optional<Project> project = service.getById(id);
    if (project.isPresent()) {
      List<Timesheet> timesheets = timesheetService.getAll().stream()
          .filter(ts -> ts.getProjectId() == id).toList();
      return ResponseEntity.status(HttpStatus.OK).body(timesheets);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping
  public ResponseEntity<List<Project>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @PostMapping
  public ResponseEntity<Project> create(@RequestBody Project project) {
    project = service.create(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(project);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

}
