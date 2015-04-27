package se.smokestack.bm;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class BMCommandReader {

	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private BMConfig bmConfig;

	@Inject
	private IOHelper ioHelper;

	ObjectMapper mapper = new ObjectMapper();

	public Optional<BMCommand> getNextMeta() {

		WinSCPScript script = createScript();
		runScript(script);
		BMCommand bmCommand = findNewMetadata();
		return Optional.ofNullable(bmCommand);
	}

	private BMCommand findNewMetadata() {
		try {
			for (String userDir : bmConfig.getUserDirs()) {
				File bmCommandFile = new File(userDir + BMCommand.BM_COMMAND);
				BMCommand currentMeta = null;
				if (bmCommandFile.exists()) {
					currentMeta = mapper.readValue(bmCommandFile, BMCommand.class);
					if (currentMeta.isHandled()) {
						LOG.info("{}: this bmCommand is already read", currentMeta.getUser());
					} else if (currentMeta.isInError()) {
						LOG.info("this bmCommand is in error");
					} else {
						if (!userDir.contains(currentMeta.getUser()))  {
							throw new IllegalArgumentException("directory and bmcommand user is not matching");
						}
						LOG.info("bmCommand details: {}", currentMeta.toString());
						return currentMeta;
					}
				}
			}
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
		return null;
	}

	private void runScript(WinSCPScript script) {
		ioHelper.winscp(script);
	}

	private WinSCPScript createScript() {
		WinSCPScript script = WinSCPScript.getInstance(BMCommand.GET_FILE_NAME).get().destination(bmConfig.getLocalTempDir())
				.target(bmConfig.getFtpBmDir()).ftpDir(bmConfig.getFtpDir()).ftpUrl(bmConfig.getFtpURL());
		return script;
	}

}
