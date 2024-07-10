package ru.gb.timesheet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.gb.timesheet.page.ProjectPageDto;
import ru.gb.timesheet.model.Project;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectPageService {

  private final ProjectService projectService;

  public List<ProjectPageDto> findAll() {
    return projectService.getAll().stream()
        .map(this::convert)
        .toList();
  }

  public Optional<ProjectPageDto> findById(Long id) {
    return projectService.getById(id)
        .map(this::convert);
  }

  private ProjectPageDto convert(Project project) {
    ProjectPageDto projectPageDto = new ProjectPageDto();
    projectPageDto.setName(project.getName());
    projectPageDto.setId(project.getId() + "");

    return projectPageDto;
  }

}
