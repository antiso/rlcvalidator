package com.icoegroup.rlcvalidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;

@Aspect
public class ValidatorAspect {
	
	@Before("execution(* com.icoegroup.rlcvalidator.Validator.validate())")
	public void enableValidationLogging(JoinPoint joinPoint) {
		MDC.put(Validator.VALIDATION_KEY, ((Validator)joinPoint.getTarget()).getBeanName());
	}

	@After("execution(* com.icoegroup.rlcvalidator.Validator.validate())")
	public void disableValidationLogging(JoinPoint joinPoint) {
		MDC.remove(Validator.VALIDATION_KEY);
	}
}
