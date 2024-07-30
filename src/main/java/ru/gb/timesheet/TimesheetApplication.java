package ru.gb.timesheet;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Role;
import ru.gb.timesheet.model.RoleEnum;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.model.User;
import ru.gb.timesheet.model.UserRole;
import ru.gb.timesheet.repository.EmployeeRepository;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.RoleRepository;
import ru.gb.timesheet.repository.TimesheetRepository;
import ru.gb.timesheet.repository.UserRepository;
import ru.gb.timesheet.repository.UserRoleRepository;

@SpringBootApplication
public class TimesheetApplication {
	private final static String names[] = { "Ivan", "Denis", "Max", "Igor", "Kostya", "Ira", "Tanya", "Dominik",
			"Polina" };
	private final static String surnames[] = { "Ivanov", "Petrov", "Vasechkin", "Pushkin", "Mask", "Tesla", "Veyder",
			"Bekk", "Malcev" };

	// тестирование приложения
	// http://localhost:8080/swagger-ui/index.html
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(TimesheetApplication.class, args);
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

		RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
		Role adm = roleRepository.save(new Role(RoleEnum.ADMIN.getName()));
		Role usr = roleRepository.save(new Role(RoleEnum.USER.getName()));
		Role rst = roleRepository.save(new Role(RoleEnum.REST.getName()));
		
		UserRepository userRepository = ctx.getBean(UserRepository.class);
		User admin = new User();
		admin.setLogin("admin");
		admin.setPassword("$2a$12$LbAPCsHn8ZN5MUDqDmIX7e9n1YlDkCxEt0lW3Q2WuW0M1vteo8jvG"); // admin
		admin.setRoles(List.of(adm.getId(), usr.getId(), rst.getId()));
		User user = new User();
		user.setLogin("user");
		user.setPassword("$2a$12$.dlnBAYq6sOUumn3jtG.AepxdSwGxJ8xA2iAPoCHSH61Vjl.JbIfq"); // user
		user.setRoles(List.of(usr.getId()));
		User rest = new User();
		rest.setLogin("rest");
		rest.setPassword("$2y$10$WQsLxE8ikbrx6j7RL3aKy.FeejrplCcaIfYXvbzce7JaPEsako.0u"); // rest
		rest.setRoles(List.of(rst.getId()));
		userRepository.save(admin);
		userRepository.save(user);
		userRepository.save(rest);

		UserRoleRepository userRoleRepository = ctx.getBean(UserRoleRepository.class);
		UserRole adminRole = new UserRole();
		adminRole.setUserId(1L);
		adminRole.setRoleId(1L);
		userRoleRepository.save(adminRole);
		UserRole userRole = new UserRole();
		userRole.setUserId(2L);
		userRole.setRoleId(2L);
		userRoleRepository.save(userRole);
		UserRole restRole = new UserRole();
		restRole.setUserId(3L);
		restRole.setRoleId(3L);
		userRoleRepository.save(restRole);
	}

}
