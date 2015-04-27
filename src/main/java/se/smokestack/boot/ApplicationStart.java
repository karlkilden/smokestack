package se.smokestack.boot;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.bm.Birdman;

@ApplicationScoped
public class ApplicationStart {
	private static final Logger log = LogManager.getLogger();

	@Inject
	private Birdman birdMan;

	@Inject
	private Event<ExceptionToCatchEvent> event;

	@PostConstruct
	private void start() {
		System.out.println(System.getProperty("user.name"));

	}

	public void run(String[] params) {
		
		
		
		try {
			birdMan.fly(params);
		} catch (Exception e) {
			event.fire(new ExceptionToCatchEvent(e));
		}

	}

}
