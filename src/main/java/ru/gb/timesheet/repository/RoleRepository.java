package ru.gb.timesheet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.gb.timesheet.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
