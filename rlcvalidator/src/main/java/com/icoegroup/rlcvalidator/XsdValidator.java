/**
 * 
 */
package com.icoegroup.rlcvalidator;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.ClassPathResource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Jonathan Ben-Hamou
 * 
 */
public class XsdValidator extends Validator {

	private final class ValidationErrorHandler implements ErrorHandler {
		private boolean hasError = false;

		public void warning(SAXParseException exception) throws SAXException {
			log.warn("WARNING:" , exception);
			
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			hasError = true;
			XsdValidator.this.error(exception.getMessage());
			
		}

		public void error(SAXParseException exception) throws SAXException {
			hasError = true;
			XsdValidator.this.error(exception.getMessage());
		}
	}

	String schemaName;
	private Schema schema;

	public XsdValidator() {
		super();
	}
	public XsdValidator(String schemaName) {
		super();
		this.schemaName = schemaName;
		ClassPathResource schemaRes = new ClassPathResource(schemaName);
		try {
			schema = SchemaFactory.newInstance(
					XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
					new SAXSource(new InputSource(schemaRes.getInputStream())));
		} catch (Exception e) {
			error("Can't read schema: ", e);
			throw new ValidatorConfigurationException(e);
		}
	}

	
	@Override
	public boolean validate() {
		boolean result = true;
		log.info("Validating " + getValidationContext().getRlcFileName() + " against " + schemaName);
		javax.xml.validation.Validator validator = schema.newValidator();
		ValidationErrorHandler errorHandler = new ValidationErrorHandler();
		validator.setErrorHandler(errorHandler);
		
		try {
			validator.validate(getValidationContext().getSAXSorce());
		} catch (Exception e) {
			result = false;
		}
		if (result && !errorHandler.hasError) {
			info("Validation successful.");
		} else {
			error("Validation failed.");
		}
		return result && !errorHandler.hasError;
	}

	protected String getSchemaName() {
		return schemaName;
	}

	protected void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

}
