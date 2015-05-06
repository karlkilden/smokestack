package se.bm.core;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ProxyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class BirdmanStartup  {

	@Inject
	private Instance<BMRunner> runners;


	private static final Logger LOG = LogManager.getLogger();

	public void start(String[] args) {

		BMRunner foundRunner = null;
		for (BMRunner runner : runners) {
			File file = new File(runner.configPath());

			if (file.exists()) {
				LOG.info("runner config found for {}", ProxyUtils.getUnproxiedClass(runner.getClass()).getSimpleName());
				if( foundRunner != null) {
					LOG.warn("several runmodes found");
				}
				foundRunner = runner;
			}
		}
		if (foundRunner == null) {
			LOG.error("no runner found");
		}
		else {
			foundRunner.start(args);			
		}

	}


}