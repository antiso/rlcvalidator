package com.icoegroup.rlcvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Validator implements BeanNameAware {

	public static final Marker VALIDATION_INFO = MarkerFactory
			.getMarker("VALIDATION_INFO");

	public static final String VALIDATION_KEY = "validator";

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected Context ctx;

	private String beanName;

	public Context getValidationContext() {
		return ctx;
	}

	public void setValidationContext(Context ctx) {
		this.ctx = ctx;
	}

	public abstract boolean validate();

	public void info(String format, Object arg) {
		log.info(VALIDATION_INFO, format, arg);
	}

	public void info(String msg, Throwable t) {
		log.info(VALIDATION_INFO, msg, t);
	}

	public void info(String msg) {
		log.info(VALIDATION_INFO, msg);
	}

	public void error(String msg) {
		log.error(VALIDATION_INFO, msg);
	}

	public void error(String msg, Throwable t) {
		log.error(VALIDATION_INFO, msg, t);
	}

	public void error(Throwable t) {
		log.error(VALIDATION_INFO, t.getCause() != null ? t.getCause()
				.getMessage() : t.getMessage(), t);
	}

	public void error(String format, Object... arguments) {
		log.error(VALIDATION_INFO, format, arguments);
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}

	protected String getBeanName() {
		return beanName;
	}

}
