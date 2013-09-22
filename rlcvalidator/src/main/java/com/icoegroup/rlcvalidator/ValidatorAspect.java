package com.icoegroup.rlcvalidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Aspect
public class ValidatorAspect {
	private static Logger log = LoggerFactory.getLogger(Validator.class);

	@Before("execution(* com.icoegroup.rlcvalidator.IValidator.validate())")
	public void enableValidationLogging(JoinPoint joinPoint) {
		final Object targetValidator = joinPoint.getTarget();
		if (targetValidator instanceof Validator) {
			final String beanName = ((Validator) targetValidator).getBeanName();
			MDC.put(IValidator.VALIDATION_KEY, beanName);
			log.info(IValidator.VALIDATION_INFO, "Executing validator: "
					+ beanName);
		} else {
			final String beanName = ((ValidationProfile) targetValidator)
					.getBeanName();
			log.info(IValidator.VALIDATION_INFO, "Starting profile: "
					+ beanName);
		}
	}

	@AfterReturning(pointcut = "execution(* com.icoegroup.rlcvalidator.IValidator.validate())", returning = "validationResult")
	public void disableValidationLogging(Object validationResult) {
		if (Boolean.TRUE.equals(validationResult))
			log.info(IValidator.VALIDATION_INFO, "Validation successful.");
		else
			log.error(IValidator.VALIDATION_INFO, "Validation failed.");
		MDC.remove(IValidator.VALIDATION_KEY);
	}

	@AfterThrowing(pointcut = "execution(* com.icoegroup.rlcvalidator.IValidator.validate())", throwing = "ex")
	public void catchValidationErrors(Throwable ex) {
		log.error(IValidator.VALIDATION_INFO, "Exception during validation: "
				+ ex.getMessage());
		MDC.remove(IValidator.VALIDATION_KEY);

	}
}
