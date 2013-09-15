/**
 * 
 */
package com.icoegroup.rlcvalidator;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Jonathan Ben-Hamou
 * 
 */
public class XsdValidator extends Validator {

	public class NSRemovingHandler implements ContentHandler {

		ValidatorHandler validatorHandler;

		public NSRemovingHandler(ValidatorHandler validatorHandler) {
			this.validatorHandler = validatorHandler;
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			if ("".equals(prefix))
				return;
			else
				validatorHandler.startPrefixMapping(prefix, uri);

		}

		public int hashCode() {
			return validatorHandler.hashCode();
		}

		public void setDocumentLocator(Locator locator) {
			validatorHandler.setDocumentLocator(locator);
		}

		public boolean equals(Object obj) {
			return validatorHandler.equals(obj);
		}

		public void startDocument() throws SAXException {
			validatorHandler.startDocument();
		}

		public void endDocument() throws SAXException {
			validatorHandler.endDocument();
		}

		public void setContentHandler(ContentHandler receiver) {
			validatorHandler.setContentHandler(receiver);
		}

		public ContentHandler getContentHandler() {
			return validatorHandler.getContentHandler();
		}

		public void setErrorHandler(ErrorHandler errorHandler) {
			validatorHandler.setErrorHandler(errorHandler);
		}

		public ErrorHandler getErrorHandler() {
			return validatorHandler.getErrorHandler();
		}

		public void setResourceResolver(LSResourceResolver resourceResolver) {
			validatorHandler.setResourceResolver(resourceResolver);
		}

		public String toString() {
			return validatorHandler.toString();
		}

		public LSResourceResolver getResourceResolver() {
			return validatorHandler.getResourceResolver();
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			validatorHandler.endElement(uri, localName, qName);
		}

		public TypeInfoProvider getTypeInfoProvider() {
			return validatorHandler.getTypeInfoProvider();
		}

		public boolean getFeature(String name)
				throws SAXNotRecognizedException, SAXNotSupportedException {
			return validatorHandler.getFeature(name);
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			validatorHandler.characters(ch, start, length);
		}

		public void setFeature(String name, boolean value)
				throws SAXNotRecognizedException, SAXNotSupportedException {
			validatorHandler.setFeature(name, value);
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			validatorHandler.ignorableWhitespace(ch, start, length);
		}

		public void setProperty(String name, Object object)
				throws SAXNotRecognizedException, SAXNotSupportedException {
			validatorHandler.setProperty(name, object);
		}

		public void processingInstruction(String target, String data)
				throws SAXException {
			validatorHandler.processingInstruction(target, data);
		}

		public void skippedEntity(String name) throws SAXException {
			validatorHandler.skippedEntity(name);
		}

		public Object getProperty(String name)
				throws SAXNotRecognizedException, SAXNotSupportedException {
			return validatorHandler.getProperty(name);
		}

		public void endPrefixMapping(String prefix) throws SAXException {
			if ("".equals(prefix))
				return;
			validatorHandler.endPrefixMapping(prefix);
		}

		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			AttributesImpl attrs = new AttributesImpl();
			for (int i = 0; i < atts.getLength(); i++) {
				// if (!"xmlns".equals(atts.getLocalName(i)))
				attrs.addAttribute(atts.getURI(i), atts.getLocalName(i),
						atts.getQName(i), atts.getType(i), atts.getValue(i));
			}
			validatorHandler.startElement("", localName, qName, attrs);
		}

	}

	private final class ValidationErrorHandler implements ErrorHandler {
		private boolean hasError = false;

		public void warning(SAXParseException exception) throws SAXException {
			log.warn("WARNING:", exception);

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
		log.info("Validating " + getValidationContext().getRlcFileName()
				+ " against " + schemaName);
		// SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		// // parserFactory.setNamespaceAware(false);
		// parserFactory.setSchema(schema);
		// parserFactory.setValidating(true);
		// javax.xml.validation.Validator validator = schema.newValidator();
		ValidationErrorHandler errorHandler = new ValidationErrorHandler();

		try {
			XMLReader parser = XMLReaderFactory.createXMLReader();
			ValidatorHandler validatorHandler = schema.newValidatorHandler();
			validatorHandler.setErrorHandler(errorHandler);
			parser.setContentHandler(new NSRemovingHandler(validatorHandler));
			parser.parse(new InputSource(getValidationContext()
					.getRlcInputStream()));
			// validator.setFeature("http://xml.org/sax/features/namespaces",
			// false);
			// validator.validate(getValidationContext().getSAXSorce());
		} catch (SAXException e) {
			error(e.getMessage(), e);
			result = false;
		} catch (IOException e) {
			error(e.getMessage(), e);
			result = false;
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
