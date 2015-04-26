package se.smokestack.bm;

import static se.smokestack.bm.PropertyKeys.TOMEE_HOST;
import static se.smokestack.bm.PropertyKeys.TOMEE_SHUTDOWN_PORT;
import static se.smokestack.bm.PropertyKeys.WAR_FILE_NAME;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class WarFileHandler extends CommandHandler {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private IOHelper ioHelper;
	
	@Override
	public Command command() {
		return Command.WAR;
	}

	@Override
	public void handle(Metadata data) {
		
		boolean stopTomcat = Boolean.parseBoolean(prop(PropertyKeys.TOMEE_STOP_BEFORE_REPLACE_WAR));
		if (stopTomcat) {
			ioHelper.stopTomcat();			
			sleep(Long.parseLong(prop(PropertyKeys.TOMEE_SHUTDOWN_SLEEP)));
		}
		String warFileTarget = prop(PropertyKeys.TOMEE_HOME) + "webapps/" + prop(WAR_FILE_NAME);
	    try {
			Files.copy(Paths.get(userDir(data)+"/"+prop(WAR_FILE_NAME)), Paths.get(warFileTarget), StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}

		
		super.markHandled(data);

	}



}
