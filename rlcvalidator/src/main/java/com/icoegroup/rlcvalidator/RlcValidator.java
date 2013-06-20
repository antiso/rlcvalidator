package com.icoegroup.rlcvalidator;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RlcValidator {

	static Logger log = LoggerFactory.getLogger(RlcValidator.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java -jar rlcvalidator.jar <rlcfile>");
			System.exit(1);
		}
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		ctx.registerShutdownHook();
		ctx.getBean(Context.class).setRlcFileName(args[0]);
		Map<String, Validator> validators = ctx.getBeansOfType(Validator.class);

		Set<String> validatorBeanNames = validators.keySet();
		for (String validatorName : validatorBeanNames) {
			log.info(Validator.VALIDATION_KEY, "Executing validator: "
					+ validatorName);
			try {
				validators.get(validatorName).validate();
			} catch (Throwable e) {

			}
		}

	}
}
