package lev.filippov.demomvc.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    @Pointcut("execution(public * lev.filippov.demomvc.services.*.*(..))")
    private void servicesWithArguments(){};

    @Pointcut("execution(public * lev.filippov.demomvc.services.*.*())")
    private void servicesWithoutArguments(){};

    @Before(value = "servicesWithArguments() || servicesWithoutArguments()")
    public void log(JoinPoint point){
        LoggerFactory.getLogger(point.getTarget().getClass().getName()).info(point.toShortString());
    }
}
