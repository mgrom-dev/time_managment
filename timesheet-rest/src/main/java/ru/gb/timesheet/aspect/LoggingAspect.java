package ru.gb.timesheet.aspect;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j // Slf4j - Simple logging facade for java
@Component
@Aspect
public class LoggingAspect {

  // Before
  // AfterThrowing
  // AfterReturning
  // After = AfterReturning + AfterThrowing
  // Around ->

//  Bean = TimesheetService, obj = timesheetService
  // proxyTimesheetService(obj)

  @Pointcut("execution(* ru.gb.timesheet.service.TimesheetService.*(..))")
  public void timesheetServiceMethodsPointcut() {
  }

  // Pointcut - точка входа в аспект
  @Before(value = "timesheetServiceMethodsPointcut()")
  public void beforeTimesheetServiceFindById(JoinPoint jp) {
    String methodName = jp.getSignature().getName();
    String argsString = Arrays.stream(jp.getArgs())
                .filter(arg -> arg != null)
                .map(arg -> arg.getClass().getSimpleName() + " = " + arg.toString())
                .collect(Collectors.joining(", "));
    log.info("Before -> TimesheetService.{}({})", methodName, argsString);
  }

  @AfterThrowing(value = "timesheetServiceMethodsPointcut()", throwing = "ex")
  public void afterTimesheetServiceFindById(JoinPoint jp, Exception ex) {
    String methodName = jp.getSignature().getName();
    log.info("AfterThrowing -> TimesheetService#{} -> {}", methodName, ex.getClass().getName());
  }

}
