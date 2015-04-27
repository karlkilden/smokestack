package se.smokestack.bm.client;

import static se.smokestack.bm.client.PrintOut.nl;
import static se.smokestack.bm.client.PrintOut.prompt;
import static se.smokestack.bm.client.PrintOut.spaced;
import static se.smokestack.bm.client.PrintOut.start;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.deltaspike.core.util.ExceptionUtils;

import se.smokestack.bm.BMCommand;
import se.smokestack.bm.BMCommandWriter;
import se.smokestack.bm.BMConfig;
import se.smokestack.bm.Command;
import se.smokestack.bm.Options;
import se.smokestack.boot.Dirs;

import com.beust.jcommander.JCommander;
import com.google.common.base.Strings;

@ApplicationScoped
public class BMClient {

	@Inject
	@InjectableResource(location = "META-INF/maven/com.kildeen/smokestack/pom.properties")
	private Properties properties;

	@Inject
	private BMCommandWriter bmCommandWriter;
	
	@Inject
	BMConfig bmConfig;

	public void runClientLoop(String[] args) {
		args = new String[] { "-i" };
		if (args.length == 1 && StringUtils.equals("-i", args[0])) {
			doRunLoop();
		}

		else {
			runCommand(args);
		}

		String[] a = new String[] { "-force", "trunk:war", };
		runCommand(a);

	}

	private void runCommand(String[] args) {
		Options options = new Options();
		JCommander jcmd = new JCommander(options, args);
		if (CollectionUtils.isEmpty(options.getCommands()) || options.getCommands().size() > 1) {
			throw new RuntimeException("Must have exactly one main param");
		}
		String[] cmdString = options.getCommands().get(0).split(":");
		BMCommand cmd = new BMCommand(Command.valueOf(StringUtils.upperCase(cmdString[1])));
		cmd.setUser(System.getProperty("user.name"));
		cmd.setSystem(cmdString[0]);
		cmd.setOptions(options);

		String targetPath = bmConfig.getSystems().get(cmd.getSystem());
		Objects.requireNonNull(targetPath, "system not configured " + cmd.getSystem());
		try {
			Dirs.copy(Paths.get(targetPath), Paths.get(bmConfig.getLocalTempDir() + "/" + cmd.getSystem()));
			bmCommandWriter.writeClientCommand(cmd);
			Dirs.copy(Paths.get(bmConfig.getLocalTempDir() + "/" + cmd.getSystem() + "/"), Paths.get(bmConfig.getBrokerDir() + "/" + cmd.getSystem()));
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	private void doRunLoop() {
		nl(4);
		start();
		spaced("Version " + properties.getProperty("version"));
		spaced("Welcome user: " + System.getProperty("user.name"));

		prompt();
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			if (!Strings.isNullOrEmpty(input)) {
				runCommand(input.split(" "));
			}
			prompt();
		}
	}
}
