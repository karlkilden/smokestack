package se.smokestack.bm.client;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;

import se.smokestack.bm.BMCommand;
import se.smokestack.bm.BMConfig;
import se.smokestack.bm.Command;
import se.smokestack.bm.Options;
import se.smokestack.boot.Dirs;

import com.beust.jcommander.JCommander;

@ApplicationScoped
public class BMClient {

	@Inject
	BMConfig bmConfig;

	public void runClientLoop(String[] args) {
		String[] a = new String[] { "-force", "trunk:war", };
		Options options = new Options();
		JCommander jcmd = new JCommander(options, a);
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
			Dirs.copy(Paths.get(targetPath), Paths.get(bmConfig.getBrokerDir() + "/" + cmd.getSystem()));
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}

	}
}
