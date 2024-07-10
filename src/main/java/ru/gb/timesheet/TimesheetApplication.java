package ru.gb.timesheet;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.TimesheetRepository;

@SpringBootApplication
public class TimesheetApplication {
	// тестирование приложения
	// http://localhost:8080/swagger-ui/index.html
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(TimesheetApplication.class, args);
		ProjectRepository projectRepo = ctx.getBean(ProjectRepository.class);
		for (int i = 1; i <= 5; i++) {
			Project project = new Project();
			project.setId((long) i);
			project.setName("Project #" + i);
			projectRepo.create(project);
		}

		TimesheetRepository timesheetRepo = ctx.getBean(TimesheetRepository.class);

		LocalDate createdAt = LocalDate.now();
		for (int i = 1; i <= 10; i++) {
			createdAt = createdAt.plusDays(1);

			Timesheet timesheet = new Timesheet();
			timesheet.setId((long) i);
			timesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
			timesheet.setCreatedAt(createdAt);
			timesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));

			timesheetRepo.create(timesheet);
		}
	}

}
