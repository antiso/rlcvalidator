package com.icoegroup.rlcvalidator.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icoegroup.rlcvalidator.Context;
import com.icoegroup.rlcvalidator.IValidator;
import com.icoegroup.rlcvalidator.XPathValidator;
import com.icoegroup.rlcvalidator.XsdValidator;

public class EqualsValidatorTest {

	private ClassPathXmlApplicationContext ctx;
	private Context validationContext;
	private IValidator xpathValidator;

	@Before
	public void initContext() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		validationContext = ctx.getBean(Context.class);
		xpathValidator = (IValidator) ctx.getBean("CommPointsFolderValidator");
	}

	
	@Test
	public void testValidateFolders() {
		String rlcFileName = "src/test/resources/testfiles/routes.xml";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(xpathValidator.validate());
	}

	@Test
	public void testErrValidateFolders() {
		String rlcFileName = "src/test/resources/testfiles/routes_err.xml";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(!xpathValidator.validate());
	}
}
