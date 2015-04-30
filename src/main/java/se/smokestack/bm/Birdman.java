package se.smokestack.bm;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import se.smokestack.bm.client.BMClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class Birdman {

	public static final String METADATA_FILE_NAME = "meta";
	private static final String BM_CONFIG_FILE = "conf/bmconfig.hjson";
	private static final Logger LOG = LogManager.getLogger();
	private static final Logger DEBUG = LogManager.getLogger("se.smokestack.bm.debug");

	@Produces
	@ApplicationScoped
	private BMConfig config;

	@Inject
	private BMClient client;

	@Inject
	private BMServer bmServer;


	public void fly(String[] params) {

		if (config.isClient()) {
			LOG.info("Running as client");
			client.runAsClient(params);
		}

		else {
			LOG.info("Running as server");
			bmServer.runAsServer();
		}

	}

	@PostConstruct
	public void initConfig() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String confString = new String(readAllBytes(get(BM_CONFIG_FILE)));
			String jsonString = JsonValue.readHjson(confString).toString(Stringify.PLAIN);
			config = mapper.readValue(jsonString, BMConfig.class);
			config.postConstruct();

		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}

	}
}
