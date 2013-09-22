package com.icoegroup.rlcvalidator.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icoegroup.rlcvalidator.ContentCompareValidator;
import com.icoegroup.rlcvalidator.Context;
import com.icoegroup.rlcvalidator.IValidator;
import com.icoegroup.rlcvalidator.Validator;
import com.icoegroup.rlcvalidator.XsdValidator;

public class ContentCompareValidatorTest {

	private ApplicationContext ctx;
	private Context validationContext;
	private IValidator contentValidator;

	@Before
	public void initContext() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:rlcvalidator.xml");
		validationContext = ctx.getBean(Context.class);
		contentValidator = (IValidator) ctx.getBean("CreateAckScriptValidator");
	}

	@Test
	public void test() {
		String rlcFileName = "src/test/resources/testfiles/routes.xml";
		validationContext.setRlcFileName(rlcFileName);
		assertTrue(contentValidator.validate());
	}

}
