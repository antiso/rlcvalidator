package com.icoegroup.rlcvalidator.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icoegroup.rlcvalidator.Context;
import com.icoegroup.rlcvalidator.IValidator;
import com.icoegroup.rlcvalidator.XsdValidator;

public class XsdValidatorTest {

	private ApplicationContext ctx;
	private Context validationContext;
	private IValidator xsdValidator;

	
	@Before
	public void initContext() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		validationContext = ctx.getBean(Context.class);
		xsdValidator = (IValidator) ctx.getBean("RouteValidator");
	}
	
	
	@Test
	public void testRoutes() {
		String rlcFileName = "src/test/resources/testfiles/routes.xml";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(xsdValidator.validate());
	}
	
	@Test
	public void testIncorrectRoutes() {
		String rlcFileName = "src/test/resources/testfiles/routes_err.xml";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(!xsdValidator.validate());
		
	}

}
