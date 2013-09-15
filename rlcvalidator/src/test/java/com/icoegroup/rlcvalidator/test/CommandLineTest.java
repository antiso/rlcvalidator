package com.icoegroup.rlcvalidator.test;

import java.security.Permission;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.icoegroup.rlcvalidator.RlcValidator;

//TODO Add exit code value checks if needed

public class CommandLineTest {

    @SuppressWarnings("serial")
	protected static class ExitException extends SecurityException 
    {
        public final int status;
        public ExitException(int status) 
        {
            super("There is no escape!");
            this.status = status;
        }
    }

    private static class NoExitSecurityManager extends SecurityManager 
    {
        @Override
        public void checkPermission(Permission perm) 
        {
            // allow everything.
        }
        @Override
        public void checkPermission(Permission perm, Object context) 
        {
            // allow everything.
        }
        @Override
        public void checkExit(int status) 
        {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }
    
    @Before
    public void setUp() throws Exception 
    {
        System.setSecurityManager(new NoExitSecurityManager());
    }

    @After
    public void tearDown() throws Exception 
    {
        System.setSecurityManager(null); // or save and restore original
    }
    
	/**
	 * Test correct file with 'default' profile
	 */
	@Test
	public void testCorrectWithDefaultProfile() {
		String [] params = new String[] {"src/test/resources/testfiles/routes.xml"};
		RlcValidator.main(params);
	}
	
	/**
	 * Test incorrect file with 'default' profile
	 */
	@Test
	public void testIncorrectWithDefaultProfile() {
		String [] params = new String[] {"src/test/resources/testfiles/routes_err.xml"};
		RlcValidator.main(params);
	}
	
	/**
	 * Test correct file with 'default' profile
	 */
	@Test
	public void testCorrectWithParamDefaultProfile() {
		String [] params = new String[] {"-P", "default", "src/test/resources/testfiles/routes.xml"};
		RlcValidator.main(params);
	}
	
	/**
	 * Test incorrect file with 'default' profile
	 */
	@Test
	public void testIncorrectWithParamDefaultProfile() {
		String [] params = new String[] {"-P", "default", "src/test/resources/testfiles/routes_err.xml"};
		RlcValidator.main(params);
	}

	@Test(expected=ExitException.class)
	public void testWithoutParams() {
		RlcValidator.main(new String[]{});
	}

	@Test(expected=ExitException.class)
	public void testWithIncorrectFileParam() {
		String[] args = new String[]{"no/such/file.ext"};
		RlcValidator.main(args);
	}

	@Test(expected=ExitException.class)
	public void testWithIncorrectProfileParam() {
		String[] args = new String[]{"-P", "nosuchprofile", "no/such/file.ext"};
		RlcValidator.main(args);
	}

	@Test(expected=ExitException.class)
	public void testWithIncorrectParam() {
		RlcValidator.main(new String[]{"-xz"});
	}

}
