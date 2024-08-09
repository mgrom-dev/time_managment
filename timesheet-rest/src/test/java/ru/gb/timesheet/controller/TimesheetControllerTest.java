package ru.gb.timesheet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.EmployeeRepository;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.TimesheetRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimesheetControllerTest {
  @Autowired
  private TimesheetRepository timesheetRepository;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private EmployeeRepository employeeRepository;

  @LocalServerPort
  private int port;
  private WebClient webClient;

  @BeforeEach
  void beforeEach() {
    webClient = WebClient.create("http://localhost:" + port);
    timesheetRepository.deleteAll();
    projectRepository.deleteAll();
    employeeRepository.deleteAll();
  }

  @Test
  void getByIdNotFound() {
    assertThrows(WebClientResponseException.NotFound.class, () -> {
      webClient.get()
          .uri("/timesheets/-2")
          .retrieve()
          .toBodilessEntity()
          .block();
    });
  }

  @Test
  void getByIdOk() {
    Timesheet timesheet = new Timesheet();
    timesheet.setEmployeeId(1L);
    timesheet.setProjectId(1L);
    timesheet.setMinutes(10);
    timesheet.setCreatedAt(LocalDate.now());
    Timesheet expected = timesheetRepository.save(timesheet);

    ResponseEntity<Timesheet> actual = webClient.get()
        .uri("/timesheets/" + expected.getId())
        .retrieve()
        .toEntity(Timesheet.class)
        .block();

    // assert 200 OK
    assertEquals(HttpStatus.OK, actual.getStatusCode());

    Timesheet responseBody = actual.getBody();
    assertNotNull(responseBody);
    assertEquals(expected.getId(), responseBody.getId());
    assertEquals(expected.getEmployeeId(), responseBody.getEmployeeId());
    assertEquals(expected.getProjectId(), responseBody.getProjectId());
    assertEquals(expected.getMinutes(), responseBody.getMinutes());
    assertEquals(expected.getCreatedAt(), responseBody.getCreatedAt());
  }

  @Test
  void getAll() {
    // create 3 timesheets for testing
    Timesheet timesheet1 = new Timesheet();
    timesheet1.setCreatedAt(LocalDate.of(2024, 8, 9));
    Timesheet timesheet2 = new Timesheet();
    timesheet2.setCreatedAt(LocalDate.of(2030, 8, 9));
    Timesheet timesheet3 = new Timesheet();
    timesheetRepository.save(timesheet1);
    timesheetRepository.save(timesheet2);
    timesheetRepository.save(timesheet3);

    ResponseEntity<List<Timesheet>> responseEntityAll = webClient.get()
        .uri("/timesheets")
        .retrieve()
        .toEntityList(Timesheet.class)
        .block();

    // assert 200 OK
    assertEquals(HttpStatus.OK, responseEntityAll.getStatusCode());

    // return 3 timesheets
    List<Timesheet> timesheetsAll = responseEntityAll.getBody();
    assertNotNull(timesheetsAll);
    assertEquals(3, timesheetsAll.size());
  }

  @Test
  void getAllAfter() {
    // create 3 timesheets for testing
    Timesheet timesheet1 = new Timesheet();
    timesheet1.setCreatedAt(LocalDate.of(2024, 8, 9));
    Timesheet timesheet2 = new Timesheet();
    timesheet2.setCreatedAt(LocalDate.of(2024, 8, 10));
    Timesheet timesheet3 = new Timesheet();
    timesheet3.setCreatedAt(LocalDate.of(2020, 8, 9));
    timesheetRepository.save(timesheet1);
    timesheetRepository.save(timesheet2);
    timesheetRepository.save(timesheet3);

    ResponseEntity<List<Timesheet>> responseEntityAll = webClient.get()
        .uri("timesheets?createdAtAfter=2024-08-09")
        .retrieve()
        .toEntityList(Timesheet.class)
        .block();

    // assert 200 OK
    assertEquals(HttpStatus.OK, responseEntityAll.getStatusCode());

    // return 2 timesheets
    List<Timesheet> timesheetsAll = responseEntityAll.getBody();
    assertNotNull(timesheetsAll);
    assertEquals(2, timesheetsAll.size());
  }

  @Test
  void getAllBefore() {
    // create 3 timesheets for testing
    Timesheet timesheet1 = new Timesheet();
    timesheet1.setCreatedAt(LocalDate.of(2024, 8, 9));
    Timesheet timesheet2 = new Timesheet();
    timesheet2.setCreatedAt(LocalDate.of(2024, 8, 10));
    Timesheet timesheet3 = new Timesheet();
    timesheet3.setCreatedAt(LocalDate.of(2020, 8, 9));
    timesheetRepository.save(timesheet1);
    timesheetRepository.save(timesheet2);
    timesheetRepository.save(timesheet3);

    ResponseEntity<List<Timesheet>> responseEntityAll = webClient.get()
        .uri("timesheets?createdAtBefore=2020-08-09")
        .retrieve()
        .toEntityList(Timesheet.class)
        .block();

    // assert 200 OK
    assertEquals(HttpStatus.OK, responseEntityAll.getStatusCode());

    // return 1 timesheets
    List<Timesheet> timesheetsAll = responseEntityAll.getBody();
    assertNotNull(timesheetsAll);
    assertEquals(1, timesheetsAll.size());
  }

  @Test
  void getAllBetween() {
    // create 3 timesheets for testing
    Timesheet timesheet1 = new Timesheet();
    timesheet1.setCreatedAt(LocalDate.of(2024, 8, 9));
    Timesheet timesheet2 = new Timesheet();
    timesheet2.setCreatedAt(LocalDate.of(2024, 8, 10));
    Timesheet timesheet3 = new Timesheet();
    timesheet3.setCreatedAt(LocalDate.of(2020, 8, 9));
    timesheetRepository.save(timesheet1);
    timesheetRepository.save(timesheet2);
    timesheetRepository.save(timesheet3);

    ResponseEntity<List<Timesheet>> responseEntityAll = webClient.get()
        .uri("timesheets?createdAtAfter=2024-08-09&createdAtBefore=2024-08-10")
        .retrieve()
        .toEntityList(Timesheet.class)
        .block();

    // assert 200 OK
    assertEquals(HttpStatus.OK, responseEntityAll.getStatusCode());

    // return 2 timesheets
    List<Timesheet> timesheetsAll = responseEntityAll.getBody();
    assertNotNull(timesheetsAll);
    assertEquals(2, timesheetsAll.size());
  }

  @Test
  void getAllNull() {
    ResponseEntity<List<Timesheet>> responseEntityAll = webClient.get()
        .uri("timesheets?createdAtAfter=2024-08-09&createdAtBefore=2024-08-10")
        .retrieve()
        .toEntityList(Timesheet.class)
        .block();

    assertEquals(HttpStatus.OK, responseEntityAll.getStatusCode());

    List<Timesheet> timesheetsAll = responseEntityAll.getBody();
    assertNotNull(timesheetsAll);
    assertEquals(0, timesheetsAll.size());
  }

  @Test
  void createTimesheet() {
    // given
    Timesheet timesheet = new Timesheet();
    timesheet.setEmployeeId(1L);
    timesheet.setProjectId(1L);
    timesheet.setMinutes(10);
    projectRepository.save(new Project());
    employeeRepository.save(new Employee());

    // POST /timesheets
    ResponseEntity<Timesheet> responseEntity = webClient.post()
        .uri("/timesheets")
        .bodyValue(timesheet)
        .retrieve()
        .toEntity(Timesheet.class)
        .block();

    // assert 201 Created
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    Timesheet responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals(timesheet.getEmployeeId(), responseBody.getEmployeeId());
    assertEquals(timesheet.getProjectId(), responseBody.getProjectId());
    assertEquals(timesheet.getMinutes(), responseBody.getMinutes());
    assertEquals(LocalDate.now(), responseBody.getCreatedAt());
  }

  @Test
  void deleteTimesheet() {
    // given
    Timesheet timesheet = new Timesheet();
    timesheet.setEmployeeId(1L);
    timesheet.setProjectId(1L);
    timesheet.setMinutes(10);
    timesheet.setCreatedAt(LocalDate.now());
    Timesheet savedTimesheet = timesheetRepository.save(timesheet);

    // DELETE /timesheets/{id}
    ResponseEntity<Void> responseEntity = webClient.delete()
        .uri("/timesheets/" + savedTimesheet.getId())
        .retrieve()
        .toBodilessEntity()
        .block();

    // assert 204 No Content
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    // ensure it's deleted
    assertFalse(timesheetRepository.findById(savedTimesheet.getId()).isPresent());
  }

  @Test
  void putTimesheet() {
    // given
    Timesheet timesheet = new Timesheet();
    timesheet.setEmployeeId(1L);
    timesheet.setProjectId(1L);
    timesheet.setMinutes(10);
    timesheet.setCreatedAt(LocalDate.now());
    Timesheet expected = timesheetRepository.save(timesheet);
    expected.setMinutes(20);

    // PUT /timesheets/{id}
    ResponseEntity<Timesheet> responseEntity = webClient.put()
        .uri("/timesheets/1")
        .bodyValue(expected)
        .retrieve()
        .toEntity(Timesheet.class)
        .block();

    // assert 200 OK
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Timesheet responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals(expected.getEmployeeId(), responseBody.getEmployeeId());
    assertEquals(expected.getProjectId(), responseBody.getProjectId());
    assertEquals(expected.getMinutes(), responseBody.getMinutes());
    assertEquals(expected.getCreatedAt(), responseBody.getCreatedAt());
  }
}
