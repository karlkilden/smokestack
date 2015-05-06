package se.smokestack.bm;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.server.CommandWrapper;
import se.smokestack.boot.Dirs;

@ApplicationScoped
public class LogsHandler implements BMCommandHandler {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private IOHelper ioHelper;
	
	BMConfig bmConfig;

	@Override
	public Command command() {
		return Command.LOGS;
	}

	public void handle(BMCommand data) {
		copyLogs();
	}
	
	private void copyLogs() {
		copyFilesToTempDir();
		WinSCPScript script = WinSCPScript.getWriteToBrokerInstance(BMCommand.PUT_LOGS_FILE_NAME, bmConfig).put().target(bmConfig.fullLogsTargetPath().toString());
		
		ioHelper.winscp(script);
		
	}

	private void copyFilesToTempDir() {
		try {
			Dirs.copy(bmConfig.fullLogsPath(), bmConfig.fullLogsTargetPath());
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}		
	}

	@Override
	public void handle(CommandWrapper commandWrapper) {
		// TODO Auto-generated method stub
		
	}
}
