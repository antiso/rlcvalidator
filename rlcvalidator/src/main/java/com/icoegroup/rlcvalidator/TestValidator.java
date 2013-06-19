package com.icoegroup.rlcvalidator;

import org.springframework.stereotype.Component;

@Component
public class TestValidator extends Validator {

	@Override
	public boolean validate() {
		log.info("Context returns: "  + getValidationContext().getRlcFileName());
		return true;
	}

}
