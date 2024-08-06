package ru.gb.timesheet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.ProjectService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService service;

  public ProjectController(ProjectService service) {
    this.service = service;
  }

  @Operation(summary = "Получить проект по ID", description = "Возвращает проект с указанным ID. Если проект не найден, возвращает 404.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Проект найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
      @ApiResponse(responseCode = "404", description = "Проект не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
  })
  @Parameter(name = "id", description = "ID проекта", required = true, example = "1")
  @GetMapping("/{id}")
  public ResponseEntity<Project> get(@PathVariable Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Получить расписание проекта", description = "Возвращает список с расписанием для проекта с указанным ID. Если проект не найден, возвращает 404.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Расписание проекта найдено", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Расписание для проекта на найдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
  })
  @Parameter(name = "id", description = "ID проекта", required = true, example = "1")
  @GetMapping("/{id}/timesheets")
  public ResponseEntity<List<Timesheet>> getTimesheets(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(service.getTimesheets(id));
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Получить список проектов", description = "Возвращает список всех проектов.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список проектов найден", content = @Content(mediaType = "application/json")),
  })
  @GetMapping
  public ResponseEntity<List<Project>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @Operation(summary = "Создать новый проект", description = "Создает новый проект и возвращает его с присвоенным ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Проект создан", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
  })
  @PostMapping
  public ResponseEntity<Project> create(@RequestBody Project project) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(project));
  }

  @Operation(summary = "Удалить проект", description = "Удаляет проект с указанным ID. Если проект не найден, возвращает 404.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Проект удален"),
  })
  @Parameter(name = "id", description = "ID проекта", required = true, example = "1")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

}
