package se.bm.core;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class OptionsBase {

	@Parameter(description = "Commands")
	private List<String> commands = new ArrayList<>();

	@Parameter(names = "-broker", description = "if server and broker")
	private boolean broker;

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public boolean isBroker() {
		return broker;
	}

	public void setBroker(boolean broker) {
		this.broker = broker;
	}

}
