package com.icoegroup.rlcvalidator;

import java.util.List;

import org.springframework.beans.factory.BeanNameAware;

public class ValidationProfile implements IValidator, BeanNameAware{
	List<IValidator> validators;
	private String beanName;

	public List<IValidator> getValidators() {
		return validators;
	}

	public void setValidators(List<IValidator> validators) {
		this.validators = validators;
	}

	@Override
	public boolean validate() {
		boolean result = true;
		for (IValidator validator: validators) {
			result &= validator.validate();
		}
		return result;
	}

	@Override
	public void setBeanName(String name) {
		beanName = name;
	}

	public String getBeanName() {
		return beanName;
	}
	
}
