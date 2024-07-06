package ru.gb.timesheet.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.TimesheetService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/timesheets")
public class TimesheetController {

  // GET - получить - не содержит тела
  // POST - create
  // PUT - изменение
  // PATCH - изменение
  // DELETE - удаление

  // @GetMapping("/timesheets/{id}") // получить конкретную запись по
  // идентификатору
  // @DeleteMapping("/timesheets/{id}") // удалить конкретную запись по
  // идентификатору
  // @PutMapping("/timesheets/{id}") // обновить конкретную запись по
  // идентификатору

  private final TimesheetService service;

  public TimesheetController(TimesheetService service) {
    this.service = service;
  }

  // /timesheets/{id}
  @GetMapping("/{id}") // получить все
  public ResponseEntity<Timesheet> get(@PathVariable Long id) {
    Optional<Timesheet> ts = service.getById(id);

    if (ts.isPresent()) {
      // return ResponseEntity.ok().body(ts.get());
      return ResponseEntity.status(HttpStatus.OK).body(ts.get());
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping // получить все
  public ResponseEntity<List<Timesheet>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @PostMapping // создание нового ресурса
  public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheet) {
    Optional<Timesheet> ts = service.create(timesheet);
    if (ts.isPresent()) {
      // 201 Created
      return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);
    } else {
      // 409 Conflict
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);

    // 204 No Content
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/createdAtAfter")
  public ResponseEntity<List<Timesheet>> getTimesheetsCreatedAfter(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtAfter) {
    List<Timesheet> timesheets = service.findByCreatedAtAfter(createdAtAfter);
    return ResponseEntity.ok(timesheets);
  }

  @GetMapping("/createdAtBefore")
  public ResponseEntity<List<Timesheet>> getTimesheetsCreatedBefore(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtBefore) {
    List<Timesheet> timesheets = service.findByCreatedAtBefore(createdAtBefore);
    return ResponseEntity.ok(timesheets);
  }

}
