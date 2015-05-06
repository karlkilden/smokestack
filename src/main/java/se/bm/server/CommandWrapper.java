package se.bm.server;

import java.io.File;

import se.smokestack.bm.BMCommand;

public class CommandWrapper {
	private BMCommand command;
	private SystemConfig config;
	private String userDir;
	private File commandFile;

	public CommandWrapper(BMCommand command, SystemConfig config, String userDir, File commandFile) {
		this.command = command;
		this.config = config;
		this.userDir = userDir;
		this.commandFile = commandFile;
	}

	public BMCommand get() {
		return command;
	}

	public SystemConfig getConfig() {
		return config;
	}

	public String getUserDir() {
		return userDir;
	}

	public File getCommandFile() {
		return commandFile;
	}

}
