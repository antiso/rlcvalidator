package com.icoegroup.rlcvalidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@Component
public class Context {

	File rlcFile;
	Document domDocument;
	Logger log = LoggerFactory.getLogger("RLC Configuration");

	@Value("${rlcfile.name}")
	String rlcFileName;
	
	private FileInputStream rlcInputStream;

	public String getRlcFileName() {
		return rlcFileName;
	}

	public InputStream getRlcInputStream() {
		try {
			rlcInputStream = new FileInputStream(getRlcFile());
		} catch (FileNotFoundException e) {
			log.error("Can't read RLC file: " + getRlcFile().getAbsolutePath());
			throw new ValidatorConfigurationException(e);
		}
		return rlcInputStream;
	}

	public File getRlcFile() {
		if (rlcFile == null) {
			rlcFile = new File(rlcFileName);
		}
		return rlcFile;
	}

	public SAXSource getSAXSorce() {
		return new SAXSource(new InputSource(getRlcInputStream()));
	}

	public DOMSource getDomSource() {
		return new DOMSource(getDOMDocument());
	}

	public Source getSource() {
		return getSAXSorce();
	}

	public Document getDOMDocument() {
		if (domDocument == null) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				domDocument = db.parse(getRlcFile());
			} catch (Exception e) {
				log.error("Error reading file: " + rlcFile.getAbsolutePath());
				throw new ValidatorConfigurationException(
						"Can't read RLC file.", e);
			}

		}
		return domDocument;
	}
}
