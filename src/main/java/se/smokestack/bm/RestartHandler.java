package se.smokestack.bm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.server.CommandWrapper;

@ApplicationScoped
public class RestartHandler implements BMCommandHandler {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private IOHelper ioHelper;


	@Override
	public Command command() {
		return Command.WAR;
	}

	public void handle(BMCommand data) {

		ioHelper.stopTomcat();

		ioHelper.startTomcat();

	}

	@Override
	public void handle(CommandWrapper commandWrapper) {
		// TODO Auto-generated method stub
		
	}

}
