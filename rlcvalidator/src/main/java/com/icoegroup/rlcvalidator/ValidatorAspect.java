package com.icoegroup.rlcvalidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Aspect
public class ValidatorAspect {
	private static Logger log = LoggerFactory.getLogger(Validator.class);
	
	@Before("execution(* com.icoegroup.rlcvalidator.Validator.validate())")
	public void enableValidationLogging(JoinPoint joinPoint) {
		MDC.put(Validator.VALIDATION_KEY, ((Validator)joinPoint.getTarget()).getBeanName());
	}

	@AfterReturning(
			pointcut="execution(* com.icoegroup.rlcvalidator.Validator.validate())",
			returning="validationResult")
	public void disableValidationLogging(Object validationResult) {
		if (Boolean.TRUE.equals(validationResult))
			log.info(Validator.VALIDATION_INFO, "Validation successful.");
		else 
			log.error(Validator.VALIDATION_INFO, "Validation failed.");
		MDC.remove(Validator.VALIDATION_KEY);
	}
}
