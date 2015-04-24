package se.smokestack.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.resourceloader.FileResourceProvider;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Splitter;
@ApplicationScoped
public class BirdmanConfigReader {
	@Inject
	@InjectableResource(resourceProvider = FileResourceProvider.class, location = "cmd.properties")
	private Properties p;
	private static final Logger log = LogManager.getLogger();
	private ConfHolder holder;

	@PostConstruct
	public void test() {
		log.info(p);

		Iterable<String> systems = split(p.getProperty("cmd"));
		Iterable<String> commands = split(p.getProperty("sys"));
		
		holder = new ConfHolder();
		for (String cmd : commands) {
			for (String sys : systems) {
				holder.add(cmd, sys, p);
			}
		}
	}
	
	 private Iterable<String> split(String token) {
		 return Splitter.on(',')
			       .trimResults()
			       .omitEmptyStrings()
			       .split(token);
	 }


	public ConfHolder getConfHolder(String[] params) {
		return  holder.build(split(params[0]), params);
	}

}
