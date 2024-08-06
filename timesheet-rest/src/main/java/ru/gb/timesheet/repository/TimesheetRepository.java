package ru.gb.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.gb.timesheet.model.Timesheet;

import java.time.LocalDate;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long>
/* , NamedEntityRepository<Timesheet, Long> */ {

  // select * from timesheet where project_id = $1
  // Note: сломается, если в БД результат выдает больше одного значения
  // List<Timesheet> findByProjectId(Long projectId);

  // default List<Timesheet> findByCreatedAtBetweenUnsafe(LocalDate min, LocalDate
  // max) {
  // if (min == null && max == null) {
  // return findAll();
  // } else if (min == null) {
  // return findByCreatedAtLessThan(max);
  // }
  // }

  // select * from timesheet where created_at > $1 and created_at < $2
  List<Timesheet> findByCreatedAtBetween(LocalDate min, LocalDate max);

  // select * from timesheet where project_id = $1
  // order by created_at desc
  // jql - java query language
  @Query("select t from Timesheet t where t.projectId = :projectId order by t.createdAt desc")
  List<Timesheet> findByProjectId(Long projectId);

  List<Timesheet> findByEmployeeId(Long projectId);
  // @Query(nativeQuery = true, value = "select * from timesheet where project_id
  // = :projectId")
  // List<Long> findIdsByProjectId(Long projectId);

  // @Query(nativeQuery = true, value = "update timesheet set active = false where
  // project_id = :projectId")
  // @Modifying
  // void deactivateTimesheetsWithProjectId(Long projectId);

  // @Query("select t.id from Timesheet t where t.projectId = :projectId order by
  // t.createdAt desc")
  // List<Long> findIdsByProjectId(Long projectId);

  // select * from timesheet where project_id = $1
  // Note: сломается, если в БД результат выдает больше одного значения
  // Optional<Timesheet> findByProjectId(Long projectId);

  // select * from timesheet where project_id = $1
  // order by created_at desc
  // List<Timesheet> findByProjectIdOrderByCreatedAtDesc(Long projectId);

  // select * from timesheet where project_id = $1 or minutes = $2
  // List<Timesheet> findByProjectIdOrMinutes(Long projectId, Integer minutes);

  // ... where project_name like '%projectNameLike%'
  // List<Timesheet> findByProjectNameLike(String projectNameLike);

}

// @Repository // @Component для классов, работающих с данными
// public class TimesheetRepository {

// private static Long sequence = 1L;
// private final List<Timesheet> timesheets = new ArrayList<>();

// public Optional<Timesheet> getById(Long id) {
// // select * from timesheets where id = $id
// return timesheets.stream()
// .filter(it -> Objects.equals(it.getId(), id))
// .findFirst();
// }

// public List<Timesheet> getAll(LocalDate createdAtBefore, LocalDate
// createdAtAfter) {
// Predicate<Timesheet> filter = it -> true;

// if (Objects.nonNull(createdAtBefore)) {
// filter = filter.and(it -> it.getCreatedAt().isBefore(createdAtBefore));
// }

// if (Objects.nonNull(createdAtAfter)) {
// filter = filter.and(it -> it.getCreatedAt().isAfter(createdAtAfter));
// }

// return timesheets.stream()
// .filter(filter)
// .toList();
// }

// public Timesheet create(Timesheet timesheet) {
// timesheet.setId(sequence++);
// timesheets.add(timesheet);
// return timesheet;
// }

// public void delete(Long id) {
// timesheets.stream()
// .filter(it -> Objects.equals(it.getId(), id))
// .findFirst()
// .ifPresent(timesheets::remove); // если нет - иногда посылают 404 Not Found
// }

// public List<Timesheet> findByCreatedAtAfter(LocalDate date) {
// return timesheets.stream()
// .filter(timesheet -> timesheet.getCreatedAt().isAfter(date))
// .collect(Collectors.toList());
// }

// public List<Timesheet> findByCreatedAtBefore(LocalDate date) {
// return timesheets.stream()
// .filter(timesheet -> timesheet.getCreatedAt().isBefore(date))
// .collect(Collectors.toList());
// }

// public List<Timesheet> findByProjectId(Long projectId) {
// return timesheets.stream()
// .filter(it -> Objects.equals(it.getProjectId(), projectId))
// .toList();
// }

// }
