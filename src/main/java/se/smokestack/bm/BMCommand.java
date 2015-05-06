package se.smokestack.bm;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import se.bm.core.UserOptions;

public class BMCommand implements Serializable {
	private static final long serialVersionUID = -4519914870328386451L;
	public static final String BM_COMMAND = "bmCommand.json";
	public static final String GET_FILE_NAME = "command.get";
	public static final String PUT_FILE_NAME = "command.put";
	public static final String PUT_LOGS_FILE_NAME = "logs.put";
	public static final String CLIENT_FILE_NAME = "client.put";
	public static final String RESPONSE_GET = "response.get";
	public static final String FTP_PUT = "ftp.put";

	private String user;
	private Command command;
	private boolean handled; 
	private boolean inError;
	private String system;
	
	private UserOptions options;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public BMCommand(Command command) {
		this.user = System.getProperty("user.name");
		this.command = command;
	}

	public BMCommand() {
	}

	public Command getCommand() {
		return command;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public boolean isInError() {
		return inError;
	}

	public void setInError(boolean inError) {
		this.inError = inError;
	}

	public UserOptions getOptions() {
		return options;
	}

	public void setOptions(UserOptions options) {
		this.options = options;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public void setCommand(Command newCommand) {
		this.command = newCommand;
	}

}
