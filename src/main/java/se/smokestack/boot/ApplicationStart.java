package se.smokestack.boot;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.beust.jcommander.JCommander;

import se.smokestack.bm.Birdman;
import se.smokestack.bm.Options;

@ApplicationScoped
public class ApplicationStart {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private Birdman birdMan;

	@Inject
	private Event<ExceptionToCatchEvent> event;
	@Produces
	private Options options;
	
	@PostConstruct
	private void start() {

	}

	public void run(String[] params) {
		options = new Options();
		new JCommander(options, params);
		LOG.info("params: {}", ArrayUtils.toString(params,null));
		
		try {
			
			birdMan.fly(params);
		} catch (Exception e) {
			event.fire(new ExceptionToCatchEvent(e));
		}

	}

}
