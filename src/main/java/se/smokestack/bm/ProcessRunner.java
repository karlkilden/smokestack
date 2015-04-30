package se.smokestack.bm;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcessRunner {
	private static final Logger log = LogManager.getLogger();

	public void runProcess(String command) {

		log.debug("running cmd: {}", command);
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	public void runBat(String brokerbatPath) {
		runProcess("cmd /c start " + brokerbatPath);
	}

}
