package ru.gb.timesheet;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.EmployeeRepository;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.TimesheetRepository;

@EnableDiscoveryClient
// @EnableEurekaClient
@SpringBootApplication
public class TimesheetRestApplication {
	private final static String names[] = { "Ivan", "Denis", "Max", "Igor", "Kostya", "Ira", "Tanya", "Dominik",
			"Polina" };
	private final static String surnames[] = { "Ivanov", "Petrov", "Vasechkin", "Pushkin", "Mask", "Tesla", "Veyder",
			"Bekk", "Malcev" };

	// тестирование приложения
	// http://localhost:8080/swagger-ui/index.html
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(TimesheetRestApplication.class, args);
		ProjectRepository projectRepo = ctx.getBean(ProjectRepository.class);
		for (int i = 1; i <= 5; i++) {
			Project project = new Project();
			project.setName("Project #" + i);
			project.getEmployees().add(ThreadLocalRandom.current().nextLong(1, 6));
			projectRepo.save(project);
		}

		TimesheetRepository timesheetRepo = ctx.getBean(TimesheetRepository.class);

		LocalDate createdAt = LocalDate.now();
		for (int i = 1; i <= 10; i++) {
			createdAt = createdAt.plusDays(1);

			Timesheet timesheet = new Timesheet();
			timesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
			timesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 6));
			timesheet.setCreatedAt(createdAt);
			timesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));

			timesheetRepo.save(timesheet);
		}

		EmployeeRepository employeeRepository = ctx.getBean(EmployeeRepository.class);
		for (int i = 1; i <= 5; i++) {
			Employee employee = new Employee();
			employee.setName(names[ThreadLocalRandom.current().nextInt(names.length)]);
			employee.setSurname(surnames[ThreadLocalRandom.current().nextInt(surnames.length)]);
			employee.getProjects().add(ThreadLocalRandom.current().nextLong(1, 6));
			employee.setAge(20 + ThreadLocalRandom.current().nextInt(30));
			employeeRepository.save(employee);
		}
	}

}
