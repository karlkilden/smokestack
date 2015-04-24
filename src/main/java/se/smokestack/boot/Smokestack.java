package se.smokestack.boot;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;

public class Smokestack {
	public static void main(String[] args) {
        Logger logger = Logger.getLogger("org.apache.webbeans.corespi.scanner.AbstractMetaDataDiscovery");
        logger.setLevel(Level.SEVERE);
		CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
		cdiContainer.boot();

		// Starting the application-context allows to use @ApplicationScoped
		// beans
		ContextControl contextControl = cdiContainer.getContextControl();
		contextControl.startContext(ApplicationScoped.class);

		ApplicationStart appStart = BeanProvider
				.getContextualReference(ApplicationStart.class);
		appStart.fly(args);

		cdiContainer.shutdown();
	}
}