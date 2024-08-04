package ru.gb.timesheet.hw;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Homework {

  /**
   * 1. В LoggingAspect добавить логирование типов и значений аргументов.
   * Например (пример вывода): TimesheetService.findById(Long = 3)
   * Эту информацию можно достать из joinPoint.getArgs()
   *
   * 2. * Создать аспект, который аспектирует методы, помеченные аннотацией Recover, и делает следующее:
   * 2.1 Если в процессе исполнения метода был exception (любой),
   * то его нужно залогировать ("Recovering TimesheetService#findById after Exception[RuntimeException.class, "exception message"]")
   * и вернуть default-значение наружу Default-значение: для примитивов значение по умолчанию, для ссылочных типов - null.
   * Для void-методов возвращать не нужно.
   *
   * 3. **** В аннотацию Recover добавить атрибут Class<?>[] noRecoverFor, в которое можно записать список классов исключений,
   * которые НЕ нужно отлавливать.
   * Это вхождение должно учитывать иерархию классов.
   *
   * Пример:
   * @Recover(noRecoverFor = {NoSuchElementException.class, IllegalStateException.class})
   * public Timesheet getById(Long id) {...}
   *
   */

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface Recover { // recover - восстанавливать
//    Class<?>[] noRecoverFor() default {};
  }
  
}
