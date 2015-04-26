package se.smokestack.bm;

import static se.smokestack.bm.PropertyKeys.FTP_DIR;
import static se.smokestack.bm.PropertyKeys.FTP_BIRDMAN_DIR;
import static se.smokestack.bm.PropertyKeys.FTP_ULR;
import static se.smokestack.bm.PropertyKeys.LOCAL_TEMP_DIR;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.login.FailedLoginException;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class MetadataReader {

	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private WinSCPHandler winSCPHandler;

	@Inject
	private PropertyHelper propertyHelper;

	@Inject
	private IOHelper ioHelper;

	ObjectMapper mapper = new ObjectMapper();


	private Metadata metadata;

	public Optional<Metadata> getNextMeta() {

		WinSCPScript script = createScript();
		runScript(script);
		metadata = findNewMetadata();
		handleMetaData();
		return Optional.ofNullable(metadata);

	}

	private void handleMetaData() {
		if (metadata != null) {
			authenticate();
		}
	}

	private void authenticate() {

		if (!propertyHelper.getUsers().contains(metadata.getUser())) {
			ExceptionUtils.throwAsRuntimeException(new FailedLoginException("user:" + metadata.getUser() + " not found"));
		} else {
			LOG.info("authenticated: {}", metadata.getUser());
		}

	}

	private Metadata findNewMetadata() {
		try {
			for (String userDir : propertyHelper.getUserDirs()) {
				File metadataFile = new File(userDir + Metadata.METADATA);
				Metadata currentMeta = null;
				if (metadataFile.exists()) {

					currentMeta = mapper.readValue(metadataFile, Metadata.class);
					if (currentMeta.isHandled()) {
						LOG.info("this metadata is already read");
					} else {
						LOG.info("metadata details: {}", currentMeta.toString());
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
		ioHelper.winscp(script.getScriptName());
	}

	private WinSCPScript createScript() {
		WinSCPScript script = WinSCPScript.getInstance(Metadata.GET_FILE_NAME).get().destination(propertyHelper.get(LOCAL_TEMP_DIR)).target(propertyHelper.get(FTP_BIRDMAN_DIR))
				.ftpDir(propertyHelper.get(FTP_DIR)).ftpUrl(propertyHelper.get(FTP_ULR));

		winSCPHandler.writeScriptToDisk(script);
		return script;
	}


}
