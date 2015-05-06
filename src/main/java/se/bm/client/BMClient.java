package se.bm.client;

import static se.bm.client.PrintOut.nl;
import static se.bm.client.PrintOut.prompt;
import static se.bm.client.PrintOut.spaced;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.core.BMRunner;
import se.bm.core.ContextualMapper;
import se.bm.core.ConfigResolver;
import se.bm.core.RunModeType;
import se.bm.core.UserOptions;
import se.smokestack.bm.BMCommand;
import se.smokestack.bm.Command;
import se.smokestack.bm.IOHelper;

import com.beust.jcommander.JCommander;
import com.google.common.base.Strings;

@ApplicationScoped
public class BMClient implements BMRunner {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	@InjectableResource(location = "META-INF/maven/com.kildeen/smokestack/pom.properties")
	private Properties properties;

	@Inject
	ConfigResolver configResolver;

	@Inject
	private BrokerHelper brokerHelper;
	
	@Inject private IOHelper ioHelper;
	
	@Inject
	private ClientConfig clientConfig;
	
	@Inject
	private ClientCommand command;



	private enum ConsoleCommand {

		EXIT, RETRY, UPDATE, BROKER
	}

	private Scanner sc;

	private BMCommand cmd;

	private String warFileName;

	private String currentSystemPath;

	private UserOptions options;

	private ClientOptions runtimeOptions;
	


	public void start(String[] args) {

		runtimeOptions = new ClientOptions();
		new JCommander(runtimeOptions, args);

		if (runtimeOptions.isInteractive()) {
			doRunLoop();
		}

		else {
			runCommand(args);
		}

		String[] a = new String[] { "-force", "trunk:war", };
		try {
			runCommand(a);
		} catch (Exception e) {
			LOG.error("ERROR", e);
		}

	}

	private void doRunLoop() {
		nl(4);
		PrintOut.start();
		spaced("Version " + properties.getProperty("version"));
		spaced("Welcome user: " + System.getProperty("user.name"));

		prompt();
		sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			if (!Strings.isNullOrEmpty(input)) {
				try {
					if (!options.isKeepData()) {
						brokerHelper.clean(cmd);
					} else {
						LOG.warn("You might get a false positive on reciving response since keep data is enabled");
					}
					runCommand(input.split(" "));
				} catch (Exception e) {
					LOG.error("error", e);
				}
			}
			prompt();
		}
	}

	private void runCommand(String[] args) {
		options = new UserOptions();
		new JCommander(options, args);
		if (CollectionUtils.isEmpty(options.getCommands()) || options.getCommands().size() > 1) {
			throw new RuntimeException("Must have exactly one main param");
		}
		String[] cmdString = options.getCommands().get(0).split(":");
		if (cmdString.length == 1) {
			handleSpecialCommand(cmdString[0]);
		} else {
			runNewCommand(cmdString[0], cmdString[1]);
		}
	}

	public void runNewCommand(String systemString, String cmdString) {

		command.handle(systemString, cmdString, options);
		waitForServerResponse();
	}

	private void waitForServerResponse() {
		if (!options.isSkipWait()) {
			LOG.info("waiting for response");
			brokerHelper.getResponse(cmd);
		}
	}

	private void handleSpecialCommand(String string) {
		ConsoleCommand c = ConsoleCommand.valueOf(StringUtils.upperCase(string));

		if (c == ConsoleCommand.RETRY) {
			if (cmd == null) {
				LOG.warn("no command to retry");
			} else {
				runNewCommand(cmd.getSystem(), cmd.getCommand().toString());
			}
		} else if (c == ConsoleCommand.EXIT) {
			System.exit(0);
		} else if (c == ConsoleCommand.UPDATE) {
			// birdMan.initConfig();
		} else if (c == ConsoleCommand.BROKER) {
			// bmConfig.setBroker(true);
			// bmConfig.setLoopInterval(0);
			// server.runAsServer();
		}

	}





	@PreDestroy
	private void shutdown() {
		if (sc != null) {
			sc.close();
		}
	}

	@Override
	public String configPath() {
		return configResolver.getClientConfigPath();
	}

	@Override
	public void runValidation() {
		ioHelper.validatePath(clientConfig.getBrokerDir(), "broker must be configured");
	}

	@Override
	public RunModeType runModeType() {
		return RunModeType.CLIENT;
	}

}
