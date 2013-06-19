package com.icoegroup.rlcvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Validator {
	

	protected Logger log = LoggerFactory.getLogger(getClass()); 
	
	@Autowired
	private Context ctx;

	public Context getValidationContext() {
		return ctx;
	}

	public void setValidationContext(Context ctx) {
		this.ctx = ctx;
	}
	
	public abstract boolean validate();

}
