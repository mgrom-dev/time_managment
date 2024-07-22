package ru.gb.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.gb.timesheet.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}