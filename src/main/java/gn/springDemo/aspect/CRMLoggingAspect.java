package gn.springDemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class CRMLoggingAspect {

    // setup logger
    private Logger logger = Logger.getLogger(getClass().getName());

    // setup pointcut declarations
    @Pointcut("execution(* gn.springDemo.controller.*.*(..))")
    private void forControllerPackage() {}

    // do the same for service and dao
    @Pointcut("execution(* gn.springDemo.service.*.*(..))")
    private void forServicePackage() {}

    @Pointcut("execution(* gn.springDemo.dao.*.*(..))")
    private void forDaoPackage() {}

    // add @Before advice

    // add @AfterRetuning advise
}
