package com.icoegroup.rlcvalidator;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
		System.setProperty("rlcfile.name", args[0]);
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		ctx.registerShutdownHook();
		Map<String, Validator> validators = ctx.getBeansOfType(Validator.class);
		
		Set<String> validatorBeanNames = validators.keySet();
		for (String validatorName : validatorBeanNames) {
			log.info("Executing validator: " + validatorName);
			validators.get(validatorName).validate();
		}

	}

}
