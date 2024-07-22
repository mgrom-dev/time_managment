package ru.gb.timesheet.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "timesheet")
public class Timesheet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  private Long projectId;
  private Long employeeId;
  private int minutes;
  private LocalDate createdAt;

}
