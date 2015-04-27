package se.smokestack.bm;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
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
	private static final Logger AUDIT = LogManager.getLogger("se.smokestack.bm.log");

	@Inject
	private BMCommandReader metaHandler;

	@Inject
	private Instance<BMCommandHandler> handlers;

	@Inject
	private BMCommandWriter bmCommandWriter;

	@Produces
	@ApplicationScoped
	private BMConfig config;

	@Inject
	private BMClient client;

	public void fly(String[] params) {

		if (config.isClient()) {
			client.runClientLoop(params);
		}

		else {
			server();
		}

	}

	private void server() {
		Optional<BMCommand> data = metaHandler.getNextMeta();
		if (data.isPresent()) {
			config.addMetadata(data.get());
			LOG.info("Session started for {}", config.getBMCommand().getUser());
			extecuteCommand(data.get());
			server();
		} else {
			LOG.info("no bmCommand found");
		}
	}

	private void extecuteCommand(BMCommand data) {
		try {
			for (BMCommandHandler handler : handlers) {
				if (handler.command() == data.getCommand()) {
					handler.handle(data);
					bmCommandWriter.markHandled(data);
					AUDIT.info(data.getUser() + " " + data.getCommand() + " " + data.isHandled());
					if (data.isHandled()) {
						LOG.info("command {} was executed", handler.command());
					} else {
						LOG.error("command {} was not executed", handler.command());
					}
				}
			}
			if (!data.isHandled()) {
				throw new IllegalStateException("command " + data.getCommand() + "was not managed by any handler ");
			}
		} catch (Exception e) {
			bmCommandWriter.markInError(data);
			LOG.error("Error executing data. Will mark it in error. {}", data);
			LOG.error("", e);
		}
	}

	@PostConstruct
	private void initConfig() {
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
