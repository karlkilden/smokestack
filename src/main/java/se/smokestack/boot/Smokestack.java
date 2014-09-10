package se.smokestack.boot;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;

public class Smokestack {
	public static void main(String[] args) {

		CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
		cdiContainer.boot();

		// Starting the application-context allows to use @ApplicationScoped
		// beans
		ContextControl contextControl = cdiContainer.getContextControl();
		contextControl.startContext(ApplicationScoped.class);

		ApplicationStart appStart = BeanProvider
				.getContextualReference(ApplicationStart.class);
		appStart.toString();

		cdiContainer.shutdown();
	}
}