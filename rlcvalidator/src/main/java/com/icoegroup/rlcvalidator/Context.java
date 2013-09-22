package com.icoegroup.rlcvalidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ch.qos.logback.classic.LoggerContext;

@Component
public class Context implements ApplicationContextAware {

	File rlcFile;
	Document domDocument;
	Logger log = LoggerFactory.getLogger(Context.class);

	@Value("${rlcfile.name}")
	String rlcFileName;

	@Value("${profile.name}")
	String profileName;

	String rlcName;

	private InputStream rlcInputStream;
	private AbstractApplicationContext applicationContext;

	public String getRlcFileName() {
		return rlcFileName;
	}

	public InputStream getRlcInputStream() {
		if (rlcInputStream != null) {
			if (rlcInputStream.markSupported()) {
				try {
					rlcInputStream.reset();
					return rlcInputStream;
				} catch (IOException e) {
					log.warn("InputStream repoted mark support but can't reset()");
				}
			} else {
				try {
					rlcInputStream.close();
				} catch (IOException e) {
					log.warn(e.getMessage());
				}
			}
		}
		if (!getRlcFile().getName().endsWith(".xml"))
			try {
				// Closed on context close
				@SuppressWarnings("resource")
				ZipFile zipFile = new ZipFile(getRlcFile());
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				ZipEntry exportedEntry = null;
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if (!(entryName.contains("/") || entryName.contains("\\"))
							&& entryName.endsWith(".xml")) {
						exportedEntry = entry;
						break;
					}
				}
				if (exportedEntry == null) {
					log.error("No xml entry in RLC file.");
					throw new ValidatorConfigurationException(
							"No xml entry in RLC file.");
				}

				rlcInputStream = zipFile.getInputStream(exportedEntry);
			} catch (IOException e) {
				log.error("Can't read RLC file '" + getRlcFileName() + "':"
						+ e.getMessage());
				throw new ValidatorConfigurationException(e);
			}
		else {
			try {
				rlcInputStream = new FileInputStream(getRlcFile());
			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
				throw new ValidatorConfigurationException(e);
			}
		}
		if (rlcInputStream.markSupported())
			rlcInputStream.mark(Integer.MAX_VALUE);
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
				domDocument = db.parse(getRlcInputStream());
			} catch (ValidatorConfigurationException e) {
				throw e;
			} catch (Exception e) {
				log.error("Error reading file: " + rlcFile.getAbsolutePath());
				throw new ValidatorConfigurationException(
						"Can't read RLC file.", e);
			}

		}
		return domDocument;
	}

	public void setRlcFileName(String rlcFileName) {
		if (rlcFileName == null || rlcFileName.length() == 0)
			throw new ValidatorConfigurationException("Empty RLC file name.");
		this.rlcFileName = rlcFileName.replace('\'', '/');
		rlcName = rlcFileName.indexOf('/') != -1 ? rlcFileName.substring(
				rlcFileName.lastIndexOf('/') + 1, rlcFileName.indexOf('.'))
				: rlcFileName.substring(0, rlcFileName.indexOf('.'));
		if (!getRlcFile().exists()) {
			throw new ValidatorConfigurationException("No such file: "
					+ rlcFileName);
		}
		MDC.put("rlcname", rlcName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (AbstractApplicationContext) applicationContext;
		this.applicationContext
				.addApplicationListener(new ApplicationListener<ApplicationEvent>() {

					@Override
					public void onApplicationEvent(ApplicationEvent event) {
						if (event instanceof ContextClosedEvent) {
							if (rlcInputStream != null) {
								try {
									rlcInputStream.close();
								} catch (IOException e) {
									log.warn(e.getMessage());
								}
							}
							LoggerContext lc = (LoggerContext) LoggerFactory
									.getILoggerFactory();
							lc.stop();
						}
					}
				});

	}

	public String getRlcName() {
		return rlcName;
	}

	public void setRlcName(String rlcName) {
		this.rlcName = rlcName;
	}

	public String getProfileName() {
		return profileName;
	}
}
