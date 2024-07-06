package ru.gb.timesheet.repository;

import org.springframework.stereotype.Repository;

import ru.gb.timesheet.model.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProjectRepository {

  private static Long sequence = 1L;
  private final List<Project> projects = Collections.synchronizedList(new ArrayList<>());

  public synchronized Optional<Project> getById(Long id) {
    return projects.stream()
        .filter(it -> Objects.equals(it.getId(), id))
        .findFirst();
  }

  public List<Project> getAll() {
    synchronized (projects) {
      return List.copyOf(projects);
    }
  }

  public synchronized Project create(Project project) {
    project.setId(sequence++);
    projects.add(project);
    return project;
  }

  public void delete(Long id) {
    projects.removeIf(it -> Objects.equals(it.getId(), id));
  }

  public synchronized Optional<Project> update(Long id, Project updatedProject) {
    return getById(id).map(existingProject -> {
      existingProject.setName(updatedProject.getName());
      return existingProject;
    });
  }

}
