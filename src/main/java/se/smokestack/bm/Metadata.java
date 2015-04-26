package se.smokestack.bm;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Metadata implements Serializable {
	private static final long serialVersionUID = -4519914870328386451L;
	public static final String METADATA = "metadata.json";
	public static final String GET_FILE_NAME = "meta.get";
	public static final String PUT_FILE_NAME = "meta.put";

	private String user;
	private Command command;
	private boolean handled; 
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Metadata(Command command) {
		this.user = System.getProperty("user.name");
		this.command = command;
	}

	public Metadata() {
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

}
