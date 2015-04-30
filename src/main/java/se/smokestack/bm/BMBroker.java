package se.smokestack.bm;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BMBroker {
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private BMCommandWriter bmCommandWriter;

	@Inject
	private BMConfig bmConfig;

	@Inject
	private Options options;

	@Inject
	private IOHelper ioHelper;

	public void runBroker() {
		LOG.info("broker:{}", bmConfig.isBroker());
		if (bmConfig.isBroker() && !options.isResume()) {
			brokerGet();
			bmCommandWriter.copyBrokerFilesToServerDir();
		}
	}

	public void brokerGet() {

		WinSCPScript script = WinSCPScript.getInstance(BMCommand.RESPONSE_GET).get().destination(bmConfig.getBrokerResponseDir())
				.ftpDir(bmConfig.getFtpDir()+"//"+bmConfig.getFtpBmDir()).target("brokerget").ftpUrl(bmConfig.getFtpURL());
		ioHelper.winscp(script);
	}
}
