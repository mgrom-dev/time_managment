package ru.gb.timesheet.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.EmployeeService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  private final EmployeeService service;

  public EmployeeController(EmployeeService service) {
    this.service = service;
  }

  @GetMapping("/{id}") // получить все
  public ResponseEntity<Employee> get(@PathVariable Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}/timesheets")
  public ResponseEntity<List<Timesheet>> getTimesheetsByEmployeeId(@PathVariable Long id) {
    return ResponseEntity.ok(service.getTimesheets(id));
  }

  @GetMapping
  public ResponseEntity<List<Employee>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @PostMapping
  public ResponseEntity<Employee> create(@RequestBody Employee employee) {
    final Employee created = service.create(employee);

    // 201 Created
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);

    // 204 No Content
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
    return ResponseEntity.notFound().build();
  }

}
