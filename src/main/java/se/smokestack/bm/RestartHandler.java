package se.smokestack.bm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class RestartHandler extends BMCommandHandler {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private IOHelper ioHelper;


	@Override
	public Command command() {
		return Command.WAR;
	}

	@Override
	public void handle(BMCommand data) {

		ioHelper.stopTomcat();

		ioHelper.startTomcat();

	}

}
