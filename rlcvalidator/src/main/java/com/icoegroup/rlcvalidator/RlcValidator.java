package com.icoegroup.rlcvalidator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class RlcValidator {

	private static final int INCORRECT_FILE_NAME = 3;
	private static final int INCORRECT_PROFILE = 2;
	private static final int INCORRECT_OPTIONS = 1;
	static Logger log = LoggerFactory.getLogger(RlcValidator.class);
	private static Options options;
	private static AbstractApplicationContext ctx;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ctx = new ClassPathXmlApplicationContext("classpath:rlcvalidator.xml");
		initOptions();
		String profileName = "default";
		String rlcFileName = null;
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h") || cmd.getArgs().length == 0) {
				HelpFormatter formatter = new HelpFormatter();
				formatter
						.printHelp("rlcvalidator [options] <rlcfile>", options);
				System.exit(INCORRECT_OPTIONS);
			}
			if (cmd.hasOption("P")) {
				profileName = cmd.getOptionValue("P");
			}
			rlcFileName = cmd.getArgs()[0];
		} catch (ParseException e1) {
			HelpFormatter formatter = new HelpFormatter();
			System.out.println("Error: " + e1.getMessage());
			formatter.printHelp("rlcvalidator [options] <rlcfile>", options);
			System.exit(INCORRECT_OPTIONS);
		}
		loadContext(profileName);

		ctx.registerShutdownHook();
		try {
			ctx.getBean(Context.class).setRlcFileName(rlcFileName);
		} catch (ValidatorConfigurationException e) {
			System.err.println(e.getMessage());
			System.exit(INCORRECT_FILE_NAME);
		}
		ValidationProfile profile = ctx.getBeansOfType(ValidationProfile.class)
				.entrySet().iterator().next().getValue();
		List<Validator> validators = profile.getValidators();
		for (Validator validator : validators) {
			log.info("Executing validator: "
					+ validator.getBeanName());
			try {
				if (!validator.validate())
					log.info("Execution failed: "
							+ validator.getBeanName());

			} catch (Throwable e) {
				log.error(Validator.VALIDATION_INFO, "Failed to execute validator: "
						+ validator.getBeanName(), e);

			}
		}
	}

	private static void loadContext(String profileName) {
		try {
			ClassPathXmlApplicationContext profileContext = new ClassPathXmlApplicationContext();
			profileContext.setParent(ctx);
			profileContext.setConfigLocation(profileName + ".profile");
			profileContext.refresh();
			ctx = profileContext;
		} catch (Exception e) {
			if (e.getCause() instanceof IOException) {
				System.err.println("Can't load profile: " + profileName);
			} else {
				e.printStackTrace();
			}
			System.exit(INCORRECT_PROFILE);
		}
	}

	@SuppressWarnings("static-access")
	private static void initOptions() {
		options = new Options();
		options.addOption("h", false, "print help");
		Option profile = OptionBuilder.withArgName("profile").hasArg()
				.withDescription("use specified validation profile\n\t" +
						"'default' profile used by default").create("P");
		options.addOption(profile);
	}
}
