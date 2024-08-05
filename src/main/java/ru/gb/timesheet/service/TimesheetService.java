package ru.gb.timesheet.service;

import org.springframework.stereotype.Service;

import ru.gb.timesheet.aspect.Recover;
import ru.gb.timesheet.aspect.Timer;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.TimesheetRepository;

import org.slf4j.event.Level;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // то же самое, что и Component
@Timer(level = Level.TRACE)
public class TimesheetService {

  private final TimesheetRepository timesheetRepository;
  private final ProjectRepository projectRepository;

  public TimesheetService(TimesheetRepository repository, ProjectRepository projectRepository) {
    this.timesheetRepository = repository;
    this.projectRepository = projectRepository;
  }

  public Optional<Timesheet> getById(Long id) {
    return timesheetRepository.findById(id);
  }

  public List<Timesheet> getAll() {
    return timesheetRepository.findAll();
  }

  public List<Timesheet> findAll(LocalDate createdAtBefore, LocalDate createdAtAfter) {
    createdAtBefore = createdAtBefore == null ? LocalDate.MIN : createdAtBefore;
    createdAtAfter = createdAtAfter == null ? LocalDate.MAX : createdAtAfter;
    return timesheetRepository.findByCreatedAtBetween(createdAtAfter, createdAtBefore);
  }

  @Recover(noRecoverFor = {NoSuchElementException.class})
  public Optional<Timesheet> create(Timesheet timesheet) {
    if (Objects.isNull(timesheet.getProjectId())) {
      throw new IllegalArgumentException("projectId must not be null");
    }

    if (projectRepository.findById(timesheet.getProjectId()).isEmpty()) {
      throw new NoSuchElementException("Project with id " + timesheet.getProjectId() + " does not exists");
    }

    timesheet.setCreatedAt(LocalDate.now());
    return Optional.of(timesheetRepository.save(timesheet));
  }

  public void delete(Long id) {
    timesheetRepository.deleteById(id);
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
