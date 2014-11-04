package se.smokestack.boot;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.parse.Nashorn;

@ApplicationScoped
public class ApplicationStart {
	private static final Logger log = LogManager.getLogger();

	@PostConstruct
	private void start() {
		log.info("starting up");
		Nashorn h = new Nashorn();
		h.test();
	}
}
