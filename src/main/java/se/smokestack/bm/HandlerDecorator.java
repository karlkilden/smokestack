package se.smokestack.bm;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.server.CommandWrapper;

@Decorator
public class HandlerDecorator implements BMCommandHandler {
	private static final Logger LOG = LogManager.getLogger();
	private static final Logger AUDIT = LogManager.getLogger("se.smokestack.bm.log");
	@Inject
	private BMCommandWriter bmCommandWriter;
	@Inject
    @Delegate
    BMCommandHandler handler;

	@Override
	public Command command() {
		return handler.command();
	}

	@Override
	public void handle(CommandWrapper commandWrapper) {
		BMCommand command = commandWrapper.get();
		LOG.debug("command received");
		handler.handle(commandWrapper);
		bmCommandWriter.markHandled(command);
		if (command.isHandled()) {
			logSuccess(command);			
		}
		
	}
	public void logSuccess(BMCommand command) {
		AUDIT.info(command.getUser() + " " + command.getCommand() + " " + command.isHandled());
		if (command.isHandled()) {
			LOG.info("command {} was executed", command.getCommand());
		} else {
			LOG.error("command {} was not executed", command.getCommand());
		}		
	}
	
}
