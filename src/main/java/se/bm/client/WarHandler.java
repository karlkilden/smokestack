package se.bm.client;

import java.util.Optional;

import javax.inject.Inject;

import se.smokestack.bm.Command;

public class WarHandler implements ClientHandler {
	@Inject
	private BrokerHelper brokerHelper;
	@Override
	public Command command() {
		return Command.WAR;
	}

	@Override
	public void handle() {
		brokerHelper.copyTo(clientCommand.cmd());
		
	}

	@Override
	public Optional<Command> next() {
		return Optional.empty();
	}

}
