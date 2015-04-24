package se.smokestack.boot;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import se.smokestack.batch.MPSystem;

@ApplicationScoped
public class ApplicationStart {

	@Inject
	private MPSystem mpSystem;
	
	@Inject
	private BirdmanConfigReader birdmanConfigReader;

	@PostConstruct
	private void start() {
		
		
	}	
	
	public void fly(String[] params) {
		ConfHolder confHolder = birdmanConfigReader.getConfHolder(params);
		
		mpSystem.handleConfs(confHolder);

		
	}
	

}
