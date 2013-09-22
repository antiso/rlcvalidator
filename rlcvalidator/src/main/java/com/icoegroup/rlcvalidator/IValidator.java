package com.icoegroup.rlcvalidator;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public interface IValidator {

	public static final Marker VALIDATION_INFO = MarkerFactory
			.getMarker("VALIDATION_INFO");
	public static final String VALIDATION_KEY = "validator";

	public abstract boolean validate();

}