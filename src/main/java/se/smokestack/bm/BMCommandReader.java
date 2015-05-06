package se.smokestack.bm;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import se.bm.server.SystemConfig;

@ApplicationScoped
public class BMCommandReader {

	@Inject
	private Instance<BMCommandHandler> handlers;

	private Map<Command, BMCommandHandler> handlerMap = new HashMap<>();

	public void processCommands(SystemConfig systemConfig) {

		for (String user : systemConfig.getUsers()) {
			String userDir = systemConfig.getInputDir() + user;
			ReadHelper helper = new ReadHelper(systemConfig, userDir);
			if (helper.hasCommand()) {
				BMCommand bmCommand = helper.command().get();
				handlerMap.get(bmCommand).handle(helper.command());
			}
		}

	}

	@PostConstruct
	private void setup() {

		for (BMCommandHandler handler : handlers) {
			handlerMap.put(handler.command(), handler);
		}

	}

}
