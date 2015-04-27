package se.smokestack.bm;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class WarFileHandler extends BMCommandHandler {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private IOHelper ioHelper;
	
	@Inject
	private BMConfig bmConfig;
	
	@Override
	public Command command() {
		return Command.RESTART;
	}

	@Override
	public void handle(BMCommand data) {
				
		if (bmConfig.isShutdownWhenNewWar()) {
			ioHelper.stopTomcat();			
		}
	    replaceWar();
	    
	    ioHelper.startTomcat();
	    
	}

	private void replaceWar() {
		try {
			Files.copy(bmConfig.fullWarFilePath(), bmConfig.fullWarFileTargetPath(), REPLACE_EXISTING);
			
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}



}
