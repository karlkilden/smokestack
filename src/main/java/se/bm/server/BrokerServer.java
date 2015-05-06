package se.bm.server;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.core.Smokestack;
import se.bm.server.ftp.ScriptKey;

@Specializes
@Exclude(onExpression = "broker", interpretedBy = Smokestack.class)
@ApplicationScoped
public class BrokerServer extends Server {
	private static final Logger LOG = LogManager.getLogger();

	private static final ScriptKey BROKER_FETCH = new ScriptKey(false, ScriptKey.BROKER_FETCH);
	private static final ScriptKey BROKER_PUT = new ScriptKey(false, ScriptKey.BROKER_PUT);

	@Inject
	private ServerConfig serverConfig;

	@Inject
	private BrokerConfig brokerConfig;

	@Inject
	private ScriptRunner scriptBuilder;

	public void runBroker(List<SystemConfig> systemConfigs) {
		if (serverConfig.isBroker()) {
			fetchServerResponse();
			putClientRequests();
		}
	}

	private void putClientRequests() {
		LOG.debug("brokerFetch");
		scriptBuilder.put(BROKER_PUT, serverConfig.getFtpDir(), brokerConfig.getPutDir());
	}

	private void fetchServerResponse() {
		LOG.debug("brokerFetch");
		scriptBuilder.fetch(BROKER_FETCH, serverConfig.getFtpDir(), serverConfig.getFtpFetchDir(), brokerConfig.getFetchDir());
	}

	@Override
	protected void doRun() {
		runBroker(systemConfigs);
		super.doRun();
	}
	
}
