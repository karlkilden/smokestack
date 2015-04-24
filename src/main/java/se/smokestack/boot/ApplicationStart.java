package se.smokestack.boot;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import se.smokestack.batch.MPSystem;

@ApplicationScoped
public class ApplicationStart {

	@Inject
	private MPSystem sys;

	@PostConstruct
	private void start() {
		try {
			CopyDir d = new CopyDir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	

}
