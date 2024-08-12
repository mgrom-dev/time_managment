package ru.gb.aspect.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j // Slf4j - Simple logging facade for java
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

  private final LoggingProperties properties;

  @Pointcut("@annotation(ru.gb.aspect.logging.Logging)") // method
  public void loggingMethodsPointcut() {
  }

  @Pointcut("@within(ru.gb.aspect.logging.Logging)") // class
  public void loggingTypePointcut() {
  }

  @Around(value = "loggingMethodsPointcut() || loggingTypePointcut()")
  public Object loggingMethod(ProceedingJoinPoint pjp) throws Throwable {
    String methodName = pjp.getSignature().getName();

    // Log arguments
    if (properties.isPrintArgs()) {
      Object[] args = pjp.getArgs();
      StringBuilder argsLog = new StringBuilder("Arguments: [");
      for (int i = 0; i < args.length; i++) {
        if (args[i] == null) {
          continue;
        }
        argsLog.append(args[i].getClass().getSimpleName()).append(": ").append(args[i]);
        if (i < args.length - 1) {
          argsLog.append(", ");
        }
      }
      argsLog.append("]");
      log.atLevel(properties.getLevel()).log("Before -> TimesheetService#{} with {}", methodName, argsLog.toString());
    } else {
      log.atLevel(properties.getLevel()).log("Before -> TimesheetService#{}", methodName);
    }

    try {
      return pjp.proceed();
    } finally {
      log.atLevel(properties.getLevel()).log("After -> TimesheetService#{}", methodName);
    }
  }

}
