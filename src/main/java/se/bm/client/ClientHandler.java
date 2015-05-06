package se.bm.client;

import java.util.Optional;

import se.smokestack.bm.Command;

public interface ClientHandler {

	Command command();

	void handle();
	
	public Optional<Command> next();

}
