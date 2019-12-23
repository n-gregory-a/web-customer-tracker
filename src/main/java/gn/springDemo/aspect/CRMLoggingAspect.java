package gn.springDemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
    private void forAppFlow() {}

    // add @Before advice
    @Before("forAppFlow()")
    public void before(JoinPoint joinPoint) {

        // display method we are calling
        String method = joinPoint.getSignature().toShortString();
        logger.info("====>> @Before: calling method: " + method);

        // display the args to the method

        // get the args
        Object[] args = joinPoint.getArgs();

        // loop thru and display args
        for (Object tempArg: args
             ) {
            logger.info("=====>> argument: " + tempArg);
        }

    }

    // add @AfterRetuning advise
}
