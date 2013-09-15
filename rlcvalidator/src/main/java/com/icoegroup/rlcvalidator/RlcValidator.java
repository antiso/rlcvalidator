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
				System.exit(1);
			}
			if (cmd.hasOption("P")) {
				profileName = cmd.getOptionValue("P");
			}
			rlcFileName = cmd.getArgs()[0];
		} catch (ParseException e1) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("rlcvalidator [options] <rlcfile>", options);
			System.exit(1);
		}
		loadContext(profileName);

		ctx.registerShutdownHook();
		ctx.getBean(Context.class).setRlcFileName(rlcFileName);
		ValidationProfile profile = ctx.getBeansOfType(ValidationProfile.class)
				.entrySet().iterator().next().getValue();
		List<Validator> validators = profile.getValidators();
		for (Validator validator : validators) {
			log.info(Validator.VALIDATION_INFO, "Executing validator: "
					+ validator.getBeanName());
			try {
				if (!validator.validate())
					log.info(Validator.VALIDATION_INFO, "Execution failed: "
							+ validator.getBeanName());

			} catch (Throwable e) {

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
			System.exit(2);
		}
	}

	@SuppressWarnings("static-access")
	private static void initOptions() {
		options = new Options();
		options.addOption("h", false, "print help");
		Option profile = OptionBuilder.withArgName("profile").hasArg()
				.withDescription("use validation profile").create("P");
		options.addOption(profile);
	}
}
