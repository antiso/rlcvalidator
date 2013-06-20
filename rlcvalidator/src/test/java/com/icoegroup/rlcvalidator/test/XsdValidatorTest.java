package com.icoegroup.rlcvalidator.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icoegroup.rlcvalidator.Context;
import com.icoegroup.rlcvalidator.XsdValidator;

public class XsdValidatorTest {

	private ApplicationContext ctx;
	private Context validationContext;
	private XsdValidator xsdValidator;

	
	@Before
	public void initContext() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		validationContext = ctx.getBean(Context.class);
		xsdValidator = (XsdValidator) ctx.getBean("RouteValidator");
	}
	
	
	@Test
	public void testRoutes() {
		String rlcFileName = "src/test/resources/testfiles/routes.rlc";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(xsdValidator.validate());
	}
	
	@Test
	public void testIncorrectRoutes() {
		String rlcFileName = "src/test/resources/testfiles/routes_err.rlc";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(!xsdValidator.validate());
		
	}

}
