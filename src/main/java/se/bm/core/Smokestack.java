package se.bm.core;

import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.interpreter.ExpressionInterpreter;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.boot.JulLogHandler;

public class Smokestack implements ExpressionInterpreter<String, Boolean> {

	JulLogHandler handler = new JulLogHandler();

	private static final Logger LOG = LogManager.getLogger();

	private OptionsBase runtimeOptions;

	public static void main(String[] args) {

		CdiContainer cdiContainer = startContainer();

		BeanProvider.getContextualReference(BirdmanStartup.class).start(args);

		cdiContainer.shutdown();
		LOG.info("shutting down");
	}

	private static CdiContainer startContainer() {
		CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
		cdiContainer.boot();
		ContextControl contextControl = cdiContainer.getContextControl();
		contextControl.startContext(ApplicationScoped.class);
		return cdiContainer;
	}

	@Override
	public Boolean evaluate(String expression) {
		if (runtimeOptions == null) {
			return true;
		}
		try {
			Method getter = OptionsBase.class.getMethod("is" + StringUtils.capitalize(expression));
			boolean result = (Boolean) getter.invoke(runtimeOptions, new Object[] {});
			LOG.info("Extension {} activated:{}", expression, result);
			return !result;
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
			return null;
		}
	}
}