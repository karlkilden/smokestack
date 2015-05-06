package se.bm.server;

import static com.google.common.io.Files.createParentDirs;
import static java.nio.file.Files.readAllBytes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import se.bm.core.ConfigResolver;
import se.bm.core.Constants;
import se.bm.core.ConfigResolver;
import se.bm.server.ftp.ScriptKey;
import se.smokestack.boot.Dirs;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class ServerBuilder {
	private static final Logger LOG = LogManager.getLogger();

	private Map<String, SystemConfig> systemsMap = new HashMap<>();
	private List<SystemConfig> systems = new ArrayList<>();

	@Inject
	private ConfigResolver configResolver;
	
	@Inject
	private ServerConfig serverConfig;
	
	@Inject
	private ConfigResolver helper;


	public void setupServerConfigs() {
		LOG.info("reading confs");
		try {
			readConfiguredSystems();
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}



	private void readConfiguredSystems() {
		List<Path> srvs = Dirs.getAllFiles(configResolver.getConf(), configResolver.getSystemConfigExtension());
		for (Path path : srvs) {
			SystemConfig currentSystem = helper.getObject(path, SystemConfig.class);
			currentSystem.setSystem(path.toFile().getName().replace(configResolver.getSystemConfigExtension(), ""));
			currentSystem.setScriptKey(new ScriptKey(false, "system."+currentSystem.getSystem()+".txt"));
			currentSystem.setFtpFetchDir(serverConfig.getFtpFetchDir());
			currentSystem.setFtpPutDir(serverConfig.getFtpPutDir() + Constants.SLASH);
			currentSystem.setInputDir(serverConfig.getFetchDir() + currentSystem.getSystem() + Constants.SLASH);
			currentSystem.setOutPutDir(serverConfig.getPutDir() + currentSystem.getSystem() + Constants.SLASH);
			createDirs(currentSystem);
			systemsMap.put(currentSystem.getSystem(), currentSystem);
			systems.add(currentSystem);
			LOG.info("found config:{}", currentSystem.getSystem());
		}
	}

	private void createDirs(SystemConfig currentSystem) {
		try {
			createParentDirs(new File(currentSystem.getInputDir()));
			createParentDirs(new File(currentSystem.getOutPutDir()));
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	public Map<String, SystemConfig> systems() {
		return systemsMap;
	}


	@Produces
	public List<SystemConfig> getSystems() {
		if (systems == null) {
			readConfiguredSystems();
		}
		return systems;
	}
}
