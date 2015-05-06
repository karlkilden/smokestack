package se.bm.core;

import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import se.bm.client.ClientConfig;
import se.bm.server.ServerConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class ConfigResolver {

	private String conf = PathConstants.CONF;
	private String systemConfigExtension = PathConstants.SYSTEM_CONFIG_EXT;
	private String serverConfigPath = PathConstants.SERVER_CONF_PATH;
	private String clientConfigPath = PathConstants.CLIENT_CONF_PATH;
	private String home = System.getProperty("user.dir") + Constants.SLASH;

	private class PathConstants {

		private PathConstants() {
		}

		private static final String CONF = "conf" + Constants.SLASH;
		private static final String CLIENT_CONF_PATH = CONF + "client";
		private static final String SERVER_CONF_PATH = CONF + "server";
		private static final String SYSTEM_CONFIG_EXT = ".sys";
	}

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public String getSystemConfigExtension() {
		return systemConfigExtension;
	}

	public void setSystemConfigExtension(String systemConfigExtension) {
		this.systemConfigExtension = systemConfigExtension;
	}

	public String getServerConfigPath() {
		return serverConfigPath;
	}

	public void setServerConfigPath(String serverConfigPath) {
		this.serverConfigPath = serverConfigPath;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getClientConfigPath() {
		return clientConfigPath;
	}

	private static final Logger LOG = LogManager.getLogger();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private String getjson(Path path) throws IOException {
		final String confString = new String(readAllBytes(path));
		final String jsonString = JsonValue.readHjson(confString).toString(Stringify.PLAIN);
		return jsonString;
	}

	public <E> E getObject(Path path, Class<? extends E> clazz) {
		LOG.info("Getting object of type {} from path {}", clazz.getSimpleName(), path);
		try {
			return MAPPER.readValue(getjson(path), clazz);
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
		return null;
	}

	public <E> E getObject(String path, Class<? extends E> clazz) {
		return getObject(Paths.get(path), clazz);
	}

	public ServerConfig readServerConfig() {
		return getObject(getServerConfigPath(), ServerConfig.class);

	}

	public ClientConfig readClientConfig() {
		return getObject(getClientConfigPath(), ClientConfig.class);
	}
}
