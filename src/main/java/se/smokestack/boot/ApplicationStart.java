package se.smokestack.boot;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class ApplicationStart {
	private static final Logger log = LogManager.getLogger();

	@PostConstruct
	private void start() {
		log.info("starting up");
	}
}
