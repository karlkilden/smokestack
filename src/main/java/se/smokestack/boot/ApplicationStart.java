package se.smokestack.boot;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.smokestack.bm.Birdman;
import se.smokestack.bm.Command;
import se.smokestack.bm.MPSystem;
import se.smokestack.bm.Metadata;

@ApplicationScoped
public class ApplicationStart {
	private static final Logger log = LogManager.getLogger();

	@Inject
	private MPSystem mpSystem;
	
	@Inject
	private BirdmanConfigReader birdmanConfigReader;
	
	@Inject
	private Birdman birdMan;
	
	@Inject
	private Event<ExceptionToCatchEvent> event;

	@PostConstruct
	private void start() {
		System.out.println(System.getProperty("user.name"));
		
	}	
	
	public void fly(String[] params) {
		Metadata data = new Metadata(Command.WAR);
		data.setUser("Kalle");
		data.setHandled(true);


		
//		ConfHolder confHolder = birdmanConfigReader.getConfHolder(params);
		
//		mpSystem.handleConfs(confHolder);
		try {
			birdMan.run();
		} catch (Exception e) {
			event.fire(new ExceptionToCatchEvent(e));
		}

		
	}
	

}
