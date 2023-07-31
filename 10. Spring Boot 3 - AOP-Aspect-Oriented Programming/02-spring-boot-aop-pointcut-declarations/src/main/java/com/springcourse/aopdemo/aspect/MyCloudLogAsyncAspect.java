package com.springcourse.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class MyCloudLogAsyncAspect {

    @Before("com.springcourse.aopdemo.aspect.AopExpressions.forDaoPackageNoGetterSetter()")
    public void logToCloud() {

        System.out.println("\n=====>>> Logging to Cloud in async fashion");
    }
}
