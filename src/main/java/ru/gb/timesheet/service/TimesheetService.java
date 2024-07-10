package ru.gb.timesheet.service;

import org.springframework.stereotype.Service;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.TimesheetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // то же самое, что и Component
public class TimesheetService {

  private final TimesheetRepository repository;
  private final ProjectService projectService;

  public TimesheetService(TimesheetRepository repository, ProjectService projectService) {
    this.repository = repository;
    this.projectService = projectService;
  }

  public Optional<Timesheet> getById(Long id) {
    return repository.getById(id);
  }

  public List<Timesheet> getAll() {
    return repository.getAll(null, null);
  }
  
  public List<Timesheet> findAll(LocalDate createdAtBefore, LocalDate createdAtAfter) {
    return repository.getAll(createdAtBefore, createdAtAfter);
  }

  public Optional<Timesheet> create(Timesheet timesheet) {
    if (projectService.getById(timesheet.getProjectId()).isPresent()) {
      timesheet.setCreatedAt(LocalDate.now());
      return Optional.of(repository.create(timesheet));
    } else {
      return Optional.empty();
    }
  }

  public void delete(Long id) {
    repository.delete(id);
  }

  public List<Timesheet> findAll() {
    return findAll(null, null);
  }

  public List<Timesheet> findByCreatedAtAfter(LocalDate date) {
    return getAll().stream()
        .filter(timesheet -> timesheet.getCreatedAt().isAfter(date))
        .collect(Collectors.toList());
  }

  public List<Timesheet> findByCreatedAtBefore(LocalDate date) {
    return getAll().stream()
        .filter(timesheet -> timesheet.getCreatedAt().isBefore(date))
        .collect(Collectors.toList());
  }

}
