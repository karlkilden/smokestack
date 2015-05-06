package se.bm.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.bm.BMCommand;
import se.smokestack.bm.BMCommandWriter;
import se.smokestack.bm.BMConfig;
import se.smokestack.bm.IOHelper;
import se.smokestack.boot.Dirs;

public class BrokerHelper {
	private static final Logger LOG = LogManager.getLogger();


	@Inject
	private BMCommandWriter bmCommandWriter;

	public void copyTo(ClientCommand command) {
		String systemUserBrokerDir = bmConfig.brokerDir() + "\\" + cmd.getSystem() + "\\" + cmd.getUser();
		requireDir(systemUserBrokerDir, "broker dir must be configured and a subfolder for each user (another subfolder for each system)");

		String localTempDirForSystem = bmConfig.getLocalTempDir() + cmd.getSystem();
		requireDir(localTempDirForSystem, "a local temp folder must be configured and a subfolder for each system");

		try {
			Dirs.copy(Paths.get(dropPath), Paths.get(localTempDirForSystem));
			bmCommandWriter.writeClientCommand(cmd);
			Dirs.copy(Paths.get(localTempDirForSystem), Paths.get(systemUserBrokerDir));
			LOG.info("copied to broker: {}", systemUserBrokerDir);
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	public void copyFrom(BMCommand cmd) {
		String getPath = getGetPath(cmd);

		requireDir(getPath, "get dir for " + cmd.getSystem() + " is missing");
		requireDir(bmConfig.brokerDir(), "broker dir must be configured");

		String systemUserBrokerDir = getFromDir(cmd);
		requireDir(systemUserBrokerDir, "broker dir must be configured and a subfolder for each system (another subfolder for user system)");

		try {
			Dirs.copy(Paths.get(systemUserBrokerDir), Paths.get(getPath));
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	private String getFromDir(BMCommand cmd) {
		return bmConfig.getBrokerParentDir() + "\\" + "broker\\brokerget" + "\\" + cmd.getSystem() + "\\" + cmd.getUser();
	}

	private String getGetPath(BMCommand cmd) {
		return bmConfig.getSystems().get(cmd.getSystem()) + "\\" + "get" + "\\" + cmd.getSystem() + "\\";
	}

	private void requireDir(String dropPath, String errorMsg) {
		require(dropPath, errorMsg, true);
	}

	private void requireFile(String dropPath, String errorMsg) {
		Objects.requireNonNull(dropPath, errorMsg);
		File test = new File(dropPath);
		if (test.exists()) {
		}
	}

	private void require(String dir, String errorMsg, boolean directory) {
		Objects.requireNonNull(dir, errorMsg);
		File test = new File(dir);
		if (!test.exists()) {
			LOG.error("The directory {} does not exist. {}", dir, errorMsg);
		} else if (directory && !test.isDirectory()) {
			LOG.error("Not a directory. {}", dir, errorMsg);

		}
	}

	public void clean(BMCommand cmd) {
		Dirs.cleanIfExists(Paths.get(getGetPath(cmd)));
	}

	public void getResponse(BMCommand cmd) {
		boolean responseFound = false;
		long timeout = 200000;
		long sleep = 20000L;
		long wait = 0;
		while (!responseFound) {
			IOHelper.sleep(wait);
			wait = wait + sleep;
			copyFrom(cmd);
			if (wait == timeout) {
				LOG.warn("get response timed out");
			}
			File f = new File(getGetPath(cmd) + BMCommand.BM_COMMAND);
			responseFound = f.exists();
			if (responseFound) {
				LOG.info("Response received");
				Dirs.cleanIfExists(Paths.get(getFromDir(cmd)));
			}
		}
	}

}
