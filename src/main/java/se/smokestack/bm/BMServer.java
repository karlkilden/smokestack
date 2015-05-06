//package se.smokestack.bm;
//
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
//import javax.enterprise.inject.Instance;
//import javax.inject.Inject;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import se.bm.core.Options;
//
//public class BMServer {
//	private static final Logger LOG = LogManager.getLogger();
//	private static final Logger AUDIT = LogManager.getLogger("se.smokestack.bm.log");
//	@Inject
//	private BMCommandReader commandReader;
//
//	@Inject
//	private Instance<BMCommandHandler> handlers;
//
//	@Inject
//	private BMCommandWriter bmCommandWriter;
//
//	@Inject
//	private BMConfig bmConfig;
//
//	@Inject
//	private IOHelper ioHelper;
//
//	@Inject
//	private BMBroker broker;
//
//	@Inject
//	private Options options;
//
//	public void runAsServer() {
//
//		server();
//
//		if (bmConfig.getLoopInterval() > 0) {
//			while (true) {
//				IOHelper.sleep(TimeUnit.MINUTES.toMillis(bmConfig.getLoopInterval()));
//				server();
//			}
//		}
//
//	}
//
//	private void server() {
//
//		broker.runBroker();
//		LOG.info("server:true");
//		WinSCPScript script = fetchServerGet();
//		runScript(script);
//
////		Optional<BMCommand> data = commandReader.getNextCommand();
//		if (data.isPresent()) {
//			bmConfig.addMetadata(data.get());
//			LOG.info("Session started for {}", bmConfig.getBMCommand().getUser());
//			extecuteCommand(data.get());
//			server();
//		} else {
//			LOG.info("no bmCommand found");
//
//		}
//	}
//
//	private void runScript(WinSCPScript script) {
//		ioHelper.winscp(script);
//	}
//
//	private WinSCPScript fetchServerGet() {
//		WinSCPScript script = WinSCPScript.getInstance(BMCommand.GET_FILE_NAME).get().destination(bmConfig.getServerTempDir())
//				.ftpDir(bmConfig.getFtpDir() + "//" + bmConfig.getFtpBmDir() + "//" + "serverget").target(bmConfig.getSystem())
//				.ftpUrl(bmConfig.getFtpURL());
//
//		if (!options.isNoClean()) {
//			script.removeTarget(true);
//		}
//		return script;
//	}
//
//	private void extecuteCommand(BMCommand data) {
//		try {
//			for (BMCommandHandler handler : handlers) {
//				if (handler.command() == data.getCommand()) {
//					handler.handle(data);
//					bmCommandWriter.markHandled(data);
//					AUDIT.info(data.getUser() + " " + data.getCommand() + " " + data.isHandled());
//					if (data.isHandled()) {
//						LOG.info("command {} was executed", handler.command());
//					} else {
//						LOG.error("command {} was not executed", handler.command());
//					}
//				}
//			}
//			if (!data.isHandled()) {
//				throw new IllegalStateException("command " + data.getCommand() + "was not managed by any handler ");
//			}
//		} catch (Exception e) {
//			bmCommandWriter.markInError(data);
//			LOG.error("Error executing data. Will mark it in error. {}", data);
//			LOG.error("", e);
//		}
//	}
//
//}
