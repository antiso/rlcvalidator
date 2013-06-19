package com.icoegroup.rlcvalidator.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.icoegroup.rlcvalidator.XsdValidator;

public class XsdValidatorTest {

	private ApplicationContext ctx;

	@Test
	public void testRoutes() {
		System.setProperty("rlcfile.name", "src/test/resources/testfiles/routes.rlc");
		ctx = new FileSystemXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		Map<String, XsdValidator> validators = ctx.getBeansOfType(XsdValidator.class);
		assertTrue(validators.values().iterator().next().validate());
	}
	
	@Test
	public void testIncorrectRoutes() {
		System.setProperty("rlcfile.name", "src/test/resources/testfiles/routes_err.rlc");
		ctx = new FileSystemXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		Map<String, XsdValidator> validators = ctx.getBeansOfType(XsdValidator.class);
		assertTrue(!validators.values().iterator().next().validate());
		
	}

}
