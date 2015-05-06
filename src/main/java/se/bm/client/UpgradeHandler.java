package se.bm.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.bm.BMCommand;
import se.smokestack.bm.Command;
import se.smokestack.bm.IOHelper;

public class UpgradeHandler implements ClientHandler {
	private static final Logger LOG = LogManager.getLogger();
	@Override
	public Command command() {
		return Command.UPGRADE;
	}

	@Override
	public void handle() {
		command.setCommand(Command.WAR);
		handleUpgrade(command);
	}

	private void handleUpgrade(ClientCommand command) {
		String path = "";
		File f = new File(path);
		if (f.exists()) {
			long timeStamp = f.lastModified();

			buildWar();
											// got the new war
			getWar(command, timeStamp, path);
		} else {
			LOG.warn("must have a war in warfiles folder to be able to upgrade");
		}

	}
	
	private void getWar(ClientCommand command, long timeStamp, String path) {
		BMCommand cmd = command.cmd();
		boolean copy = cmd.getOptions().isNoBuild();
		if (!cmd.getOptions().isNoBuild()) {
			IOHelper.sleep(120000L);
			long newStamp = waitForWar(timeStamp, path);
			copy = newStamp != timeStamp;
		}
		if (copy) {

			try {
				String target = command.putDir + "\\" + command.getWarFileName();
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
		// if (!cmd.getOptions().isNoBuild()) {
		//
		// HttpMethod triggerJob = new GetMethod(bmConfig.getJenkinsURL() +
		// "job/" + bmConfig.getJobNames().get(cmd.getSystem())
		// + "build?token=BUILD");
		// HttpClient client = new HttpClient();
		//
		// try {
		// client.executeMethod(triggerJob);
		// } catch (Exception e) {
		// System.out.println("Error talking to jenkins");
		// }
		// }
	}

	@Override
	public Optional<Command> next() {
		return Optional.of(Command.WAR);
	}
}
