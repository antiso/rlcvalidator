package com.icoegroup.rlcvalidator;

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
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

		initOptions();
		String profileName = "default";
		String rlcFileName = null;
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h") || (cmd.getArgs().length == 0 && !cmd.hasOption("p"))) {
				HelpFormatter formatter = new HelpFormatter();
				formatter
						.printHelp("rlcvalidator [options] <rlcfile>", options);
				System.exit(INCORRECT_OPTIONS);
			}
			if (cmd.hasOption("p")) {
				System.setProperty("rlcvalidator.properties.file",
						"file:///" + cmd.getOptionValue("p"));
			}
			if (cmd.hasOption("P")) {
				profileName = cmd.getOptionValue("P");
			}
			if (cmd.getArgs().length !=0 ) rlcFileName = cmd.getArgs()[0];
		} catch (ParseException e1) {
			HelpFormatter formatter = new HelpFormatter();
			System.out.println("Error: " + e1.getMessage());
			formatter.printHelp("rlcvalidator [options] <rlcfile>", options);
			System.exit(INCORRECT_OPTIONS);
		}
		loadContext();

		try {
			final Context validationContext = ctx.getBean(Context.class);
			if (rlcFileName == null/*
					&& !"".equals(validatoinContext.getRlcFileName())*/)
				rlcFileName = validationContext
						.getRlcFileName();
			validationContext.setRlcFileName(rlcFileName);
			if ("default".equals(profileName))
				profileName = validationContext.getProfileName();

		} catch (ValidatorConfigurationException e) {
			System.err.println(e.getMessage());
			System.exit(INCORRECT_FILE_NAME);
		}
		IValidator profile = null;
		try {
			profile = (IValidator) ctx.getBean(profileName);
		} catch (BeansException e) {
			log.error("Can't load validation profile: ", e);
			System.exit(INCORRECT_PROFILE);
		}
		profile.validate();
	}

	private static void loadContext() {
		try {
			ctx = new ClassPathXmlApplicationContext("rlcvalidator.xml");
			ctx.registerShutdownHook();
		} catch (Exception e) {
			log.error("Can't load Spring configuration", e);
			System.exit(INCORRECT_PROFILE);
		}
	}

	@SuppressWarnings("static-access")
	private static void initOptions() {
		options = new Options();
		options.addOption("h", false, "print help");
		Option option = OptionBuilder
				.withArgName("profile")
				.hasArg()
				.withDescription(
						"use specified validation profile\n\t"
								+ "'default' profile used by default")
				.create("P");
		options.addOption(option);
		option = OptionBuilder.withArgName("property file").hasArg()
				.withDescription("use specified properties file").create("p");
		options.addOption(option);
	}
}
