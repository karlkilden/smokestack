package se.smokestack.bm;

import java.io.File;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.server.CommandWrapper;
import se.bm.server.SystemConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadHelper {
	private static final Logger LOG = LogManager.getLogger();
	ObjectMapper mapper = new ObjectMapper();
	private File bmCommandFile;
	private BMCommand command;

	private CommandWrapper wrapper;

	public ReadHelper(SystemConfig systemConfig, String userDir) {
		LOG.debug("trying to read command at path {}", userDir);

		bmCommandFile = new File(userDir + BMCommand.BM_COMMAND);
		command = null;
		if (bmCommandFile.exists()) {
			createCommandFromFile();
			if (command.isHandled()) {
				LOG.info("{}: this bmCommand is already read", command.getUser());
			} else if (command.isInError()) {
				LOG.info("this bmCommand is in error");
			} else {
				if (!userDir.contains(command.getUser())) {
					throw new IllegalArgumentException("directory and bmcommand user is not matching");
				}
				LOG.info("bmCommand details: {}", command.toString());
				wrapper = new CommandWrapper(command, systemConfig, userDir, bmCommandFile);
			}
		}
	}

	private void createCommandFromFile() {
		try {
			command = mapper.readValue(bmCommandFile, BMCommand.class);
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	public boolean hasCommand() {
		return wrapper != null;
	}

	public CommandWrapper command() {
		return wrapper;
	}



}
