package se.bm.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.bm.core.BMRunner;
import se.bm.core.ConfigResolver;
import se.bm.core.RunModeType;
import se.smokestack.bm.BMCommandReader;
import se.smokestack.bm.IOHelper;

@ApplicationScoped
public class Server implements BMRunner {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private ConfigResolver configResolver;

	@Inject
	private BMCommandReader commandReader;

	@Inject
	private IOHelper ioHelper;


	@Inject
	private ScriptRunner scriptRunner;

	@Inject
	private ServerConfig config;

	@Inject
	protected List<SystemConfig> systemConfigs;

	private volatile boolean keepRunning;
	
	@Override
	public String configPath() {
		return configResolver.getServerConfigPath();
	}

	@Override
	public void runValidation() {
		ioHelper.validatePath(config.getWinscpDir(), "winscpDir");
	}

	@Override
	public void start(String[] args) {
		
		while (isKeepRunning()) {
			IOHelper.sleep(TimeUnit.MINUTES.toMillis(1));
			LOG.debug("Running");
			doRun();
		}
	}

	protected void doRun() {

		for (SystemConfig systemConfig : systemConfigs) {
			fetchInput(systemConfig);
			commandReader.processCommands(systemConfig);
		}
	}

	private void fetchInput(SystemConfig systemConfig) {

		scriptRunner
				.fetch(systemConfig.getScriptKey(), systemConfig.getFtpFetchDir(), systemConfig.getSystem(), systemConfig.getInputDir());

	}

	public boolean isKeepRunning() {
		return keepRunning;
	}

	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	@Override
	public RunModeType runModeType() {
		return RunModeType.SERVER;
	}

}
