package com.springcourse.aopdemo.aspect;

import com.springcourse.aopdemo.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspect {

    @Around("execution(* com.springcourse.aopdemo.service.*.getFortune(..))")
    public Object aroundGetFortune(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {

        // print out method we are advising on
        String method = theProceedingJoinPoint.getSignature().toShortString();
        System.out.println("\n========>>> Executing @Around on method: " + method);

        // get begin timestamp
        long begin = System.currentTimeMillis();

        // now, let's execute the method
        Object result = null;

        try {
            result = theProceedingJoinPoint.proceed();
        } catch (Exception exc) {

            // log the exception
            System.out.println(exc.getMessage());

            // rethrow exception
            throw  exc;
        }

        // get end timestamp
        long end = System.currentTimeMillis();

        // compute duration and display it
        long duration = end - begin;

        System.out.println("\n=====> Duration: " + duration / 1000.0 + " seconds");

        return result;
    }

    @Before("com.springcourse.aopdemo.aspect.AopExpressions.forDaoPackageNoGetterSetter()")
    public void beforeAddAccountAdvice(JoinPoint theJoinPoint) {

        System.out.println("\n=====>>> Executing @Before advice on addAccount()");

        // display the method signature
        MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();

        System.out.println("Method: " + methodSig);

        // display method arguments

        // get args
        Object[] args = theJoinPoint.getArgs();

        // loop through args
        for(Object tempArg : args) {
            System.out.println(tempArg);

            if (tempArg instanceof Account) {

                // downcast and print Account specific stuff
                Account theAccount = (Account) tempArg;

                System.out.println("account name: " + theAccount.getName());
                System.out.println("account level: " + theAccount.getLevel());
            }
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.springcourse.aopdemo.dao.AccountDAO.findAccounts(..))",
            throwing = "theExc")
    public void afterThrowingFindAccountsAdvice(JoinPoint theJoinPoint, Throwable theExc) {

        // print out which method we are advising on
        String method = theJoinPoint.getSignature().toShortString();
        System.out.println("\n========>>> Executing @AfterThrowing on method: " + method);

        // log the exception
        System.out.println("\n========>>> The exception is: " + theExc);
    }

    @AfterReturning(
            pointcut = "execution(* com.springcourse.aopdemo.dao.AccountDAO.findAccounts(..))",
            returning = "result"
    )
    public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {

        // print out which method we are advising on
        String method = theJoinPoint.getSignature().toShortString();
        System.out.println("\n========>>> Executing @AfterReturning on method: " + method);

        // print out the results of the method call
        System.out.println("\n========>>> result is: " + result);

        // let's post-process the data ... let's modify it :-)


        // convert the account names to uppercase
        convertAccountNameToUpperCase(result);

        System.out.println("\n========>>> result is: " + result);
    }

    @After("execution(* com.springcourse.aopdemo.dao.AccountDAO.findAccounts(..))")
    public void afterFinallyFindAccountAccountsAdvice(JoinPoint theJoinPoint) {

        // print out which method we are advising on
        String method = theJoinPoint.getSignature().toShortString();
        System.out.println("\n========>>> Executing @After (finally) on method: " + method);
    }

    private void convertAccountNameToUpperCase(List<Account> result) {

        // loop through accounts

            // get uppercase version of name
            for(Account tempAccount : result) {

                String theUpperName = tempAccount.getName().toUpperCase();

                // update the name on the account
                tempAccount.setName(theUpperName);
            }

    }
}
