package se.smokestack.boot;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.parse.Nashorn;

@ApplicationScoped
public class ApplicationStart {
	
//	@Inject
//	@InjectableResource(resourceProvider = FileResourceProvider.class, location = "build.xml")
//	private InputStream inputStream;
	private static final Logger log = LogManager.getLogger();
	
	@Inject
	Nashorn nashhorn;
	
	@PostConstruct
	private void start() {
//		nashhorn.manipulate(null);
		System.out.println(nashhorn.replace("1hej2hej3hej"));
	}
}
