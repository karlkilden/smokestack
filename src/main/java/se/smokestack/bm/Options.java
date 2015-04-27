package se.smokestack.bm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Options {

	@Parameter(description = "Commands")
	private List<String> commands = new ArrayList<>();

	@Parameter(names = "-force", description = "force tomcat service to stop")
	private boolean forceShutdown;

	public boolean isForceShutdown() {
		return forceShutdown;
	}

	public void setForceShutdown(boolean forceShutdown) {
		this.forceShutdown = forceShutdown;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
}
