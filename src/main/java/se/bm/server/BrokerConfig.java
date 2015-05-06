package se.bm.server;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import se.bm.core.Constants;
import se.bm.core.ConfigResolver;
import se.bm.core.Constants.Broker;

@ApplicationScoped
public class BrokerConfig {
	@Inject
	ConfigResolver configResolver;
	private transient String home;
	private transient String putDir;
	private transient String fetchDir;


	public String getPutDir() {
		return putDir;
	}

	public void setPutDir(String putDir) {
		this.putDir = putDir;
	}

	public String getFetchDir() {
		return fetchDir;
	}

	public void setFetchDir(String fetchDir) {
		this.fetchDir = fetchDir;
	}



	@PostConstruct
	private void init() {
		home = configResolver.getHome() + Constants.Broker.HOME;
		putDir = home + Constants.PUT;
		fetchDir = home + Constants.FETCH;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

}
