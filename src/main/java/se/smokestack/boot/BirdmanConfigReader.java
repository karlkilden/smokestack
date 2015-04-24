package se.smokestack.boot;

import static se.smokestack.boot.Conf.split;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.resourceloader.FileResourceProvider;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

		Iterable<String> systems = split(p.getProperty("sys"));
		Iterable<String> commands = split(p.getProperty("cmd"));
		
		holder = new ConfHolder(p);
		for (String cmd : commands) {
			for (String sys : systems) {
				holder.add(cmd, sys);
			}
		}
	}
	



	public ConfHolder getConfHolder(String[] params) {
		return  holder.build(split(params[0]), params);
	}

}
