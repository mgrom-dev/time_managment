package ru.gb.timesheet.hw;

public class Homework {

  /**
   * 1. Создать класс Employee - Сотрудник с полями
   * 1.1 Идентификтаор
   * 1.2 Имя (и фамилия)
   * 1.3 ...
   * 1.4 Создать контроллер-сервис-репозиторий для этой сущности
   *
   * 2. В timesheet добавить поле employee типа Employee (или employeeId тип Long)
   * 3. Создать ресурс /employees/{id}/timesheets - получить все таймшиты по сотруднику
   *
   * 4. ** Связываем Project <-> Employee отношением ManyToMany
   * 4.1 Т.е. на проекте может быть несколько сотрдников; один сотрудник может быть на нескольких проектах.
   */

  // POST /timesheets
  // {projectId: 1, employeeId: 3}

}
