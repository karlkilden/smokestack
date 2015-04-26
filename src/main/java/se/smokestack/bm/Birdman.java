package se.smokestack.bm;

import java.util.Optional;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.resourceloader.FileResourceProvider;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class Birdman {

	public static final String METADATA_FILE_NAME = "meta";
	private static final String PROPERTIES_FILE_NAME = "bm.properties";
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	@InjectableResource(resourceProvider = FileResourceProvider.class, location = PROPERTIES_FILE_NAME)
	private Properties properties;

	@Inject
	private MetadataReader metaHandler;

	@Inject
	private Instance<CommandHandler> handlers;

	public void run() {

		Optional<Metadata> data = metaHandler.getNextMeta();

		if (data.isPresent()) {
			extecuteCommand(data.get());
		}
		else {
			LOG.info("no metadata found");
		}

		// String command =
		// "cmd /c C:/projects/WinSCP/WinSCP.exe /script=C:/projects/WinSCP/test.script";
		// processRunner.runProcess(command);

	}

	private void extecuteCommand(Metadata data) {
		for (CommandHandler handler : handlers) {
			if (handler.command() == data.getCommand()) {
				handler.handle(data);
				if (data.isHandled()) {
					LOG.info("command {} was executed", handler.command());
				} else {
					LOG.error("command {} was not executed", handler.command());
				}
			}
		}

	}
}
