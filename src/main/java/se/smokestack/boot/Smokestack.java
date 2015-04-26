package se.smokestack.boot;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Smokestack {
	
	JulLogHandler handler = new JulLogHandler();
	
	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {


		CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
		cdiContainer.boot();

		// Starting the application-context allows to use @ApplicationScoped
		// beans
		ContextControl contextControl = cdiContainer.getContextControl();
		contextControl.startContext(ApplicationScoped.class);

		ApplicationStart appStart = BeanProvider
				.getContextualReference(ApplicationStart.class);
		try {
			appStart.fly(args);
		} catch (Exception e) {
			
		}
		cdiContainer.shutdown();
		log.info("shutting down");
	}
}