package se.smokestack.bm.client;

import static se.smokestack.bm.client.PrintOut.nl;
import static se.smokestack.bm.client.PrintOut.prompt;
import static se.smokestack.bm.client.PrintOut.spaced;
import static se.smokestack.bm.client.PrintOut.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.bm.BMCommand;
import se.smokestack.bm.BMConfig;
import se.smokestack.bm.BMServer;
import se.smokestack.bm.Birdman;
import se.smokestack.bm.Command;
import se.smokestack.bm.IOHelper;
import se.smokestack.bm.Options;

import com.beust.jcommander.JCommander;
import com.google.common.base.Strings;

@ApplicationScoped
public class BMClient {
	private static final Logger LOG = LogManager.getLogger();
	private static final Logger DEBUG = LogManager.getLogger("se.smokestack.bm.debug");

	@Inject
	@InjectableResource(location = "META-INF/maven/com.kildeen/smokestack/pom.properties")
	private Properties properties;

	@Inject
	BMConfig bmConfig;

	@Inject
	private BrokerHelper brokerHelper;

	@Inject
	BMServer server;

	@Inject
	Birdman birdMan;

	private enum ConsoleCommand {

		EXIT, RETRY, UPDATE, BROKER
	}

	private Scanner sc;

	private BMCommand cmd;

	private String warFileName;

	private String currentSystemPath;

	private Options options;

	public void runAsClient(String[] args) {

		args = new String[] { "-i" };
		if (args.length == 1 && StringUtils.equals("-i", args[0])) {
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

	private void runCommand(String[] args) {
		options = new Options();
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
		
		cmd = new BMCommand(Command.valueOf(StringUtils.upperCase(cmdString)));
		cmd.setUser(System.getProperty("user.name"));
		cmd.setSystem(systemString);
		warFileName = bmConfig.getWarNames().get(cmd.getSystem());
		currentSystemPath = bmConfig.getSystems().get(cmd.getSystem());
		cmd.setOptions(options);
		if (!options.isKeepData())  {
			brokerHelper.clean(cmd);			
		}
		else {
			LOG.warn("You might get a false positive on reciving response since keep data is enabled");
		}
		if (cmd.getCommand() == Command.UPGRADE) {
			handleUpgrade();
		}
		
		if (cmd.getCommand() != Command.RESPONSE) {
			
			copyCommandAndFilesToBroker();
		}

		waitForServerResponse();
	}

	private void waitForServerResponse() {
		if(!options.isSkipWait()) {
			LOG.info("waiting for response");			
			brokerHelper.getResponse(cmd);
		}
	}

	private void copyCommandAndFilesToBroker() {
		brokerHelper.copyTo(cmd);
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
			birdMan.initConfig();
		} else if (c == ConsoleCommand.BROKER) {
			bmConfig.setBroker(true);
			bmConfig.setLoopInterval(0);
			server.runAsServer();
		}

	}

	private void handleUpgrade() {
		String path = bmConfig.getWarfilesLocation() + warFileName;
		LOG.info("getting stamp from:{}", path);
		File f = new File(path);
		if (f.exists()) {
			long timeStamp = f.lastModified();

			buildWar();
			cmd.setCommand(Command.WAR); // Server does not care how the client got the new war
			getWar(timeStamp, path);
		} else {
			LOG.warn("must have a war in warfiles folder to be able to upgrade");
		}

	}

	private void getWar(long timeStamp, String path) {
		boolean copy = cmd.getOptions().isNoBuild();
		if (!cmd.getOptions().isNoBuild()) {
			IOHelper.sleep(120000L);
			long newStamp = waitForWar(timeStamp, path);
			copy = newStamp != timeStamp;
		}
		if (copy) {

			try {
				String target = currentSystemPath + "\\" + warFileName;
				Files.copy(Paths.get(path), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				LOG.error("Error fetching war", e);
			}
		}
	}

	private long waitForWar(long timeStamp, String path) {
		long newStamp = -1;
		int tries = 0;
		while (newStamp != timeStamp) {
			if (tries == 3) {
				LOG.warn("No war file found - aborting");
				break;
			}
			File f = new File(path);
			newStamp = f.lastModified();
			IOHelper.sleep(30000L);
			tries++;
			LOG.info("No war found - waiting");
		}
		return newStamp;
	}

	private void buildWar() {
		if (!cmd.getOptions().isNoBuild()) {

			HttpMethod triggerJob = new GetMethod(bmConfig.getJenkinsURL() + "job/" + bmConfig.getJobNames().get(cmd.getSystem())
					+ "build?token=BUILD");
			HttpClient client = new HttpClient();

			try {
				client.executeMethod(triggerJob);
			} catch (Exception e) {
				System.out.println("Error talking to jenkins");
			}
		}
	}

	private void doRunLoop() {
		nl(4);
		start();
		spaced("Version " + properties.getProperty("version"));
		spaced("Welcome user: " + System.getProperty("user.name"));

		prompt();
		sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			if (!Strings.isNullOrEmpty(input)) {
				try {
					runCommand(input.split(" "));
				} catch (Exception e) {
					LOG.error("error", e);
				}
			}
			prompt();
		}
	}

	@PreDestroy
	private void shutdown() {
		if (sc != null) {
			sc.close();
		}
	}
}
